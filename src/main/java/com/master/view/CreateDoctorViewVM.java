package com.master.view;

import com.google.common.collect.Sets;
import com.patientadmission.domain.AdmittingDepartment;
import com.patientadmission.domain.Doctor;
import com.patientadmission.master.dto.DoctorDto;
import com.patientadmission.presentation.queries.PatientAdmissionFinder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.nthdimenzion.crud.ICrud;
import org.nthdimenzion.presentation.annotations.Composer;
import org.nthdimenzion.presentation.infrastructure.AbstractZKModel;
import org.nthdimenzion.security.application.services.UserService;
import org.nthdimenzion.security.domain.UserLogin;
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
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2/12/2015.
 */
@Composer
@NoArgsConstructor
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class CreateDoctorViewVM extends AbstractZKModel{
    @WireVariable
    private PatientAdmissionFinder patientAdmissionFinder;
    private DoctorDto doctorDto;
    private List<Map<String, ?>> departments;
    private String doctorId;
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
            doctorDto = new DoctorDto();
            System.out.println("patientAdmissionFinder = " + patientAdmissionFinder);
            departments = patientAdmissionFinder.findAllDepartments();
            System.out.println("departments = " + departments);
    }

    @Command
    public void createDoctor(){
        try {
            Doctor doctor = new Doctor();
            doctorDto.setDetailsToEntity(doctor);
            Set<AdmittingDepartment> admittingDepartments = setAdmittingDepartmentToEntity(doctor);
            doctor.setAdmittingDepartments(admittingDepartments);
            crudDao.save(doctor);
            Long doctorId = doctor.getId();
            if(doctorId != null){
                addUser(doctorId);
            }
            Messagebox.show("Doctor successfully created", "Notification",
                    new Messagebox.Button[]{Messagebox.Button.OK}, null,
                    new EventListener<Messagebox.ClickEvent>() {
                        public void onEvent(org.zkoss.zul.Messagebox.ClickEvent event) {
                            if ("onOK".equals(event.getName())) {
                                navigation.redirect("createDoctor");
                            }
                        }
                    });
        }catch (Exception e){
            displayMessages.displayError("Error creating doctor try again...");
        }
        System.out.println(doctorDto);

    }

    public void addUser(Long doctorId) {
        String userName = doctorDto.getUserName();
        userService.createUserLoginForDoctor(userName, doctorId);
        navigation.redirect("viewUsers");
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

    public Validator getFormValidator(){
        return new AbstractValidator() {
            public void validate(ValidationContext ctx) {
                if (doctorDto.getSelectedDepartments().size()==0) {
                    addInvalidMessage(ctx, "selectedDepartments", "select atleast one");
                }
            }
        };
    }
}
