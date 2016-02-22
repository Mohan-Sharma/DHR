package com.patientadmission.presentation;

import com.google.common.collect.Lists;
import com.master.domain.Enumeration;
import com.patientadmission.domain.Doctor;
import com.patientadmission.presentation.queries.PatientAdmissionFinder;
import lombok.Getter;
import lombok.Setter;
import org.nthdimenzion.crud.ICrud;
import org.nthdimenzion.object.utils.UtilValidator;
import org.nthdimenzion.presentation.annotations.Composer;
import org.nthdimenzion.presentation.infrastructure.AbstractZKModel;
import org.nthdimenzion.presentation.infrastructure.Navigation;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Combobox;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Nthdimenzion
 * Date: 30/1/13
 * Time: 7:27 PM
 */
@Composer
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class DoctorViewModel extends AbstractZKModel {

    @WireVariable
    protected Navigation navigation;

    @WireVariable
    private PatientAdmissionFinder patientAdmissionFinder;
    List<Map<String,?>> allDoctors = Lists.newArrayList();
    List<Map<String,?>> allDoctorsClone = Lists.newArrayList();
    private String keyword;

    @Init
    public void init(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        allDoctors = patientAdmissionFinder.findAllDoctors();
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component component){
        Selectors.wireComponents(component, this, false);
        allDoctorsClone.addAll(allDoctors);
    }
    @Command
    @NotifyChange("allDoctors")
    public void search(){
        allDoctors = search(keyword);
    }

    @Command
    @NotifyChange({"keyword", "allDoctors"})
    public void clearKeyword(){
        keyword = "";
        allDoctors.clear();
        allDoctors.addAll(allDoctorsClone);
    }

    public List<Map<String,?>> search(String keyword){
        displayMessages.clearMessage();
        allDoctors = patientAdmissionFinder.findAllDoctors();
        List<Map<String,?>> result = Lists.newLinkedList();
        if (keyword==null || "".equals(keyword)){
            return allDoctors;
        }else{
            for (Map<String,?> doctor : allDoctors){
                String firstName = doctor.get("firstName") == null ? "" : doctor.get("firstName").toString().toLowerCase();
                String lastName = doctor.get("lastName") == null ? "" : doctor.get("lastName").toString().toLowerCase();
                String id = doctor.get("id") == null ? "" : doctor.get("id").toString().toLowerCase();
                String gender = doctor.get("gender") == null ? "" : doctor.get("gender").toString().toLowerCase();
                String emailId = doctor.get("emailId") == null ? "" : doctor.get("emailId").toString().toLowerCase();
                if (firstName.contains(keyword.toLowerCase()) ||
                        lastName.contains(keyword.toLowerCase()) ||
                        id.contains(keyword.toLowerCase()) ||
                        gender.contains(keyword.toLowerCase()) ||
                        emailId.contains(keyword.toLowerCase())
                        ){
                    result.add(doctor);
                }
            }
            if(result.size()==0){
                displayMessages.displayError("Enter some valid keyword");
            }
            return result;
        }
    }
    public void redirectToCreateDoctor(Map map){
        navigation.redirect("createDoctor", map);
    }

}