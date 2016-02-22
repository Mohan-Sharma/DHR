package com.master.view;

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
import org.nthdimenzion.security.application.services.UserService;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.*;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import java.io.Serializable;
import java.util.*;

/**
 * Created by Administrator on 2/12/2015.
 */
@Composer
@NoArgsConstructor
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class CreateUserViewVM extends AbstractZKModel{
    @WireVariable
    private PatientAdmissionFinder patientAdmissionFinder;
    private UserDto userDto;
    private List<Map<String, ?>> departments;
    private List<String> roles;
    @WireVariable
    private ICrud crudDao;
    @WireVariable
    private UserService userService;
    @Init
    public void init(){
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW)Component component){
        Selectors.wireComponents(component, this, false);
        userDto = new UserDto();
        System.out.println("patientAdmissionFinder = "+patientAdmissionFinder);
        departments = patientAdmissionFinder.findAllDepartments();
        roles = new ArrayList<String>(Arrays.asList("ADMIN", "DATAOP", "USER_CREATER"));
        //System.out.println("departments = "+departments);
    }

    @Command
    public void createUser(){
        try {

            if (userDto.getSelectedRole() == null){
                displayMessages.displayError("Please select role...");
                return;
            }

            User user = new User();
            userDto.setDetailsToEntity(user);
            Set<AdmittingDepartment> admittingDepartments = setAdmittingDepartmentToEntity(user);
            user.setAdmittingDepartments(admittingDepartments);
            crudDao.save(user);
            Long userId = user.getId();
            if(userId != null){
                addUser(userId);
            }
            Messagebox.show("User successfully created", "Notification",
                    new Messagebox.Button[]{Messagebox.Button.OK}, null,
                    new EventListener<Messagebox.ClickEvent>() {
                        public void onEvent(org.zkoss.zul.Messagebox.ClickEvent event) {
                            if ("onOK".equals(event.getName())) {
                                navigation.redirect("createUser");
                            }
                        }
                    });
        }catch (Exception e){
            displayMessages.displayError("Error creating user try again...");
        }
        System.out.println(userDto);

    }

    public void addUser(Long userId) {
        String userName = userDto.getUserName();
        userService.createUserLoginForUser(userName, userId, userDto.getSelectedRole());
        navigation.redirect("viewUsers");
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

    /*public Validator getFormValidator(){
        return new AbstractValidator() {
            public void validate(ValidationContext ctx) {
                if (doctorDto.getSelectedDepartments().size()==0) {
                    addInvalidMessage(ctx, "selectedDepartments", "select atleast one");
                }
            }
        };
    }*/
}
