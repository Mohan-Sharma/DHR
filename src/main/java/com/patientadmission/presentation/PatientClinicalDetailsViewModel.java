package com.patientadmission.presentation;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.master.domain.Enumeration;
import com.master.services.IMasterService;
import com.patientadmission.command.*;
import com.patientadmission.domain.ClinicalDetails;
import com.patientadmission.domain.IcdElement;
import com.patientadmission.domain.Procedure;
import com.patientadmission.presentation.queries.PatientAdmissionFinder;
import org.nthdimenzion.crud.ICrud;
import org.nthdimenzion.object.utils.UtilValidator;
import org.nthdimenzion.presentation.annotations.Composer;
import org.nthdimenzion.presentation.infrastructure.AbstractZKModel;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.databind.BindingListModelList;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Composer
public class PatientClinicalDetailsViewModel extends AbstractZKModel {

	private RecordPatientAdmissionCommand patientAdmissionCommand;

	private List<Enumeration> conditionAtDischarge = Lists.newArrayList();

	private List<Enumeration> dischargeDestination = Lists.newArrayList();

	private List<Enumeration> selectedIndications = Lists.newArrayList();

	private List<Enumeration> referredFrom = Lists.newArrayList();

	private List<Enumeration> hospitalUnits = Lists.newArrayList();

	private List<Enumeration> clinicalUnits = Lists.newArrayList();

	private List<IcdElement> selectedAdmittingDiagnosis = Lists.newArrayList();

	private List<IcdElement> selectedDischargeDiagnosis = Lists.newArrayList();

	private List<IcdElement> selectedFinalDiagnosis = Lists.newArrayList();

	private List<Procedure> selectedProceduresDone = Lists.newArrayList();

	private List<String> selectedAdmittingDiagnosisIds = Lists.newArrayList();

	private List<String> selectedDischargeDiagnosisIds = Lists.newArrayList();

	private List<String> selectedFinalDiagnosisIds = Lists.newArrayList();

	private List<Long> selectedProceduresDoneIds = Lists.newArrayList();

	private List<String> selectedIndicationsForAdmissionsEnumCode = Lists
			.newArrayList();

	@WireVariable
	private PatientAdmissionFinder patientAdmissionFinder;

	@WireVariable
	private ICrud crudDao;

	@WireVariable
	private IMasterService masterService;

	private Enumeration selectedReferredFrom;

	private Enumeration selectedInternalHospitalUnit;

	private Enumeration selectedInternalClinic;

	private Enumeration selectedConditionAtDischarge;

	private Enumeration selectedDischargeDestination;

	private String mrnNumber;

	private String patientId;

	private String inPatientnumber;

	private boolean viewMode;

	private Map<String, String> clinicalDetailsMap = Maps.newHashMap();

	private String patientAdmissionNotSaved;

	@Init
	public void init(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		patientAdmissionCommand = new RecordPatientAdmissionCommand();
		if(UtilValidator.isNotEmpty(Executions.getCurrent().getParameter("patientId")))
		patientId = Executions.getCurrent().getParameter("patientId");
		mrnNumber = Executions.getCurrent().getParameter("mrnNumber");
		inPatientnumber = Executions.getCurrent().getParameter("inPatientNumber");
		patientAdmissionNotSaved = Executions.getCurrent().getParameter("next");
		hospitalUnits = masterService
		.getEnumerationByEnumType(Enumeration.EnumerationType.INTERNAL_HOSPITAL_UNIT
					.toString());
		clinicalUnits = masterService
		.getEnumerationByEnumType(Enumeration.EnumerationType.INTERNAL_CLINIC
				.toString());
		if(!"true".equals(Executions.getCurrent().getParameter("next")) ||UtilValidator.isNotEmpty(Executions.getCurrent().getParameter("admissionId")) ){
			ClinicalDetails clinicalDetails = masterService.fetchClinicalDetailsByInPatientNumber(inPatientnumber);
			List<Map<String, ?>> indicationsForAdmissionMap = patientAdmissionFinder.findIndicationsForAdmissionByInpatientNumber(inPatientnumber);
			List<Map<String, ?>> selectedAdmissionDiagnosisMap = patientAdmissionFinder.findAdmittingDiagnosisByInpatientNumber(inPatientnumber);
			List<Map<String,?>> selectedDischargeDiagnosisMap = patientAdmissionFinder.findDischargeDiagnosisByInpatientNumber(inPatientnumber);
			List<Map<String, ?>> selectedFinalDiagnosisMap = patientAdmissionFinder.findFinalDiagnosisByInpatientNumber(inPatientnumber);
			List<Map<String,?>> selectedProceduresDoneMap = patientAdmissionFinder.findProceduresPerformedByInpatientNumber(inPatientnumber);
			for(Map<String, ?> map : indicationsForAdmissionMap){
				Enumeration enumeration = (Enumeration)crudDao.find(Enumeration.class, (Long) map.get("INDICATIONFORADMISSION"));
				selectedIndications.add(enumeration);
				selectedIndicationsForAdmissionsEnumCode.add(enumeration.getEnumCode());
			}
			patientAdmissionCommand.patientClinicalDetails.indicationForAdmissionIds.addAll(selectedIndicationsForAdmissionsEnumCode);
			for(Map<String, ?> map : selectedAdmissionDiagnosisMap){
				IcdElement icdElement = (IcdElement)crudDao.find(IcdElement.class, (String) map.get("ADMITTINGDIAGNOSIS"));
				selectedAdmittingDiagnosis.add(icdElement);
				selectedAdmittingDiagnosisIds.add((String) map.get("ADMITTINGDIAGNOSIS"));
			}
			patientAdmissionCommand.patientClinicalDetails.selectedAdmittingDiagnosisIds.addAll(selectedAdmittingDiagnosisIds);
			for(Map<String, ?> map : selectedDischargeDiagnosisMap){
				IcdElement icdElement = (IcdElement)crudDao.find(IcdElement.class, (String) map.get("DISCHARGEDIAGNOSIS"));
				selectedDischargeDiagnosis.add(icdElement);
				selectedDischargeDiagnosisIds.add((String) map.get("DISCHARGEDIAGNOSIS"));
			}
			patientAdmissionCommand.patientClinicalDetails.selectedDischargeDiagnosisIds.addAll(selectedDischargeDiagnosisIds);
			for(Map<String, ?> map : selectedFinalDiagnosisMap){
				IcdElement icdElement = (IcdElement)crudDao.find(IcdElement.class, (String) map.get("FINALDIAGNOSIS"));
				selectedFinalDiagnosis.add(icdElement);
				selectedFinalDiagnosisIds.add((String) map.get("FINALDIAGNOSIS"));
			}
			patientAdmissionCommand.patientClinicalDetails.selectedFinalDiagnosisIds.addAll(selectedFinalDiagnosisIds);
			for(Map<String, ?> map : selectedProceduresDoneMap){
				Procedure procedure = (Procedure)crudDao.find(Procedure.class, (Long) map.get("PROCEDURESPERFORMED"));
				selectedProceduresDone.add(procedure);
				selectedProceduresDoneIds.add((Long) map.get("PROCEDURESPERFORMED"));
			}
			patientAdmissionCommand.patientClinicalDetails.selectedProceduresDoneIds.addAll(selectedProceduresDoneIds);

			if(clinicalDetails.getReferredFrom() != null){
			selectedReferredFrom = clinicalDetails.getReferredFrom();
			patientAdmissionCommand.patientClinicalDetails.referredFromId = selectedReferredFrom.getEnumCode();
			}
			if(clinicalDetails.getHospitalUnit() != null){
				selectedInternalHospitalUnit = clinicalDetails.getHospitalUnit();
				patientAdmissionCommand.patientClinicalDetails.hospitalUnitId = selectedInternalHospitalUnit.getEnumCode();
			}
			if(clinicalDetails.getClinicalUnit() != null){
				selectedInternalClinic = clinicalDetails.getClinicalUnit();
				patientAdmissionCommand.patientClinicalDetails.clinicalUnitId = selectedInternalClinic.getEnumCode();
			}
			if(patientAdmissionCommand != null && patientAdmissionCommand.patientClinicalDetails !=null)
				patientAdmissionCommand.patientClinicalDetails.outsideReferral = clinicalDetails.getOutsideReferral();
			patientAdmissionCommand.patientClinicalDetails.referred = clinicalDetails.getReferred();
			selectedConditionAtDischarge = clinicalDetails.getConditionAtDischarge();
			if(selectedConditionAtDischarge != null)
				patientAdmissionCommand.patientClinicalDetails.conditionAtDischargeId = selectedConditionAtDischarge.getEnumCode();
			selectedDischargeDestination = clinicalDetails.getDischargeDestination();
			if(selectedDischargeDestination != null)
				patientAdmissionCommand.patientClinicalDetails.dischargeDestinationId = selectedDischargeDestination.getEnumCode();
		}
			conditionAtDischarge = masterService
					.getEnumerationByEnumType(Enumeration.EnumerationType.CONDITION_AT_DISCHARGE
							.toString());
			dischargeDestination = masterService
					.getEnumerationByEnumType(Enumeration.EnumerationType.DISCHARGE_DESTINATION
							.toString());
			referredFrom = masterService
					.getEnumerationByEnumType(Enumeration.EnumerationType.REFERRED_FROM
							.toString());
		}

	public List<Enumeration> getClinicalUnits() {
		return clinicalUnits;
	}

	public void setClinicalUnits(List<Enumeration> clinicalUnits) {
		this.clinicalUnits = clinicalUnits;
	}

	public Enumeration getSelectedReferredFrom() {
		return selectedReferredFrom;
	}

	public void setSelectedReferredFrom(Enumeration selectedReferredFrom) {
		this.selectedReferredFrom = selectedReferredFrom;
	}

	@Command
	@NotifyChange( { "hospitalUnits", "clinicalUnits" })
	public void selectReferredFrom(@BindingParam("ihu")Combobox combobox,@BindingParam("ic")Combobox combobox2,@BindingParam("outside")Textbox textbox) {
		hospitalUnits.clear();
		clinicalUnits.clear();
		patientAdmissionCommand.patientClinicalDetails.referredFromId = selectedReferredFrom
				.getEnumCode();
		if ("IHU".equals(selectedReferredFrom.getEnumCode().toString())) {
			hospitalUnits = masterService
					.getEnumerationByEnumType(Enumeration.EnumerationType.INTERNAL_HOSPITAL_UNIT
							.toString());
			clinicalUnits.clear();
			combobox2.setDisabled(true);
			textbox.setValue("");
			textbox.setDisabled(true);
			combobox.setDisabled(false);
		} else if ("IC".equals(selectedReferredFrom.getEnumCode().toString())) {
			clinicalUnits = masterService
					.getEnumerationByEnumType(Enumeration.EnumerationType.INTERNAL_CLINIC
							.toString());
			hospitalUnits.clear();
			combobox.setDisabled(true);
			combobox2.setDisabled(false);
		}
		else if("OUTSIDE".equals(selectedReferredFrom.getEnumCode().toString())){
			clinicalUnits.clear();
			hospitalUnits.clear();
			combobox.setDisabled(true);
			combobox2.setDisabled(true);
			textbox.setDisabled(false);
		}
	}

	@Command
	public void selectInternalHospitalUnit() {
		patientAdmissionCommand.patientClinicalDetails.hospitalUnitId = selectedInternalHospitalUnit
				.getEnumCode();
	}

	@Command
	public void selectInternalClinicUnit() {
		patientAdmissionCommand.patientClinicalDetails.clinicalUnitId = selectedInternalClinic
				.getEnumCode();
	}

	public Enumeration getSelectedConditionAtDischarge() {
		return selectedConditionAtDischarge;
	}

	public void setSelectedConditionAtDischarge(
			Enumeration selectedConditionAtDischarge) {
		this.selectedConditionAtDischarge = selectedConditionAtDischarge;
	}

	public Enumeration getSelectedDischargeDestination() {
		return selectedDischargeDestination;
	}

	public void setSelectedDischargeDestination(
			Enumeration selectedDischargeDestination) {
		this.selectedDischargeDestination = selectedDischargeDestination;
	}

	public List<Enumeration> getConditionAtDischarge() {
		return conditionAtDischarge;
	}

	public List<Enumeration> getDischargeDestination() {
		return dischargeDestination;
	}

	@Command
	public void selectDischargeDestination() {
		patientAdmissionCommand.patientClinicalDetails.dischargeDestinationId = selectedDischargeDestination
				.getEnumCode();
	}

	@Command
	public void selectConditionAtDischarge() {
		patientAdmissionCommand.patientClinicalDetails.conditionAtDischargeId = selectedConditionAtDischarge
				.getEnumCode();
	}

	@Command
	public void savePatientAdmissionAndClinicalDetails() {
		patientAdmissionCommand.mrnNumber = mrnNumber;
		if("true".equals(patientAdmissionNotSaved)){
	        CreatePatientAdmissionCommand createPatientAdmissionCommand = new CreatePatientAdmissionCommand();
        createPatientAdmissionCommand = populate(patientAdmissionCommand,
                createPatientAdmissionCommand);
        createPatientAdmissionCommand.mrnNumber = mrnNumber;
        sendCommand(createPatientAdmissionCommand);
		}
		PatientClinicalDetails patientClinicalDetails = patientAdmissionCommand.patientClinicalDetails;
		patientClinicalDetails.inPatientNumber = inPatientnumber;
		if (isSuccess(sendCommand(patientClinicalDetails))) {
			displayMessages.displaySuccess();
		}
	}

	public Enumeration getSelectedInternalHospitalUnit() {
		return selectedInternalHospitalUnit;
	}

	public void setSelectedInternalHospitalUnit(
			Enumeration selectedInternalHospitalUnit) {
		this.selectedInternalHospitalUnit = selectedInternalHospitalUnit;
	}

	public Enumeration getSelectedInternalClinic() {
		return selectedInternalClinic;
	}

	public void setSelectedInternalClinic(Enumeration selectedInternalClinic) {
		this.selectedInternalClinic = selectedInternalClinic;
	}

	public RecordPatientAdmissionCommand getPatientAdmissionCommand() {
		return patientAdmissionCommand;
	}

	public List<Enumeration> getReferredFrom() {
		return referredFrom;
	}

	public List<Enumeration> getHospitalUnits() {
		return hospitalUnits;
	}

	public boolean isViewMode() {
		return viewMode;
	}

	public Map<String, String> getClinicalDetailsMap() {
		return clinicalDetailsMap;
	}

	public List<Enumeration> getSelectedIndications() {
		return selectedIndications;
	}

	public void setSelectedIndications(List<Enumeration> selectedIndications) {
		this.selectedIndications = selectedIndications;
	}

	public void setHospitalUnits(List<Enumeration> hospitalUnits) {
		this.hospitalUnits = hospitalUnits;
	}

	public List<IcdElement> getSelectedAdmittingDiagnosis() {
		return selectedAdmittingDiagnosis;
	}

	public void setSelectedAdmittingDiagnosis(
			List<IcdElement> selectedAdmittingDiagnosis) {
		this.selectedAdmittingDiagnosis = selectedAdmittingDiagnosis;
	}

	public List<IcdElement> getSelectedDischargeDiagnosis() {
		return selectedDischargeDiagnosis;
	}

	public void setSelectedDischargeDiagnosis(
			List<IcdElement> selectedDischargeDiagnosis) {
		this.selectedDischargeDiagnosis = selectedDischargeDiagnosis;
	}

	public List<IcdElement> getSelectedFinalDiagnosis() {
		return selectedFinalDiagnosis;
	}

	public void setSelectedFinalDiagnosis(List<IcdElement> selectedFinalDiagnosis) {
		this.selectedFinalDiagnosis = selectedFinalDiagnosis;
	}

	public List<Procedure> getSelectedProceduresDone() {
		return selectedProceduresDone;
	}

	public void setSelectedProceduresDone(List<Procedure> selectedProceduresDone) {
		this.selectedProceduresDone = selectedProceduresDone;
	}

	@Command
	@NotifyChange("selectedIndications")
	public void addIndicationsForAdmission(@BindingParam("listbox") final Listbox listbox) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("indications", selectedIndications);
		Window window = (Window) Executions.createComponents(
				"/com/patient/addIndicationsForAdmissions.zul",
				null,map);
		window.addEventListener("onDetach", new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				List<Enumeration> enumerations = ((List<Enumeration>)event.getData());
				selectedIndications =enumerations; 
				listbox.setModel(new BindingListModelList<Enumeration>(selectedIndications, true));
				for(Enumeration enumeration : selectedIndications)
					selectedIndicationsForAdmissionsEnumCode.add(enumeration.getEnumCode());
				patientAdmissionCommand.patientClinicalDetails.indicationForAdmissionIds = selectedIndicationsForAdmissionsEnumCode;
                System.out.println("selectedIndications  " + selectedIndications);
            }
		});
	}
	
	@Command
	@NotifyChange("selectedAdmittingDiagnosis")
	public void addAdmittingDiagnosis(@BindingParam("admittingDiagnosisListbox") final Listbox listbox){
		Map<String, Object> map = new HashMap<String, Object>();
		String multiSelect = "true";
		map.put("multiSelect", multiSelect);
		map.put("icdElements", selectedAdmittingDiagnosis);
		Window window = (Window) Executions.createComponents(
				"/com/patient/icdElement.zul",
				null,map);
		window.addEventListener("onDetach", new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				selectedAdmittingDiagnosis = (List<IcdElement>) event.getData();
				listbox.setModel(new BindingListModelList<IcdElement>(selectedAdmittingDiagnosis, true));
				for(IcdElement element : selectedAdmittingDiagnosis)
					selectedAdmittingDiagnosisIds.add(element.getIcdCode());
				patientAdmissionCommand.patientClinicalDetails.selectedAdmittingDiagnosisIds = selectedAdmittingDiagnosisIds;
			}
		});
	}
	
	@Command
	@NotifyChange("selectedDischargeDiagnosis")
	public void addDischargeDiagnosis(@BindingParam("dischargeDiagnosisListbox")final Listbox listbox){
		Map<String, Object> map = new HashMap<String, Object>();
		String multiSelect = "true";
		map.put("multiSelect", multiSelect);
		map.put("icdElements", selectedDischargeDiagnosis);
		Window window = (Window) Executions.createComponents(
				"/com/patient/icdElement.zul",
				null,map);
		window.addEventListener("onDetach", new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				selectedDischargeDiagnosis = (List<IcdElement>) event.getData();
				listbox.setModel(new BindingListModelList<IcdElement>(selectedDischargeDiagnosis, true));
				for(IcdElement element : selectedDischargeDiagnosis)
					selectedDischargeDiagnosisIds.add(element.getIcdCode());
				patientAdmissionCommand.patientClinicalDetails.selectedDischargeDiagnosisIds = selectedDischargeDiagnosisIds;
			}
		});
	}
	
	@Command
	@NotifyChange("selectedFinalDiagnosis")
	public void addFinalDiagnosis(@BindingParam("finalDiagnosisListbox") final Listbox listbox){
		Map<String, Object> map = new HashMap<String, Object>();
		String multiSelect = "true";
		map.put("multiSelect", multiSelect);
		map.put("icdElements", selectedFinalDiagnosis);
		Window window = (Window) Executions.createComponents(
				"/com/patient/icdElement.zul",
				null,map);
		window.addEventListener("onDetach", new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				selectedFinalDiagnosis = (List<IcdElement>) event.getData();
				listbox.setModel(new BindingListModelList<IcdElement>(selectedFinalDiagnosis, true));
				for(IcdElement element : selectedFinalDiagnosis)
					selectedFinalDiagnosisIds.add(element.getIcdCode());
				patientAdmissionCommand.patientClinicalDetails.selectedFinalDiagnosisIds = selectedFinalDiagnosisIds;
			}
		});
	}
	
	@Command
	@NotifyChange("selectedProceduresDone")
	public void addProcedures(@BindingParam("procedureDoneListbox")final Listbox listbox){
		Map<String, Object> map = new HashMap<String, Object>();
		String multiSelect = "true";
		map.put("multiSelect", multiSelect);
		map.put("procedures", selectedProceduresDone);
		Window window = (Window) Executions.createComponents(
				"/com/patient/cptSearch.zul",
				null,map);
		window.addEventListener("onDetach", new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				selectedProceduresDone = (List<Procedure>) event.getData();
				listbox.setModel(new BindingListModelList<Procedure>(selectedProceduresDone, true));
				for(Procedure procedure: selectedProceduresDone)
					selectedProceduresDoneIds.add(procedure.getIDNum());
				patientAdmissionCommand.patientClinicalDetails.selectedProceduresDoneIds = selectedProceduresDoneIds;
			}
		});
	}
	
	public boolean isEmpty(){
		if(UtilValidator.isEmpty(patientAdmissionCommand.patientClinicalDetails.indicationForAdmissionIds) || UtilValidator.isEmpty(patientAdmissionCommand.patientClinicalDetails.selectedAdmittingDiagnosisIds) ||
				UtilValidator.isEmpty(patientAdmissionCommand.patientClinicalDetails.selectedDischargeDiagnosisIds) || UtilValidator.isEmpty(patientAdmissionCommand.patientClinicalDetails.selectedFinalDiagnosisIds)||
				UtilValidator.isEmpty(patientAdmissionCommand.patientClinicalDetails.selectedProceduresDoneIds))
			return true;
		String[] strings = new String[]{patientAdmissionCommand.patientClinicalDetails.conditionAtDischargeId,patientAdmissionCommand.patientClinicalDetails.dischargeDestinationId,
				patientAdmissionCommand.patientClinicalDetails.clinicalUnitId,patientAdmissionCommand.patientClinicalDetails.hospitalUnitId,patientAdmissionCommand.patientClinicalDetails.outsideReferral,
				patientAdmissionCommand.patientClinicalDetails.referredFromId};
		for(String string : strings){
		boolean isEmpty = empty(string);
		if(isEmpty)
			return true;
		}
		return false;
	}
	
	private boolean empty(String string){
		return UtilValidator.isEmpty(string);
	}
	
	@Command
	@NotifyChange("selectedIndications")
	public void removeIndications(@BindingParam("item")Enumeration indication){
		selectedIndications.remove(indication);
		selectedIndicationsForAdmissionsEnumCode.remove(indication.getEnumCode());
		patientAdmissionCommand.patientClinicalDetails.indicationForAdmissionIds = selectedIndicationsForAdmissionsEnumCode;
		RemoveIndicationsCommand removeIndicationsCommand = new RemoveIndicationsCommand(Lists.newArrayList(indication), inPatientnumber); 
		Boolean removed = (Boolean) sendCommand(removeIndicationsCommand);
	}
	
	@Command
	@NotifyChange("selectedAdmittingDiagnosis")
	public void removeAdmissionDiagnosis(@BindingParam("admissionDiagnosis")IcdElement admissionDiagnosis){
		selectedAdmittingDiagnosis.remove(admissionDiagnosis);
		selectedAdmittingDiagnosisIds.remove(admissionDiagnosis.getIcdCode());
		patientAdmissionCommand.patientClinicalDetails.selectedAdmittingDiagnosisIds = selectedAdmittingDiagnosisIds;
		RemoveAdmissionDiagnosisCommand removeAdmissionDiagnosisCommand = new RemoveAdmissionDiagnosisCommand(Lists.newArrayList(admissionDiagnosis), inPatientnumber);
		Boolean removed = (Boolean) sendCommand(removeAdmissionDiagnosisCommand);
	}
	
	@Command
	@NotifyChange("selectedDischargeDiagnosis")
	public void removeDischargeDiagnosis(@BindingParam("dischargeDiagnosis")IcdElement dischargeDiagnosis){
		selectedDischargeDiagnosis.remove(dischargeDiagnosis);
		selectedDischargeDiagnosisIds.remove(dischargeDiagnosis.getIcdCode());
		patientAdmissionCommand.patientClinicalDetails.selectedDischargeDiagnosisIds = selectedDischargeDiagnosisIds;
		RemoveDischargeDiagnosisCommand removeDischargeDiagnosisCommand = new RemoveDischargeDiagnosisCommand(Lists.newArrayList(dischargeDiagnosis), inPatientnumber);
		Boolean removed = (Boolean) sendCommand(removeDischargeDiagnosisCommand);
	}

	@Command
	@NotifyChange("selectedFinalDiagnosis")
	public void removeFinalDiagnosis(@BindingParam("finalDiagnosis")IcdElement finalDiagnosis){
		selectedFinalDiagnosis.remove(finalDiagnosis);
		selectedFinalDiagnosisIds.remove(finalDiagnosis.getIcdCode());
		patientAdmissionCommand.patientClinicalDetails.selectedFinalDiagnosisIds = selectedFinalDiagnosisIds;
		RemoveFinalDiagnosisCommand removeFinalDiagnosisCommand = new RemoveFinalDiagnosisCommand(Lists.newArrayList(finalDiagnosis), inPatientnumber);
		Boolean removed = (Boolean) sendCommand(removeFinalDiagnosisCommand);
	}

	@Command
	@NotifyChange("selectedProceduresDone")
	public void removeProceduresPerformedDiagnosis(@BindingParam("procedure")Procedure procedure){
		selectedProceduresDone.remove(procedure);
		selectedProceduresDoneIds.remove(procedure.getIDNum());
		patientAdmissionCommand.patientClinicalDetails.selectedProceduresDoneIds = selectedProceduresDoneIds;
		RemoveProceduresDoneCommand removeProceduresCommand = new RemoveProceduresDoneCommand(Lists.newArrayList(procedure), inPatientnumber);
		Boolean removed = (Boolean) sendCommand(removeProceduresCommand);
	}
	
	@Command
	public void back(){
		Map<String, String> map = new HashMap<String, String>();
        map.put("mrnNumber", mrnNumber);
        map.put("patientId", patientId);
        map.put("inPatientNumber", inPatientnumber);
		navigation.redirect("addPatientAdmission", map);
	}
}