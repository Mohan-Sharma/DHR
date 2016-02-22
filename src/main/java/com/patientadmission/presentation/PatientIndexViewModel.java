package com.patientadmission.presentation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.event.PagingEvent;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.patientadmission.domain.Doctor;
import com.patientadmission.presentation.queries.PatientAdmissionFinder;

@Composer
@VariableResolver(DelegatingVariableResolver.class)
public class PatientIndexViewModel extends AbstractZKModel {
	

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

    public PatientAdmissionFinder getPatientAdmissionFinder() {
        return patientAdmissionFinder;
    }

    public ICrud getCrudDao() {
        return crudDao;
    }

    public void setCrudDao(ICrud crudDao) {
        this.crudDao = crudDao;
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

    public Listbox getAgeListBox() {
        return ageListBox;
    }

    public void setAgeListBox(Listbox ageListBox) {
        this.ageListBox = ageListBox;
    }

    public Listbox getGenderListBox() {
        return genderListBox;
    }

    public void setGenderListBox(Listbox genderListBox) {
        this.genderListBox = genderListBox;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Label getPatientCountLabel() {
        return patientCountLabel;
    }

    public void setPatientCountLabel(Label patientCountLabel) {
        this.patientCountLabel = patientCountLabel;
    }

    public Set<String> getPatientMrnNumberCount() {
        return patientMrnNumberCount;
    }

    public void setPatientMrnNumberCount(Set<String> patientMrnNumberCount) {
        this.patientMrnNumberCount = patientMrnNumberCount;
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

    public Intbox getThruAgeBox() {
        return thruAgeBox;
    }

    public void setThruAgeBox(Intbox thruAgeBox) {
        this.thruAgeBox = thruAgeBox;
    }

    public void setMaps(List<Map<String, ?>> maps) {
        this.maps = maps;
    }

    private Date admissionDate;

    private Date dischargeDate;

    @Wire
    private Listbox ageListBox;

    @Wire("#simplePaging")
    private Paging simplePaging;

    @Wire("#genderListBox")
    private Listbox genderListBox;

    private String gender;

    @Wire("#patientCountLabel")
    private Label patientCountLabel;

    private Set<String> patientMrnNumberCount = Sets.newHashSet();

    private Date admissionDateThru;

    private Date dischargeDateThru;

    private Integer thruAge;

    @Wire("#thruAgeBox")
    private Intbox thruAgeBox;

    private List<Map<String, Object>> list = Lists.newArrayList();

    @WireVariable
    private IUserLoginRepository userLoginRepository;

    private IsADoctor isADoctor;


    public void setUserRepository(IUserLoginRepository userRepository) {
        this.userLoginRepository = userRepository;
    }

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
    @NotifyChange({"maps", "list"})
    public void search(@BindingParam("minPage") int minPage, @BindingParam("maxPage") int maxPage) {
        list.clear();
        Date nowDate = UtilDateTime.nowDate();
        Date calulatedDate = null;
        if (ageListBox.getSelectedItem() != null && age != null) {
            if (ageListBox.getSelectedItem().getValue().equals("In Between"))
                nowDate = UtilDateTime.addYearsToDate(UtilDateTime.nowDate(), -thruAge);
            calulatedDate = UtilDateTime.addYearsToDate(UtilDateTime.nowDate(), -age);
        }

        Long maxCont = (Long) patientAdmissionFinder
                .searchPatientAdmissionDetailsCount(UtilValidator.isEmpty(inPatientNumber) ? null : inPatientNumber, UtilValidator.isEmpty(mrnNumber) ? null
                        : mrnNumber, UtilValidator.isEmpty(firstName) ? null : firstName, UtilValidator.isEmpty(lastName) ? null : lastName,
                        admissionDate != null ? UtilDateTime.getDayStart(admissionDate) : null,
                        admissionDateThru != null ? UtilDateTime.getDayEnd(admissionDateThru) : null, dischargeDate != null ? UtilDateTime.getDayStart(dischargeDate) : null,
                        dischargeDateThru != null ? UtilDateTime.getDayEnd(dischargeDateThru) : null, null, null, null, null,
                        genderListBox != null ? (genderListBox.getSelectedItem() != null ? (String) genderListBox.getSelectedItem().getValue() : null) : null, age,
                        ageListBox != null ? (ageListBox.getSelectedItem() != null ? (String) ageListBox.getSelectedItem().getValue() : null) : null,
                        (UtilDateTime.getDayStart(nowDate)), calulatedDate,isADoctor.getDoctorId(), ((UserLogin)isADoctor).getRole()).get(0).get("CASECOUNT");
        Float nrOfPages = (float) maxCont / 10;
        simplePaging.setVisible(true);
        simplePaging.setPageSize(maxPage);
        simplePaging.setTotalSize(maxCont.intValue());
        maps = patientAdmissionFinder.searchPatientAdmissionDetailsBy(inPatientNumber, mrnNumber, firstName, lastName,
                admissionDate != null ? UtilDateTime.getDayStart(admissionDate) : null, admissionDateThru != null ? UtilDateTime.getDayEnd(admissionDateThru) : null,
                dischargeDate != null ? UtilDateTime.getDayStart(dischargeDate) : null, dischargeDateThru != null ? UtilDateTime.getDayEnd(dischargeDateThru) : null, null, null,
                null, null, genderListBox != null ? (genderListBox.getSelectedItem() != null ? (String) genderListBox.getSelectedItem().getValue() : null) : null, age,
                ageListBox != null ? (ageListBox.getSelectedItem() != null ? (String) ageListBox.getSelectedItem().getValue() : null) : null, (UtilDateTime.getDayStart(nowDate)),
                calulatedDate, minPage, maxPage,isADoctor.getDoctorId(), ((UserLogin)isADoctor).getRole());

        for (int i = 0; i < maps.size(); i++) {
            boolean mrnPresent = false;
            String mrnNumber = (String) maps.get(i).get("MEDICAL_RECORD_NUMBER");
            if (UtilValidator.isEmpty(list)) {
                Map<String, Object> map2 = Maps.newHashMap();
                map2.putAll(maps.get(i));
                List<String> inPatientNumbers = Lists.newArrayList();
                List<String> caseSheets = Lists.newArrayList();
                caseSheets.add((String) maps.get(i).get("FILE_NAME"));
                inPatientNumbers.add((String) maps.get(i).get("INPATIENT_NUMBER"));
                map2.put("IN_PATIENT_LIST", inPatientNumbers);
                map2.put("CASE_SHEET_LIST", caseSheets);
                list.add(map2);
                continue;
            }
            for (int j = 0; j < list.size(); j++) {
                if ((mrnNumber).equals(list.get(j).get("MEDICAL_RECORD_NUMBER"))) {
                    List<String> inPatients = (List<String>) list.get(j).get("IN_PATIENT_LIST");
                    inPatients.add((String) maps.get(i).get("INPATIENT_NUMBER"));
                    List<String> caseSheets = (List<String>) list.get(j).get("CASE_SHEET_LIST");
                    caseSheets.add((String) maps.get(i).get("FILE_NAME"));
                    list.get(j).put("IN_PATIENT_LIST", inPatients);
                    list.get(j).put("CASE_SHEET_LIST", caseSheets);
                }
            }
            for (int k = 0; k < list.size(); k++)
                mrnPresent = mrnNumber.equals((String) list.get(k).get("MEDICAL_RECORD_NUMBER"));
            if (!mrnPresent) {
                Map<String, Object> map2 = Maps.newHashMap();
                map2.putAll(maps.get(i));
                List<String> inPatientNumbers = Lists.newArrayList();
                inPatientNumbers.add((String) maps.get(i).get("INPATIENT_NUMBER"));
                List<String> caseSheets = Lists.newArrayList();
                caseSheets.add((String) maps.get(i).get("FILE_NAME"));
                map2.put("IN_PATIENT_LIST", inPatientNumbers);
                map2.put("CASE_SHEET_LIST", caseSheets);
                list.add(map2);
            }
        }
        BindUtils.postNotifyChange(null, null, this, "list");
    }

    @Command
    public void calulateAge(@BindingParam("dob") Date dateOfBirth, @BindingParam("label") Label label) {
        if (dateOfBirth != null) {
            String calculatedAge = UtilDateTime.calculateAge(dateOfBirth, UtilDateTime.nowDate());
            label.setValue(calculatedAge);
        }
    }

    @Command
    public void downloadDocument() throws IOException {
        Map<String, ?> map = patientAdmissionFinder.findFileNameBy(getInPatientNumber());
        String filePath = (String) map.get("FILE_PATH");
        String contentType = (String) map.get("CONTENT_TYPE");
        if (UtilValidator.isEmpty(filePath))
            return;
        Doctor doctor = crudDao.find(Doctor.class,isADoctor.getDoctorId());
        
        try {
            DocumentViewHelper.downloadPdf(filePath,contentType,doctor.getAdditionalPassword());
           // DocumentViewHelper.openDocumentInNewWindow(navigation,inPatientNumber);

        } catch (Exception e) {
            logger.error("Could not download case sheet for " + inPatientNumber,e);
            displayMessages.displayError("Cannot download case sheet contact administrator");
        }
    }
    
    @Command
    public void openFileViewer(@BindingParam("content") String inPatientNumber){
    	DocumentViewHelper.openDocumentInNewWindow(navigation, inPatientNumber);
    	//navigation.redirect("fileViewer", ImmutableMap.of("inPatientNumber", inPatientNumber));
    }
    
    @Command
    @NotifyChange("thruAgeBox")
    public void selectCondition(@BindingParam("item") String value) {
        thruAgeBox.setVisible("In Between".equals(value));
    }

    public List<Map<String, Object>> getList() {
        return list;
    }

    public void setList(List<Map<String, Object>> list) {
        this.list = list;
    }

    @Command
    public void viewPatientInfo(@BindingParam("mrnNumber") String mrnNumber) {
        Map<String, String> map = Maps.newHashMap();
        map.put("mrnNumber", mrnNumber);
        navigation.redirect("registerPatient", map);
    }

    @SuppressWarnings("unchecked")
    @Command("attachPagingEventListner")
    public void attachPagingEventListner() {
        final int PAGE_SIZE = 10;
        simplePaging.addEventListener("onPaging", new EventListener() {
            public void onEvent(Event event) {
                PagingEvent pe = (PagingEvent) event;
                int pgno = pe.getActivePage();
                int ofs = pgno * PAGE_SIZE;
                search(ofs, PAGE_SIZE);
            }

        });
    }

}
