package com.patientadmission.presentation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.nthdimenzion.crud.ICrud;
import org.nthdimenzion.object.utils.UtilDateTime;
import org.nthdimenzion.object.utils.UtilValidator;
import org.nthdimenzion.presentation.annotations.Composer;
import org.nthdimenzion.presentation.infrastructure.AbstractZKModel;
import org.nthdimenzion.security.domain.IUserLoginRepository;
import org.nthdimenzion.security.domain.IsADoctor;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.event.PagingEvent;

import com.patientadmission.domain.AdmittingDepartment;
import com.patientadmission.domain.IcdElement;
import com.patientadmission.domain.Procedure;
import com.patientadmission.presentation.queries.PatientAdmissionFinder;

@Composer
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class DiseaseIndexViewModel extends AbstractZKModel {

    private List<Map<String, ?>> maps = new ArrayList<Map<String, ?>>();
    @WireVariable
    private PatientAdmissionFinder patientAdmissionFinder;
    private Integer age;
    private Date admissionDate;
    private Procedure selectedProcedure;
    private IcdElement admissionDiagnosis;
    @Wire
    private Listbox ageListBox;
    @Wire("#genderListBox")
    private Listbox genderListBox;
    private String gender;
    @Wire("#admissionDianosisTextBox")
    private Textbox admissionDianosisTextBox;
    private Date admissionDateThru;
    private Integer thruAge;
    @Wire("#thruAgeBox")
    private Intbox thruAgeBox;
    @WireVariable
    private ICrud crudDao;
    private String diagnosisType = "Discharge";
    @Wire("#patientCountLabel")
    private Label patientCountLabel;
    @WireVariable
    private IUserLoginRepository userLoginRepository;
    private IsADoctor isADoctor;

    @Init
    public void init(){
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, true);
        isADoctor = userLoginRepository.findUserLoginWithUserName(loggedInUser.getUsername());
    }

    public List<Map<String, ?>> getMaps() {
        return maps;
    }

    @Command
    public void selectGender(@BindingParam("content") String gender) {
        this.gender = gender;
    }

    @Command
    @NotifyChange({"maps", "patientCountLabel", "patientMrnNumberCount"})
    public void search() {

        Date nowDate = UtilDateTime.nowDate();
        Date calulatedDate = null;
        if (ageListBox.getSelectedItem() != null && age != null) {
            if (ageListBox.getSelectedItem().getValue().equals("In Between"))
                nowDate = UtilDateTime.addYearsToDate(UtilDateTime.nowDate(), -thruAge);
            calulatedDate = UtilDateTime.addYearsToDate(UtilDateTime.nowDate(), -age);
        }
        
        if ("All".equals(diagnosisType)) {
            maps = patientAdmissionFinder.searchAllDiseasesBy((admissionDate != null ? UtilDateTime.getDayStart(admissionDate) : null),
                    (admissionDateThru != null ? UtilDateTime.getDayEnd(admissionDateThru) : null), nowDate, admissionDianosisTextBox.getValue() != null ? admissionDianosisTextBox.getValue() : null,
                    genderListBox != null ? ((genderListBox.getSelectedItem() != null ? (String) genderListBox.getSelectedItem().getValue() : null)) : null, age,
                    ageListBox != null ? (ageListBox.getSelectedItem() != null ? (String) ageListBox.getSelectedItem().getValue() : null) : null, calulatedDate,isADoctor.getDoctorId());

            int maxCount = patientAdmissionFinder.searchAllDiseasesBy((admissionDate != null ? UtilDateTime.getDayStart(admissionDate) : null),
                    (admissionDateThru != null ? UtilDateTime.getDayEnd(admissionDateThru) : null), nowDate, admissionDiagnosis != null ? admissionDiagnosis.getIcdCode() : null,
                    genderListBox != null ? ((genderListBox.getSelectedItem() != null ? (String) genderListBox.getSelectedItem().getValue() : null)) : null, age,
                    ageListBox != null ? (ageListBox.getSelectedItem() != null ? (String) ageListBox.getSelectedItem().getValue() : null) : null, calulatedDate, isADoctor.getDoctorId()).size();
            /*Paging.setVisible(true);
            Paging.setPageSize(maxPage);
            Paging.setTotalSize(maxCount);*/
            BindUtils.postNotifyChange(null, null, this, "maps");
            //patientCountLabel.setValue("Total :" + maxCount);

        } else {
            maps = patientAdmissionFinder.searchDiseasesBy((admissionDate != null ? UtilDateTime.getDayStart(admissionDate) : null),
                    (admissionDateThru != null ? UtilDateTime.getDayEnd(admissionDateThru) : null), nowDate, admissionDianosisTextBox.getValue() != null ? admissionDianosisTextBox.getValue() : null,
                    genderListBox != null ? ((genderListBox.getSelectedItem() != null ? (String) genderListBox.getSelectedItem().getValue() : null)) : null, age,
                    ageListBox != null ? (ageListBox.getSelectedItem() != null ? (String) ageListBox.getSelectedItem().getValue() : null) : null, calulatedDate, diagnosisType, isADoctor.getDoctorId());

            Long maxCount =(Long) patientAdmissionFinder.searchDiseasesByCount((admissionDate != null ? UtilDateTime.getDayStart(admissionDate) : null),
                    (admissionDateThru != null ? UtilDateTime.getDayEnd(admissionDateThru) : null), nowDate, admissionDiagnosis != null ? admissionDiagnosis.getIcdCode() : null,
                    genderListBox != null ? ((genderListBox.getSelectedItem() != null ? (String) genderListBox.getSelectedItem().getValue() : null)) : null, age,
                    ageListBox != null ? (ageListBox.getSelectedItem() != null ? (String) ageListBox.getSelectedItem().getValue() : null) : null, calulatedDate, diagnosisType, isADoctor.getDoctorId()).get(0).get("CASECOUNT");
            //maxCount = maps.size();
            /*Paging.setVisible(true);
            Paging.setPageSize(maxPage);
            Paging.setTotalSize(maxCount.intValue());*/
            BindUtils.postNotifyChange(null, null, this, "maps");

        }
       
        //patientCountLabel.setVisible(UtilValidator.isNotEmpty(maps));
    }

    @Command
    public void openFileViewer(@BindingParam("content") String inPatientNumber){
    	DocumentViewHelper.openDocumentInNewWindow(navigation, inPatientNumber);
    }

    @Command
    @NotifyChange("thruAgeBox")
    public void selectCondition(@BindingParam("item") String value) {
        thruAgeBox.setVisible("In Between".equals(value));
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(Date admissionDate) {
        this.admissionDate = admissionDate;
    }

    public Procedure getSelectedProcedure() {
        return selectedProcedure;
    }

    public void setSelectedProcedure(Procedure selectedProcedure) {
        this.selectedProcedure = selectedProcedure;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getAdmissionDateThru() {
        return admissionDateThru;
    }

    public void setAdmissionDateThru(Date admissionDateThru) {
        this.admissionDateThru = admissionDateThru;
    }

    public Integer getThruAge() {
        return thruAge;
    }

    public void setThruAge(Integer thruAge) {
        this.thruAge = thruAge;
    }

    public void setMaps(List<Map<String, ?>> maps) {
        this.maps = maps;
    }

    @Command
    public void addAdmittingDiagnosis() {
        String multiSelect = "false";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("multiSelect", multiSelect);
        Window window = (Window) Executions.createComponents("/com/patient/icdElement.zul", null, map);
        window.addEventListener("onDetach", new EventListener<Event>() {

            @Override
            public void onEvent(Event event) throws Exception {
                admissionDiagnosis = (IcdElement) event.getData();
                admissionDianosisTextBox.setValue(admissionDiagnosis.getDescription());
            }
        });
    }

    public String getDiagnosisType() {
        return diagnosisType;
    }

    public void setDiagnosisType(String diagnosisType) {
        this.diagnosisType = diagnosisType;
    }

    @Command
    public void displayDepartmentName(@BindingParam("label1") Label label, @BindingParam("map") Map<String, ?> map) {
        if (map.get("ADMITTINGDEPT") != null) {
            AdmittingDepartment admittingDepartment = crudDao.find(AdmittingDepartment.class, (Long) map.get("ADMITTINGDEPT"));
            label.setValue(admittingDepartment.getDepartmentName());
        }
    }

    @Command
    public void calulateAge(@BindingParam("dob") Date dateOfBirth, @BindingParam("label") Label label) {
        if (dateOfBirth != null) {
            String calculatedAge = UtilDateTime.calculateAge(dateOfBirth, UtilDateTime.nowDate());
            label.setValue(calculatedAge);
        }
    }

    @Command
    public void populateDiseaseType(@BindingParam("label") Label label) {
        if (!"All".equals(diagnosisType))
            label.setValue(diagnosisType);
        else
            label.setValue("All");
    }


    /*@SuppressWarnings("unchecked")
    @Command("attachPagingEventListner")
    public void attachPagingEventListner() {
        final int PAGE_SIZE = 10;
        Paging.addEventListener("onPaging", new EventListener() {
            public void onEvent(Event event) {
                PagingEvent pe = (PagingEvent) event;
                int pgno = pe.getActivePage();
                int ofs = pgno * PAGE_SIZE;
                search(ofs, PAGE_SIZE);
            }

        });
    }*/

    @Command
    public void clearKeyword(){
        displayMessages.clearMessage();
    }

}
