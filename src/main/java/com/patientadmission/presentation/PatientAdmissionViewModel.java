package com.patientadmission.presentation;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.base.Preconditions;
import com.master.domain.Enumeration;
import com.master.services.IMasterService;
import com.patientadmission.command.CreatePatientAdmissionCommand;
import com.patientadmission.domain.AdmittingDepartment;
import com.patientadmission.domain.Bed;
import com.patientadmission.domain.Doctor;
import com.patientadmission.domain.Ward;
import com.patientadmission.presentation.queries.PatientAdmissionFinder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Criteria;
import org.nthdimenzion.object.utils.UtilDateTime;
import org.nthdimenzion.object.utils.UtilValidator;
import org.nthdimenzion.presentation.annotations.Composer;
import org.nthdimenzion.presentation.infrastructure.AbstractZKModel;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Composer
public class PatientAdmissionViewModel extends AbstractZKModel {

    public static final String ADMITTING_DEPARTMENTS = "admittingDepartments";
    public static final String DEPARTMENT_ID = "departmentId";
    public static final String DEPARTMENT_NAME = "departmentName";
    public static final String ALL_DOCTORS = "allDoctors";
    public static final String ALL_WARDS = "allWards";
    public static final String ALL_BEDS = "allBeds";
    public static final String ALL_CATEGORIES = "allCategories";
    public static final String IN_PATIENT_NUMBER = "inPatientNumber";
    @Getter
    @Setter
    CreatePatientAdmissionCommand createPatientAdmissionCommand = new CreatePatientAdmissionCommand();


    private Ward selectedAdmittingWard;

    private Ward selectedDischargedWard;

    private Bed selectedAdmittingBed;

    private Bed selectedDischargeBed;

    private Doctor selectedAdmittingDoctor;

    private Map selectedAdmittingDepartment;

    private Enumeration selectedCategory;

    @WireVariable
    private PatientAdmissionFinder patientAdmissionFinder;

    @WireVariable
    private IMasterService masterService;

    private String mrnNumber;

    @Getter
    @Setter
    private String mlcNumber;

    private String patientId;

    private String inPatientnumber;

    private boolean viewMode;

    private Map<String, ?> patientAdmissionMap;

    private boolean inPatientBoxDisabled;

    private Map modelMap = Maps.newHashMap();

    public Map getModelMap() {
        return modelMap;
    }

    public void setModelMap(Map modelMap) {
        this.modelMap = modelMap;
    }

    @Init
    public void init(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, true);
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireEventListeners(view, this);
        Selectors.wireComponents(view, this, true);
        modelMap.clear();
        modelMap = createMapWithGivenKeys(ADMITTING_DEPARTMENTS, "lengthOfStay");
        String inPatientNumber = getParam(IN_PATIENT_NUMBER);
        patientId = getParam("patientId");
        inPatientBoxDisabled = UtilValidator.isNotEmpty(inPatientNumber);
        mrnNumber = getParam("mrnNumber");
        inPatientnumber = getParam("inPatientNumber");
        List<Enumeration> categories = masterService.getEnumerationByEnumType(Enumeration.EnumerationType.CATEGORY.toString());

        List<Map<String, ?>> admittingDepts = patientAdmissionFinder.findAllAdmittingDepartments();
        modelMap.put(ADMITTING_DEPARTMENTS, admittingDepts);
        modelMap.put(ALL_DOCTORS, crudDao.getAllDistinctRootEntity(Doctor.class));
        modelMap.put(ALL_WARDS, crudDao.getAll(Ward.class));
        modelMap.put(ALL_BEDS, crudDao.getAll(Bed.class));
        modelMap.put(ALL_CATEGORIES, categories);

        modelMap.put(IN_PATIENT_NUMBER, inPatientNumber);
        findPatientAdmission(inPatientNumber);
    }

    @Command
    public void newAdmittingBed(BindContext context) {
        navigation.openModalWindow("createBed", ImmutableMap.of("targetComp", context.getComponent()));
    }

    @Listen("onReload=#addNewAdmittingBed")
    public void newAdmittingBedListener(Event event) {
        Bed newAdmittingBed = (Bed) event.getData();
        List<Bed> allBeds = (List<Bed>) modelMap.get(ALL_BEDS);
        allBeds.add(newAdmittingBed);
        setSelectedAdmittingBed(newAdmittingBed);
        BindUtils.postNotifyChange(null, null, this, "modelMap");
    }

    @Command
    public void newDischargeBed(BindContext context) {
        navigation.openModalWindow("createBed", ImmutableMap.of("targetComp", context.getComponent()));
    }

    @Listen("onReload=#addNewDischargeBed")
    public void newDischargeBedListener(Event event) {
        Bed newDischargeBed = (Bed) event.getData();
        List<Bed> allBeds = (List<Bed>) modelMap.get(ALL_BEDS);
        allBeds.add(newDischargeBed);
        setSelectedDischargeBed(newDischargeBed);
        BindUtils.postNotifyChange(null, null, this, "modelMap");
    }

    @Command
    public void newAdmittingDepartment(BindContext ctx) {
        navigation.openModalWindow("createAdmittingDept", ImmutableMap.of("targetComp", ctx.getComponent()));
    }

    @Listen("onReload=#addNewDept")
    public void addNewDepartmentListener(Event event) {
        AdmittingDepartment newDepartment = (AdmittingDepartment) event.getData();
        Map newlyAddedDepartment = ImmutableMap.of(DEPARTMENT_ID, newDepartment.getId(), DEPARTMENT_NAME, newDepartment.getDepartmentName());
        setSelectedAdmittingDepartment(newlyAddedDepartment);
        List<Map<String, ?>> admittingDepartments = (List<Map<String, ?>>) modelMap.get(ADMITTING_DEPARTMENTS);
        admittingDepartments.add(newlyAddedDepartment);
        BindUtils.postNotifyChange(null, null, this, "modelMap");
    }


    @Command
    public void newAdmittingWard(BindContext ctx) {
        navigation.openModalWindow("createWard", ImmutableMap.of("targetComp", ctx.getComponent()));
    }


    @Listen("onReload=#addNewAdmittingWard")
    public void newAdmittingWardListener(Event event) {
        Ward newAdmittingWard = (Ward) event.getData();
        List<Ward> allWards = (List<Ward>) modelMap.get(ALL_WARDS);
        allWards.add(newAdmittingWard);
        setSelectedAdmittingWard(newAdmittingWard);
        BindUtils.postNotifyChange(null, null, this, "modelMap");
    }


    @Command
    public void newDischargeWard(BindContext ctx) {
        navigation.openModalWindow("createWard", ImmutableMap.of("targetComp", ctx.getComponent()));
    }

    @Listen("onReload=#addNewDischargeWard")
    public void newDischargeWardListener(Event event) {
        Ward newDischargeWard = (Ward) event.getData();
        List<Ward> allWards = (List<Ward>) modelMap.get(ALL_WARDS);
        allWards.add(newDischargeWard);
        setSelectedDischargedWard(newDischargeWard);
        BindUtils.postNotifyChange(null, null, this, "modelMap");
    }


    @Command
    public void registerDoctor(BindContext ctx) {
        navigation.openModalWindow("registerDoctor", ImmutableMap.of("targetComp", ctx.getComponent()));
    }

    @Listen("onReload=#addDoctor")
    public void registerDoctorListener(Event event) {
        Doctor newRegisteredDoctor = (Doctor) event.getData();
        List<Doctor> allDoctors = (List<Doctor>) modelMap.get(ALL_DOCTORS);
        allDoctors.add(newRegisteredDoctor);
        setSelectedAdmittingDoctor(newRegisteredDoctor);
        BindUtils.postNotifyChange(null, null, this, "modelMap");
    }


    @Command
    @NotifyChange("modelMap")
    public int calculateLengthOfStay(@BindingParam("admissionDateAndTime") Date admissionDateAndTime,
                                     @BindingParam("dischargeDateAndTime") Date dischargeDateAndTime) {

        if (admissionDateAndTime == null || dischargeDateAndTime == null) {
            return 0;
        }

        if (isInvalidAdmissionAndDischargeDates(admissionDateAndTime, dischargeDateAndTime))
            return 0;
        int lengthOfStay = UtilDateTime.getIntervalInDays(admissionDateAndTime, dischargeDateAndTime);
        modelMap.put("lengthOfStay", lengthOfStay);
        return lengthOfStay;
    }

    private boolean isInvalidAdmissionAndDischargeDates(Date admissionDateAndTime, Date dischargeDateAndTime) {

        if (admissionDateAndTime == null || dischargeDateAndTime == null) {
            return false;
        }

        if (admissionDateAndTime.after(dischargeDateAndTime)) {
            displayMessages.displayError("Admission Date should be before discharge Date");
            return true;
        }
        return false;
    }


    @Command
    public void continueToClinicalDetails() {
        boolean result = savePatientAdmissionDetails();
        if (result)
            continueToViewClinicalDetails();

    }

    @Command
    public void continueToViewClinicalDetails() {
        navigation.redirect("addPatientClinicalDetails", ImmutableMap.of("mrnNumber", mrnNumber, "patientId",
                patientId, IN_PATIENT_NUMBER, (String) modelMap.get(IN_PATIENT_NUMBER)));
    }

    public boolean isViewMode() {
        return viewMode;
    }

    @Command
    @NotifyChange("patientAdmissionCommand")
    public void attachDocument(BindContext ctx)
            throws IOException {
        UploadEvent event = (UploadEvent) ctx.getTriggerEvent();
        org.zkoss.util.media.Media media = event.getMedia();
        File file = new File(media.getName());
        if (!file.exists())
            file.createNewFile();
        BufferedOutputStream bufferedOutputStream = null;
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
        bufferedOutputStream.write(media.getByteData());
        bufferedOutputStream.flush();
        bufferedOutputStream.close();
        modelMap.putAll(ImmutableMap.of("fileName", media.getName(), "filePath", file.getAbsolutePath(), "contentType", media.getContentType()));
        displayMessages.displaySuccess("Document Uploaded, Click Save");
    }

    @Command
    public void saveAdmissionDetails() {
        if (savePatientAdmissionDetails())
            displayMessages.displaySuccess();
    }

    private boolean savePatientAdmissionDetails() {
        createPatientAdmissionCommand = populate(modelMap, createPatientAdmissionCommand);
        createPatientAdmissionCommand.mrnNumber = mrnNumber;
        System.out.println(createPatientAdmissionCommand);
        Object result = sendCommand(createPatientAdmissionCommand);
        if(isSuccess(result)){
            findPatientAdmission(createPatientAdmissionCommand.inPatientNumber);
            return true;
        }
        return false;
    }

    @Command
    public void back() {
        Preconditions.checkNotNull(patientId);
        Map<String, String> map = new HashMap<String, String>();
        map.put("mrnNumber", mrnNumber);
        map.put("patientId", patientId);
        map.put("inPatientNumber", inPatientnumber);
        navigation.redirect("patientBanner", map);
    }

    @Command
    @NotifyChange({"modelMap"})
    public void findPatientAdmission(@BindingParam("inPatientNumber") String inPatientNumber) {
        patientAdmissionMap = patientAdmissionFinder.getPatientAdmissionDetailsByInPatientNumber(inPatientNumber);

        if (UtilValidator.isEmpty(patientAdmissionMap)) {
            modelMap.put(IN_PATIENT_NUMBER, inPatientNumber);
            return;
        }

        Date admissionDateAndTime = (Date) patientAdmissionMap.get("admissionDateAndTime");
        Date dischargedDateAndTime = (Date) patientAdmissionMap.get("dischargedDateAndTime");

        calculateLengthOfStay(admissionDateAndTime, dischargedDateAndTime);

        if (patientAdmissionMap.get(DEPARTMENT_ID) != null) {
            setSelectedAdmittingDepartment(ImmutableMap.of(DEPARTMENT_ID, patientAdmissionMap.get(DEPARTMENT_ID),
                    DEPARTMENT_NAME, patientAdmissionMap.get(DEPARTMENT_NAME)));
        }

        setSelectedAdmittingWard(crudDao.<Ward>find(Ward.class, (Serializable) patientAdmissionMap.get("admittingWardId")));
        setSelectedDischargedWard(crudDao.<Ward>find(Ward.class, (Serializable) patientAdmissionMap.get("dischargeWardId")));

        setSelectedAdmittingBed(crudDao.<Bed>find(Bed.class, (Serializable) patientAdmissionMap.get("admittingBedId")));
        setSelectedDischargeBed(crudDao.<Bed>find(Bed.class, (Serializable) patientAdmissionMap.get("dischargeBedId")));

        setSelectedCategory(crudDao.<Enumeration>find(Enumeration.class, (Long) patientAdmissionMap.get("categoryId")));

        setSelectedAdmittingDoctor(crudDao.<Doctor>find(Doctor.class, (Long) patientAdmissionMap.get("admittingDoctorId")));

        modelMap.putAll(patientAdmissionMap);

    }

    public boolean isInPatientBoxDisabled() {
        return inPatientBoxDisabled;
    }

    public Enumeration getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(Enumeration selectedCategory) {
        this.selectedCategory = selectedCategory;
        if (selectedCategory != null)
            createPatientAdmissionCommand.categoryCode = selectedCategory.getEnumCode();
    }

    public Map getSelectedAdmittingDepartment() {
        return selectedAdmittingDepartment;
    }

    public void setSelectedAdmittingDepartment(Map selectedAdmittingDepartment) {
        this.selectedAdmittingDepartment = selectedAdmittingDepartment;
        createPatientAdmissionCommand.admittingDepartmentId = (Long) selectedAdmittingDepartment.get(DEPARTMENT_ID);
    }

    public Doctor getSelectedAdmittingDoctor() {
        return selectedAdmittingDoctor;
    }

    public void setSelectedAdmittingDoctor(Doctor selectedAdmittingDoctor) {
        modelMap.remove("admittingDoctorId");
        this.selectedAdmittingDoctor = selectedAdmittingDoctor;
        if (selectedAdmittingDoctor != null)
            createPatientAdmissionCommand.admittingDoctorId = selectedAdmittingDoctor.getId();
    }

    public Ward getSelectedDischargedWard() {
        return selectedDischargedWard;
    }

    public void setSelectedDischargedWard(Ward selectedDischargedWard) {
        modelMap.remove("dischargeWardId");
        this.selectedDischargedWard = selectedDischargedWard;
        if (selectedDischargedWard != null)
            this.createPatientAdmissionCommand.dischargeWardId = selectedDischargedWard.getId();
    }

    public Ward getSelectedAdmittingWard() {
        return selectedAdmittingWard;
    }

    public void setSelectedAdmittingWard(Ward selectedAdmittingWard) {
        modelMap.remove("admittingWardId");
        this.selectedAdmittingWard = selectedAdmittingWard;
        if (selectedAdmittingWard != null)
            createPatientAdmissionCommand.admittingWardId = selectedAdmittingWard.getId();
    }

    public Bed getSelectedAdmittingBed() {
        return selectedAdmittingBed;
    }

    public void setSelectedAdmittingBed(Bed selectedAdmittingBed) {
        modelMap.remove("admittingBedId");
        this.selectedAdmittingBed = selectedAdmittingBed;
        if (selectedAdmittingBed != null)
            createPatientAdmissionCommand.admittingBedId = selectedAdmittingBed.getId();
    }

    public Bed getSelectedDischargeBed() {
        return selectedDischargeBed;
    }

    public void setSelectedDischargeBed(Bed selectedDischargeBed) {
        modelMap.remove("dischargeWardId");
        this.selectedDischargeBed = selectedDischargeBed;
        if (selectedDischargeBed != null)
            createPatientAdmissionCommand.dischargeBedId = selectedDischargeBed.getId();
    }

}