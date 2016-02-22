package com.patientadmission.presentation;

import com.google.common.collect.Maps;
import com.patientadmission.domain.AdmittingDepartment;
import com.patientadmission.presentation.queries.PatientAdmissionFinder;
import org.nthdimenzion.crud.ICrud;
import org.nthdimenzion.object.utils.UtilValidator;
import org.nthdimenzion.presentation.annotations.Composer;
import org.nthdimenzion.presentation.infrastructure.AbstractZKModel;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Label;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Composer
public class PatientDetailsViewModel extends AbstractZKModel {

	private static final long serialVersionUID = 1L;

	private String mrnNumber;

	private Map<String, ?> patientDetailsMap;
	
	private Long patientId;
    private String inPatientnumber;

	@WireVariable
	private PatientAdmissionFinder patientAdmissionFinder;
	
    @WireVariable
    private ICrud crudDao;

	@Init
	public void init(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		mrnNumber = (String) Executions.getCurrent().getParameter("mrnNumber");
        inPatientnumber = Executions.getCurrent().getParameter("inPatientNumber");
        if(UtilValidator.isNotEmpty((String)Executions.getCurrent().getParameter("patientId"))){
	        patientId = Long.valueOf((String)Executions.getCurrent().getParameter("patientId")) ;
	        patientDetailsMap = patientAdmissionFinder.getPatientByMRN(mrnNumber);
        }
        else{
            patientDetailsMap = patientAdmissionFinder.getPatientByMRN(mrnNumber);
		    patientId = (Long)patientDetailsMap.get("patientId");
        }
	}

	public Map<String, ?> getPatientDetailsMap() {
		return patientDetailsMap;
	}

	public void setPatientDetailsMap(Map<String, ?> patientDetailsMap) {
		this.patientDetailsMap = patientDetailsMap;
	}
	
	@Command
	public void navigateToPatientAdmission(){
		Map<String, String> map = Maps.newHashMap();
		map.put("mrnNumber", mrnNumber);
		map.put("patientId", patientId.toString());
		navigation.redirect("addPatientAdmission",map);
	}
	
	public List<Map<String, ?>> getAllPatientAdmissions(){
		return patientAdmissionFinder.admissionDetailsPatient(patientId);
	}
	
	@Command
	public void viewAdmissionDetails(@BindingParam("inPatientNumber")String inPatientNumber){
		Map<String, String> map = Maps.newHashMap();
		map.put("mrnNumber", mrnNumber);
		map.put("patientId", patientId.toString());
		map.put("inPatientNumber", inPatientNumber);
		navigation.redirect("addPatientAdmission",map);
	}
	
	@Command
	public void downloadDocument(@BindingParam("content")Map<String, ?> map){
		File file = new File((String)map.get("filePath"));
		String contentType = (String)map.get("contentType");
		try {
			Filedownload.save(file, contentType);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@Command
	public void display(@BindingParam("admDeptId")Long admDeptId,@BindingParam("label")Label label){
		if(admDeptId == null)
			return;
		AdmittingDepartment admittingDepartment = crudDao.find(AdmittingDepartment.class, admDeptId);
		label.setValue(admittingDepartment.getDepartmentName());
	}
    @Command
    public void back(){
        Map<String, String> map = new HashMap<String, String>();
        map.put("mrnNumber", mrnNumber);
        map.put("patientId", patientId.toString());
        map.put("inPatientNumber", inPatientnumber);
        navigation.redirect("registerPatient", map);
    }
	
}
