package com.patientadmission.presentation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nthdimenzion.crud.ICrud;
import org.nthdimenzion.object.utils.UtilDateTime;
import org.nthdimenzion.object.utils.UtilValidator;
import org.nthdimenzion.presentation.annotations.Composer;
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
import org.zkoss.zul.Window;

import com.patientadmission.domain.Procedure;
import com.patientadmission.presentation.queries.PatientAdmissionFinder;

@Composer
@VariableResolver(DelegatingVariableResolver.class)
public class ProcedureIndexViewModal {

	private List<Map<String, ?>> maps = new ArrayList<Map<String, ?>>();

	@WireVariable
	private PatientAdmissionFinder patientAdmissionFinder;

	@WireVariable
	private ICrud crudDao;

	private Integer age;

	private Date admissionDate;

	
	private Procedure selectedProcedure;
	
	@Wire
	private Listbox ageListBox;
	
	@Wire("#genderListBox")
	private Listbox genderListBox;

	private String gender;
	
	@Wire("#patientCountLabel")
	private Label patientCountLabel;
	

	@Wire("#procedureDoneTextBox")
	private Label procedureDoneTextBox;
	
	private Date admissionDateThru;
	
	private Integer thruAge;
	
	@Wire("#thruAgeBox")
	private Intbox thruAgeBox;
	
	  @Init
	    @AfterCompose
	    public void init(@ContextParam(ContextType.VIEW) Component view) {
	    Selectors.wireComponents(view, this, true);
		}

		public List<Map<String, ?>> getMaps() {
			return maps;
		}
		
		@Command
		public void selectGender(@BindingParam("content")String gender){
			this.gender =  gender;
		}
		
		@Command
		@NotifyChange({"maps","patientCountLabel","patientMrnNumberCount"})
		public void search(){
			
		}
		
		@Command
		public void downloadDocument(@BindingParam("content")Map<String, ?> map){
			File file = new File((String)map.get("FILE_PATH"));
			String contentType = (String)map.get("CONTENT_TYPE");
			try {
				Filedownload.save(file, contentType);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		@Command
		@NotifyChange("thruAgeBox")
		public void selectCondition(@BindingParam("item")String value){
		thruAgeBox.setVisible("In Between".equals(value));
		}
		
		@Command
		public void addProcedureDone(){
			String multiSelect = "false";
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("multiSelect", multiSelect);
			Window window = (Window) Executions.createComponents("/com/patient/cptSearch.zul",null,map);
			window.addEventListener("onDetach", new EventListener<Event>() {

				@Override
				public void onEvent(Event event) throws Exception {
					selectedProcedure = (Procedure) event.getData();
					procedureDoneTextBox.setValue(selectedProcedure.getDescription());
				}
			});
		}
		
		
		@Command
		public void resetProcedure(){
		procedureDoneTextBox.setValue(null);
		selectedProcedure= null;
		}

		public PatientAdmissionFinder getPatientAdmissionFinder() {
			return patientAdmissionFinder;
		}

		

		public ICrud getCrudDao() {
			return crudDao;
		}

		public void setCrudDao(ICrud crudDao) {
			this.crudDao = crudDao;
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

		public Label getProcedureDoneTextBox() {
			return procedureDoneTextBox;
		}

		public void setProcedureDoneTextBox(Label procedureDoneTextBox) {
			this.procedureDoneTextBox = procedureDoneTextBox;
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

		public Intbox getThruAgeBox() {
			return thruAgeBox;
		}

		public void setThruAgeBox(Intbox thruAgeBox) {
			this.thruAgeBox = thruAgeBox;
		}

		public void setMaps(List<Map<String, ?>> maps) {
			this.maps = maps;
		}

}
