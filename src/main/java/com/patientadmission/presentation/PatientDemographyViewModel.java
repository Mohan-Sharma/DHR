package com.patientadmission.presentation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableMap;
import org.nthdimenzion.crud.ICrud;
import org.nthdimenzion.object.utils.UtilDateTime;
import org.nthdimenzion.object.utils.UtilValidator;
import org.nthdimenzion.presentation.annotations.Composer;
import org.nthdimenzion.presentation.infrastructure.AbstractZKModel;
import org.nthdimenzion.security.domain.IUserLoginRepository;
import org.nthdimenzion.security.domain.IsADoctor;
import org.nthdimenzion.security.domain.UserLogin;
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

import com.google.common.collect.Sets;
import com.patientadmission.domain.IcdElement;
import com.patientadmission.domain.Procedure;
import com.patientadmission.presentation.queries.PatientAdmissionFinder;

@Composer
@VariableResolver(DelegatingVariableResolver.class)
public class PatientDemographyViewModel extends AbstractZKModel {

    private List<Map<String, ?>> maps = new ArrayList<Map<String, ?>>();

    @WireVariable
    private PatientAdmissionFinder patientAdmissionFinder;

    @WireVariable
    private ICrud crudDao;

    private String mrnNumber;

    private String inPatientNumber;

    private String firstName;

    private String lastName;

    private Integer age;

    private Date admissionDate;

    private Date dischargeDate;

    private IcdElement admissionDiagnosis;

    private IcdElement dischargeDiagnosis;

    private IcdElement finalDiagnosis;

    private Procedure selectedProcedure;

    @Wire
    private Listbox ageListBox;

    @Wire("#genderListBox")
    private Listbox genderListBox;

    @Wire("#Paging")
    private Paging Paging;

    private String gender;

    @Wire("#patientCountLabel")
    private Label patientCountLabel;

    @Wire("#admissionDianosisTextBox")
    private Textbox admissionDianosisTextBox;

    @Wire("#dischargeDiagnosisTextBox")
    private Textbox dischargeDiagnosisTextBox;

    @Wire("#finalDiagnosisTextBox")
    private Textbox finalDiagnosisTextBox;

    @Wire("#procedureDoneTextBox")
    private Textbox procedureDoneTextBox;

    private Set<String> patientMrnNumberCount = Sets.newHashSet();

    private Date admissionDateThru;

    private Date dischargeDateThru;

    private Integer thruAge;

    private IsADoctor isADoctor;

    @WireVariable
    private IUserLoginRepository userLoginRepository;

    @Wire("#thruAgeBox")
    private Intbox thruAgeBox;

    @Init
    @AfterCompose
    public void init(@ContextParam(ContextType.VIEW) Component view) {
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
    @NotifyChange({ "maps", "patientCountLabel", "patientMrnNumberCount" })
    public void search(@BindingParam("minPage") int minPage, @BindingParam("maxPage") int maxPage) {
        Map result = patientAdmissionFinder.findPatientById(ImmutableMap.of("id",1));
        System.out.println("--------------------------------------------------------------");
        System.out.println("result -> " + result);
        System.out.println("--------------------------------------------------------------");
        if (UtilValidator.isNotEmpty(patientMrnNumberCount))
            patientMrnNumberCount.clear();
        Date nowDate = UtilDateTime.nowDate();
        Date calulatedDate = null;
        if (ageListBox.getSelectedItem() != null && age != null) {
            if (ageListBox.getSelectedItem().getValue().equals("In Between"))
                nowDate = UtilDateTime.addYearsToDate(UtilDateTime.nowDate(), -thruAge);
            calulatedDate = UtilDateTime.addYearsToDate(UtilDateTime.nowDate(), -age);
        }


        if (minPage == 0) {
            Long maxCont = (Long) patientAdmissionFinder
                    .searchPatientAdmissionDetailsCount(UtilValidator.isEmpty(inPatientNumber) ? null : inPatientNumber, UtilValidator.isEmpty(mrnNumber) ? null
                                    : mrnNumber, UtilValidator.isEmpty(firstName) ? null : firstName, UtilValidator.isEmpty(lastName) ? null : lastName,
                            admissionDate != null ? UtilDateTime.getDayStart(admissionDate) : null, admissionDateThru != null ? UtilDateTime.getDayEnd(admissionDateThru) : null,
                            dischargeDate != null ? UtilDateTime.getDayStart(dischargeDate) : null, dischargeDateThru != null ? UtilDateTime.getDayEnd(dischargeDateThru) : null,
                            admissionDiagnosis != null ? admissionDiagnosis.getIcdCode() : null, dischargeDiagnosis != null ? dischargeDiagnosis.getIcdCode() : null,
                            finalDiagnosis != null ? finalDiagnosis.getIcdCode() : null, selectedProcedure != null ? selectedProcedure.getIDNum() : null,
                            genderListBox != null ? (genderListBox.getSelectedItem() != null ? (String) genderListBox.getSelectedItem().getValue() : null) : null, age,
                            ageListBox != null ? (ageListBox.getSelectedItem() != null ? (String) ageListBox.getSelectedItem().getValue() : null) : null, (UtilDateTime.getDayStart(nowDate)),
                            calulatedDate,isADoctor.getDoctorId(),((UserLogin)isADoctor).getRole()).get(0).get("CASECOUNT");
            Float nrOfPages = (float) maxCont / 10;
            Paging.setVisible(true);
            Paging.setPageSize(maxPage);
            Paging.setTotalSize(maxCont.intValue());
            patientCountLabel.setValue("Patient Count :" + maxCont.intValue());

        }
        patientCountLabel.setVisible(true);
        maps = patientAdmissionFinder.searchPatientAdmissionDetailsBy(UtilValidator.isEmpty(inPatientNumber) ? null : inPatientNumber, UtilValidator.isEmpty(mrnNumber) ? null
                        : mrnNumber, UtilValidator.isEmpty(firstName) ? null : firstName, UtilValidator.isEmpty(lastName) ? null : lastName,
                admissionDate != null ? UtilDateTime.getDayStart(admissionDate) : null, admissionDateThru != null ? UtilDateTime.getDayEnd(admissionDateThru) : null,
                dischargeDate != null ? UtilDateTime.getDayStart(dischargeDate) : null, dischargeDateThru != null ? UtilDateTime.getDayEnd(dischargeDateThru) : null,
                admissionDiagnosis != null ? admissionDiagnosis.getIcdCode() : null, dischargeDiagnosis != null ? dischargeDiagnosis.getIcdCode() : null,
                finalDiagnosis != null ? finalDiagnosis.getIcdCode() : null, selectedProcedure != null ? selectedProcedure.getIDNum() : null,
                genderListBox != null ? (genderListBox.getSelectedItem() != null ? (String) genderListBox.getSelectedItem().getValue() : null) : null, age,
                ageListBox != null ? (ageListBox.getSelectedItem() != null ? (String) ageListBox.getSelectedItem().getValue() : null) : null, (UtilDateTime.getDayStart(nowDate)),
                calulatedDate, minPage, maxPage,isADoctor.getDoctorId(),((UserLogin)isADoctor).getRole());

        BindUtils.postNotifyChange(null, null, this, "maps");
    }

    @Command
    public void openFileViewer(@BindingParam("content") String inPatientNumber){
        DocumentViewHelper.openDocumentInNewWindow(navigation, inPatientNumber);
    }

    public String getMrnNumber() {
        return mrnNumber;
    }

    public void setMrnNumber(String mrnNumber) {
        this.mrnNumber = mrnNumber;
    }

    public String getInPatientNumber() {
        return inPatientNumber;
    }

    public void setInPatientNumber(String inPatientNumber) {
        this.inPatientNumber = inPatientNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public Date getDischargeDate() {
        return dischargeDate;
    }

    public void setDischargeDate(Date dischargeDate) {
        this.dischargeDate = dischargeDate;
    }

    public IcdElement getAdmissionDiagnosis() {
        return admissionDiagnosis;
    }

    public void setAdmissionDiagnosis(IcdElement admissionDiagnosis) {
        this.admissionDiagnosis = admissionDiagnosis;
    }

    public IcdElement getDischargeDiagnosis() {
        return dischargeDiagnosis;
    }

    public void setDischargeDiagnosis(IcdElement dischargeDiagnosis) {
        this.dischargeDiagnosis = dischargeDiagnosis;
    }

    public IcdElement getFinalDiagnosis() {
        return finalDiagnosis;
    }

    public void setFinalDiagnosis(IcdElement finalDiagnosis) {
        this.finalDiagnosis = finalDiagnosis;
    }

    public Procedure getSelectedProcedure() {
        return selectedProcedure;
    }

    public void setSelectedProcedure(Procedure selectedProcedure) {
        this.selectedProcedure = selectedProcedure;
    }

    public void setMaps(List<Map<String, ?>> maps) {
        this.maps = maps;
    }

    public PatientAdmissionFinder getPatientAdmissionFinder() {
        return patientAdmissionFinder;
    }

    public ICrud getCrudDao() {
        return crudDao;
    }

    public Set<String> getPatientMrnNumberCount() {
        return patientMrnNumberCount;
    }

    public Date getAdmissionDateThru() {
        return admissionDateThru;
    }

    public void setAdmissionDateThru(Date admissionDateThru) {
        this.admissionDateThru = admissionDateThru;
    }

    public Date getDischargeDateThru() {
        return dischargeDateThru;
    }

    public void setDischargeDateThru(Date dischargeDateThru) {
        this.dischargeDateThru = dischargeDateThru;
    }

    public Integer getThruAge() {
        return thruAge;
    }

    public void setThruAge(Integer thruAge) {
        this.thruAge = thruAge;
    }

    @Command
    @NotifyChange("thruAgeBox")
    public void selectCondition(@BindingParam("item") String value) {
        thruAgeBox.setVisible("In Between".equals(value));
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

    @Command
    public void addDischargeDiagnosis() {
        String multiSelect = "false";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("multiSelect", multiSelect);
        Window window = (Window) Executions.createComponents("/com/patient/icdElement.zul", null, map);
        window.addEventListener("onDetach", new EventListener<Event>() {

            @Override
            public void onEvent(Event event) throws Exception {
                dischargeDiagnosis = (IcdElement) event.getData();
                dischargeDiagnosisTextBox.setValue(dischargeDiagnosis.getDescription());
            }
        });
    }

    @Command
    public void addFinalDiagnosis() {
        String multiSelect = "false";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("multiSelect", multiSelect);
        Window window = (Window) Executions.createComponents("/com/patient/icdElement.zul", null, map);
        window.addEventListener("onDetach", new EventListener<Event>() {

            @Override
            public void onEvent(Event event) throws Exception {
                finalDiagnosis = (IcdElement) event.getData();
                finalDiagnosisTextBox.setValue(finalDiagnosis.getDescription());
            }
        });
    }

    @Command
    public void addProcedureDone() {
        String multiSelect = "false";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("multiSelect", multiSelect);
        Window window = (Window) Executions.createComponents("/com/patient/cptSearch.zul", null, map);
        window.addEventListener("onDetach", new EventListener<Event>() {

            @Override
            public void onEvent(Event event) throws Exception {
                selectedProcedure = (Procedure) event.getData();
                procedureDoneTextBox.setValue(selectedProcedure.getDescription());
            }
        });
    }

    @Command
    public void resetAdmissionDiagnosis() {
        admissionDianosisTextBox.setValue(null);
        admissionDiagnosis = null;
    }

    @Command
    public void resetDischargeDiagnosis() {
        dischargeDiagnosisTextBox.setValue(null);
        dischargeDiagnosis = null;
    }

    @Command
    public void resetFinalDiagnosis() {
        finalDiagnosisTextBox.setValue(null);
        finalDiagnosis = null;
    }

    @Command
    public void resetProcedure() {
        procedureDoneTextBox.setValue(null);
        selectedProcedure = null;
    }

    @SuppressWarnings("unchecked")
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
    }

}