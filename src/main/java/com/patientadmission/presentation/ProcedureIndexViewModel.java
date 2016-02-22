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
import com.google.common.collect.Sets;
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
import org.zkoss.zul.Button;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.patientadmission.domain.AdmittingDepartment;
import com.patientadmission.domain.Procedure;
import com.patientadmission.presentation.queries.PatientAdmissionFinder;

@Composer
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class ProcedureIndexViewModel extends AbstractZKModel {

    private List<Map<String, ?>> maps = Lists.newArrayList();
    @WireVariable
    private PatientAdmissionFinder patientAdmissionFinder;
    private Integer age;
    private Date admissionDate;
    private Procedure selectedProcedure;
    @WireVariable
    private IUserLoginRepository userLoginRepository;
    private IsADoctor isADoctor;
    @WireVariable
    private ICrud crudDao;
    private Integer count = 0;
    private Integer max = 10;
    private int page = 0;
    @Wire("#pageSize")
    private Intbox pageSize;
    @Wire("#ageListBox")
    private Listbox ageListBox;
    @Wire("#genderListBox")
    private Listbox genderListBox;
    private String gender;
    @Wire("#patientCountLabel")
    private Label patientCountLabel;
    @Wire("#procedureDoneTextBox")
    private Textbox procedureDoneTextBox;
    private Date admissionDateThru;
    private Integer thruAge;
    @Wire("#thruAgeBox")
    private Intbox thruAgeBox;
    @Wire("#totalPage")
    private Intbox totalPage;
    private long length;
    public Intbox getTotalPage() {
        return totalPage;
    }
    public void setTotalPage(Intbox totalPage) {
        this.totalPage = totalPage;
    }
    @Wire("#viewDoctorsListBox")
    private Grid viewDoctorsListBox;
    private String procedureDescription;

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


    /*@SuppressWarnings("unchecked")
    @Command("attachPagingEventListner")
    public void attachPagingEventListner(){
        pagingComp.addEventListener("onPaging", new EventListener() {
            public void onEvent(Event event) {
                page = pagingComp.getActivePage();
                pagingComp.setActivePage(page);
                count = (pagingComp.getActivePage()) * 10;
                search(count, max);

            }
        });

    }*/


    @Command
    @NotifyChange("maps")
    public void search() {
        Date nowDate = UtilDateTime.nowDate();
        Date calulatedDate = null;
        if (ageListBox.getSelectedItem() != null && age != null) {
            if (ageListBox.getSelectedItem().getValue().equals("In Between"))
                nowDate = UtilDateTime.addYearsToDate(UtilDateTime.nowDate(),
                        -thruAge);
            calulatedDate = UtilDateTime.addYearsToDate(UtilDateTime.nowDate(),
                    -age);
        }

        maps = patientAdmissionFinder
                .searchProceduresBy(
                        (admissionDate != null ? UtilDateTime.getDayStart(admissionDate) : null),
                        (admissionDateThru != null ? UtilDateTime.getDayEnd(admissionDateThru) : null),
                        nowDate,
                        genderListBox != null ? ((genderListBox.getSelectedItem() != null ? (String) genderListBox.getSelectedItem().getValue() : null)): null,
                        age,
                        ageListBox != null ? (ageListBox.getSelectedItem() != null ? (String) ageListBox.getSelectedItem().getValue() : null): null,
                        calulatedDate,
                        procedureDoneTextBox.getValue() != null? procedureDoneTextBox.getValue(): null,
                        isADoctor.getDoctorId());
        length =  (Long) patientAdmissionFinder
                .searchProceduresByCount(
                        (admissionDate != null ? UtilDateTime.getDayStart(admissionDate): null),
                        (admissionDateThru != null ? UtilDateTime.getDayEnd(admissionDateThru) : null),
                        nowDate,
                        genderListBox != null ? ((genderListBox.getSelectedItem() != null ? (String) genderListBox.getSelectedItem().getValue() : null)): null,
                        age,
                        ageListBox != null ? (ageListBox.getSelectedItem() != null ? (String) ageListBox.getSelectedItem().getValue() : null): null,
                        calulatedDate,
                        procedureDoneTextBox.getValue() != null? procedureDoneTextBox.getValue(): null,
                        isADoctor.getDoctorId()).get(0).get("CASECOUNT");
        /*pagingComp.setVisible(true);
        pagingComp.setTotalSize((int) length);
        pagingComp.setPageSize(10);*/
        BindUtils.postNotifyChange(null,null, this, "maps");

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

    @Command
    public void addProcedureDone() {
        String multiSelect = "false";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("multiSelect", multiSelect);
        Window window = (Window) Executions.createComponents(
                "/com/patient/cptSearch.zul", null, map);
        window.addEventListener("onDetach", new EventListener<Event>() {

            @Override
            public void onEvent(Event event) throws Exception {
                selectedProcedure = (Procedure) event.getData();
                procedureDoneTextBox.setValue(selectedProcedure
                        .getDescription());
            }
        });
    }

    /*@Command
    @NotifyChange({"selectedProcedure","displayProcedureListDiv","displayProcedureSelectedDiv","keyword","allCpts"})
    public void resetProcedure() {
        procedureDoneTextBox.setValue(null);

        selectedProcedure = null;
        displayProcedureListDiv = true;
        displayProcedureSelectedDiv = false;
        keyword = null;
        allCpts.clear();
        allCpts.addAll(allCptsClone);
    }*/

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

    public Intbox getPageSize() {
        return pageSize;
    }

    public void setPageSize(Intbox pageSize) {
        this.pageSize = pageSize;
    }

    @Command
    public void displayDepartmentName(@BindingParam("label1") Label label,
                                      @BindingParam("map") Map<String, ?> map) {
        if (map.get("ADMITTINGDEPT") != null) {
            AdmittingDepartment admittingDepartment = crudDao.find(
                    AdmittingDepartment.class, (Long) map.get("ADMITTINGDEPT"));
            label.setValue(admittingDepartment.getDepartmentName());
        }
    }

    @Command
    public void calulateAge(@BindingParam("dob") Date dateOfBirth,
                            @BindingParam("label") Label label) {
        if (dateOfBirth != null) {

            String calculatedAge = UtilDateTime.calculateAge(dateOfBirth,
                    UtilDateTime.nowDate());
            label.setValue(calculatedAge);
        }
    }

    /*@Command
    @NotifyChange("allCpts")
    public void searchCpt(){
        allCpts = searchBasedOnKeyword(procedureDescription);
    }

    public List<Map<String,?>> searchBasedOnKeyword(String keyword){
        displayMessages.clearMessage();
        allCpts = patientAdmissionFinder.findAllCpts();
        List<Map<String,?>> result = Lists.newLinkedList();
        if (keyword==null || "".equals(keyword)){
            return allCpts;
        }else{
            for (Map<String,?> cpt : allCpts){
                String description = cpt.get("description") == null ? "" : cpt.get("description").toString().toLowerCase();
                String code = cpt.get("code") == null ? "" : cpt.get("code").toString().toLowerCase();
                if (description.contains(keyword.toLowerCase()) || code.contains(keyword.toLowerCase())){
                    result.add(cpt);
                }
            }
            if(result.size()==0){
                displayMessages.displayError("Enter some valid keyword");
            }
            return result;
        }
    }*/
    /*@Command
    @NotifyChange({"selectedProcedure","displayProcedureListDiv","displayProcedureSelectedDiv"})
    public void clicked(@BindingParam("cpt") Map<String,?> cpt){
        System.out.println(cpt);
        selectedProcedure = crudDao.find(Procedure.class,(Serializable) cpt.get("code"));
        displayProcedureListDiv = false;
        displayProcedureSelectedDiv = true;
    }*/

    /*@Command
    @NotifyChange({"keyword", "allCpts"})
    public void clearKeyword(){
        keyword = "";
        allCpts.clear();
        allCpts.addAll(allCptsClone);
        displayMessages.clearMessage();
    }*/

}
