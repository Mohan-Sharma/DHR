package com.master.view;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.patientadmission.domain.AdmittingDepartment;
import com.patientadmission.domain.Doctor;
import com.patientadmission.domain.User;
import com.patientadmission.master.dto.DoctorDto;
import com.patientadmission.master.dto.UserDto;
import com.patientadmission.presentation.queries.PatientAdmissionFinder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.nthdimenzion.crud.ICrud;
import org.nthdimenzion.presentation.annotations.Composer;
import org.nthdimenzion.presentation.infrastructure.AbstractZKModel;
import org.nthdimenzion.security.domain.Credentials;
import org.nthdimenzion.security.domain.IUserLoginRepository;
import com.patientadmission.domain.SecurityQuestion;
import org.nthdimenzion.security.domain.UserLogin;
import org.zkoss.bind.annotation.*;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2/17/2015.
 */
@Composer
@NoArgsConstructor
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class ManageDoctorAccountVm extends AbstractZKModel{
    private DoctorDto doctorDto;
    private UserDto userDto;
    @WireVariable
    private PatientAdmissionFinder patientAdmissionFinder;
    @WireVariable
    private IUserLoginRepository userLoginRepository;
    private List<Map<String, ?>> departments;
    @WireVariable
    private ICrud crudDao;
    List<Map<String,?>> securityQuestions;
    @Wire
    Listbox departmentListbox;
    private Map<String,?> selectedQuestion;
    private String answer;
    private Map modelMap = Maps.newHashMap();
    private Doctor doctor;
    private User user;
    private UserLogin userLogin;
    Set<String> departmentSet;

    @Init
    public void init(){
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW)Component component){
        Selectors.wireComponents(component, this, false);
        doctorDto = new DoctorDto();
        userDto = new UserDto();
        departments = patientAdmissionFinder.findAllDepartments();
        userLogin = userLoginRepository.findUserLoginWithUserName(loggedInUser.getUsername());
        if (userLogin.getRole().equals("DOCTOR")) {
            doctor = userLogin.getDoctor();
            initialize(doctor);
        } else if (userLogin.getRole().equals("ADMIN")){
            user = userLogin.getUser();
            initialize(user);
        }
        securityQuestions = patientAdmissionFinder.findSecurityQuestions();
        modelMap.clear();
        modelMap = createMapWithGivenKeys("oldPassword", "newPassword","confirmPassword","selectedSecurityQuestion","selectedAnswer");
    }

    private void initialize(Doctor doctor) {
        doctorDto.setFirstName(doctor.getFirstName());
        doctorDto.setLastName(doctor.getLastName());
        doctorDto.setEmailId(doctor.getEmailId());
        doctorDto.setPhoneNumber(doctor.getContactNumber());
        doctorDto.setGender(doctor.getGender());
        departmentSet =  doctor.getAdmittingDepartmentName();
    }
    private void initialize(User user) {
        doctorDto.setFirstName(user.getFirstName());
        doctorDto.setLastName(user.getLastName());
        doctorDto.setEmailId(user.getEmailId());
        doctorDto.setPhoneNumber(user.getContactNumber());
        doctorDto.setGender(user.getGender());
        departmentSet =  user.getAdmittingDepartmentName();
    }

    @Command
    @NotifyChange({"doctor","userLogin","selectedQuestion","answer","displayMessages"})
    public void updateSecurityQuestion(){
        try{
            String securityQuestion = selectedQuestion.get("securityQuestion").toString();
            SecurityQuestion security = new SecurityQuestion(securityQuestion,answer);
            userLogin.setSecurityQuestion(security);
            crudDao.save(userLogin);
            Messagebox.show("Successfully Updated", "Notification",
                    new Messagebox.Button[]{Messagebox.Button.OK}, null,
                    new EventListener<Messagebox.ClickEvent>() {
                        public void onEvent(org.zkoss.zul.Messagebox.ClickEvent event) {
                            if ("onOK".equals(event.getName())) {
                                navigation.redirect("manageAccount");
                            }
                        }
                    });
        }
        catch (Exception e){
            displayMessages.displayError("Error occured cannot update...");
            e.printStackTrace();
        }
    }

    @Command
    @NotifyChange({"doctor","doctorDto","displayMessages"})
    public void updateDemographicDetails(){
        try {
            selectedDepartment();
            if (userLogin.getRole().equals("DOCTOR")) {
                doctorDto.setDetailsToEntity(doctor);
                Set<AdmittingDepartment> admittingDepartments = setAdmittingDepartmentToEntity(doctor);
                doctor.setAdmittingDepartments(admittingDepartments);
                crudDao.save(doctor);
            } else if (userLogin.getRole().equals("ADMIN")) {
                doctorDto.setDetailsToEntity(user);
                Set<AdmittingDepartment> admittingDepartments = setAdmittingDepartmentToEntity(user);
                user.setAdmittingDepartments(admittingDepartments);
                crudDao.save(user);
            }
            Messagebox.show("Demographic details updated successfully", "Notification",
                    new Messagebox.Button[]{Messagebox.Button.OK}, null,
                    new EventListener<Messagebox.ClickEvent>() {
                        public void onEvent(Messagebox.ClickEvent event) {
                            if ("onOK".equals(event.getName())) {
                                navigation.redirect("manageAccount");
                            }
                        }
                    });
        }
        catch (Exception e){
            displayMessages.displayError("Error occured cannot update...");
            e.printStackTrace();
        }
        System.out.println(doctorDto);
    }

    @Command
    @NotifyChange({"doctor","displayMessages","modelMap"})
    public void updateAdditionalPassword(){
        displayMessages.clearMessage();
        boolean result = validateCredentialsBeforeUpdating();
        if(result){
            try {
                if (userLogin.getRole().equals("DOCTOR")) {
                    doctor.setAdditionalPassword(modelMap.get("confirmPassword").toString());
                    crudDao.save(doctor);
                } else if (userLogin.getRole().equals("ADMIN")){
                    user.setAdditionalPassword(modelMap.get("confirmPassword").toString());
                    crudDao.save(user);
                }
                Messagebox.show("Password updated successfully", "Notification",
                        new Messagebox.Button[]{Messagebox.Button.OK}, null,
                        new EventListener<Messagebox.ClickEvent>() {
                            public void onEvent(org.zkoss.zul.Messagebox.ClickEvent event) {
                                if ("onOK".equals(event.getName())) {
                                    navigation.redirect("manageAccount");
                                }
                            }
                        });
            }
            catch (Exception e){
                displayMessages.displayError("Error occured cannot update...");
                e.printStackTrace();
            }
        }
    }

    @Command
    @NotifyChange({"doctor","displayMessages","modelMap"})
    public void updateAccountPassword(){
        displayMessages.clearMessage();
        boolean result = validateCredentialsBeforeUpdating();
        if(result){
            try {
                Credentials credentials = userLogin.getCredentials();
                String newPassword = modelMap.get("newPassword").toString();
                credentials.changePassword(newPassword);
                crudDao.save(userLogin);
                Messagebox.show("Password updated successfully", "Notification",
                        new Messagebox.Button[]{Messagebox.Button.OK}, null,
                        new EventListener<Messagebox.ClickEvent>() {
                            public void onEvent(org.zkoss.zul.Messagebox.ClickEvent event) {
                                if ("onOK".equals(event.getName())) {
                                    navigation.redirect("manageAccount");
                                }
                            }
                        });
            }
            catch (Exception e){
                displayMessages.displayError("Error occured cannot update...");
                e.printStackTrace();
            }
        }
    }

    @Command
    public void checkSelectedDepartments(@BindingParam("listItem") Listitem listitem){
        departmentListbox = listitem.getListbox();
        if(departmentSet.contains(listitem.getLabel().toString())){
            listitem.setSelected(true);
        }
    }

    public void selectedDepartment(){
        List<Listitem> listItems = departmentListbox.getItems();
        Set<Map<String, ?>> selectedDepartments = Sets.newHashSet();
        for(Listitem list : listItems) {
            if (list.isSelected()) {
                selectedDepartments.add((Map)list.getValue());
            }
        }
        doctorDto.setSelectedDepartments(selectedDepartments);
        userDto.setSelectedDepartments(selectedDepartments);
    }

    private Set<AdmittingDepartment> setAdmittingDepartmentToEntity(Doctor doctor) {
        Set<Map<String, ?>> departments = doctorDto.getSelectedDepartments();
        Set<AdmittingDepartment> admittingDepartments = Sets.newHashSet();
        for(Map<String, ?> department: departments){
            AdmittingDepartment admittingDepartment = crudDao.find(AdmittingDepartment.class, (Serializable)department.get("id"));
            admittingDepartments.add(admittingDepartment);
        }
        return admittingDepartments;
    }
    private Set<AdmittingDepartment> setAdmittingDepartmentToEntity(User user) {
        Set<Map<String, ?>> departments = userDto.getSelectedDepartments();
        Set<AdmittingDepartment> admittingDepartments = Sets.newHashSet();
        for(Map<String, ?> department: departments){
            AdmittingDepartment admittingDepartment = crudDao.find(AdmittingDepartment.class, (Serializable)department.get("id"));
            admittingDepartments.add(admittingDepartment);
        }
        return admittingDepartments;
    }

    private boolean validateCredentialsBeforeUpdating() {
        Map<String,?> security = (Map<String,?>)modelMap.get("selectedSecurityQuestion");
        //String userSelectedSecurityQuestion = security.get("securityQuestion").toString();
        String oldPassword = modelMap.get("oldPassword").toString();
        String newPassword = modelMap.get("newPassword").toString();
        String confirmPassword = modelMap.get("confirmPassword").toString();
        String loggedInUserCurrentPassword = userLogin.getCurrentPasswordForLoggedInUser();
        String encodedPassword = userLogin.getEncodedPassword(oldPassword);
        /*String userSelectedAnswer = modelMap.get("selectedAnswer").toString();
        String loggedInUserSecurityQuestion = userLogin.getSecurityQuestionForLoggedInUser();
        String loggedInUserSecurityAnswer= userLogin.getSecurityAnswerForLoggedInUser();
*/
        if(!newPassword.equals(confirmPassword)){
            displayMessages.displayError("New Password and Confirm Password Not Matching...");
            return false;
        }
        if (!loggedInUserCurrentPassword.equals(encodedPassword)){
            displayMessages.displayError("Wrong Current Password...");
            return false;
        }
        /*if (!loggedInUserSecurityQuestion.equals(userSelectedSecurityQuestion)){
            displayMessages.displayError("Wrong Security Question...");
            return false;
        }
        if (!loggedInUserSecurityAnswer.equals(userSelectedAnswer)) {
            displayMessages.displayError("Wrong Security Answer...");
            return false;
        }*/
        displayMessages.clearMessage();
        return true;
    }
}
