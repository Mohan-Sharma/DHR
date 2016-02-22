package com.patientadmission.presentation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.nthdimenzion.crud.ICrud;
import org.nthdimenzion.object.utils.UtilDateTime;
import org.nthdimenzion.object.utils.UtilValidator;
import org.nthdimenzion.presentation.annotations.Composer;
import org.nthdimenzion.presentation.infrastructure.Navigation;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.patientadmission.command.RecordPatientAdmissionCommand;

import com.patientadmission.domain.Doctor;

import com.patientadmission.presentation.queries.PatientAdmissionFinder;

@Composer
@VariableResolver(DelegatingVariableResolver.class)
public class PhysicianIndexViewModel {

	private List<Map<String, ?>> maps = new ArrayList<Map<String, ?>>();

	// private HashMap<Long, List<Map<String, ?>>> mapMultiple = new HashMap<Long, List<Map<String, ?>>>();

	private RecordPatientAdmissionCommand patientAdmissionCommand = new RecordPatientAdmissionCommand();

	private Set<Doctor> doctors = Sets.newHashSet();

	private Doctor selectedDoctor;

	@WireVariable
	private PatientAdmissionFinder patientAdmissionFinder;
	
	@Wire("#Paging")
	private Paging pagingComp;
	
	@Wire
	private Paging pagingComp1;
	

	private Integer age;

	private Date admissionDate;

	@Wire
	private Listbox ageListBox;

	@Wire("#genderListBox")
	private Listbox genderListBox;

	private String gender;

	private Date admissionDateThru;

	private Integer thruAge;

	@Wire("#thruAgeBox")
	private Intbox thruAgeBox;

	@WireVariable
	private ICrud crudDao;

	@Wire("#patientCountLabel")
	private Label patientCountLabel;

	@Wire("#patientCountLabelmultiple")
	private Label patientCountLabelmultiple;

	@Wire("#singleModel")
	private Grid singleModel;

	@Wire("#multipleModel")
	private Grid multipleModel;
	
	private Integer max = 10;
	private int page = 0;
	private Integer count = 0;
	
	@WireVariable
    protected Navigation navigation;

	@Init
	@AfterCompose
	public void init(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, true);
		List<Map<String, ?>> DoctorMapList = patientAdmissionFinder.findAllDoctors();
				
		for (Map<String, ?> m : DoctorMapList) {
			for (Map.Entry<String, ?> mEntry : m.entrySet()) {
				if ("id".equals(mEntry.getKey())) {
					doctors.add((Doctor) crudDao.find(Doctor.class,
							(Long) mEntry.getValue()));
				}
			}
		}
		doctors.add(new Doctor("All","","Doctor"));

	}

	public List<Map<String, ?>> getMaps() {
		return maps;
	}

	@Command
	public void selectGender(@BindingParam("content") String gender) {
		this.gender = gender;
	}

	@Command
	@NotifyChange({ "maps", "patientCountLabel" })
	public void search(@BindingParam("minPage") int minPage,@BindingParam("maxPage") int maxPage) {
		Date nowDate = UtilDateTime.nowDate();
		Integer totalSize = 0;
		Date calulatedDate = null;
		if (selectedDoctor == null) {
			return;
		}

		if (ageListBox.getSelectedItem() != null && age != null) {
			if (ageListBox.getSelectedItem().getValue().equals("In Between"))
				nowDate = UtilDateTime.addYearsToDate(UtilDateTime.nowDate(),
						-thruAge);
			calulatedDate = UtilDateTime.addYearsToDate(UtilDateTime.nowDate(),
					-age);
		}
         
		if ("All  Doctor".equals(selectedDoctor.getName())) {
						maps = new ArrayList<Map<String, ?>>();
			for (int i = 0; i < (crudDao.getAll(Doctor.class)
					.size()); i++) {
				List<Doctor> Doctor = crudDao
						.getAll(Doctor.class);
				
				Map<String, Object> map2 = Maps.newHashMap();
				List<Map<String, ?>> listDoctor = new ArrayList<Map<String, ?>>();
				listDoctor = patientAdmissionFinder.searchDoctorBy(
								admissionDate != null ? UtilDateTime
										.getDayStart(admissionDate) : null,
								(admissionDateThru != null ? UtilDateTime
										.getDayEnd(admissionDateThru) : null),
								nowDate,
								Doctor.get(i).getId(),
								genderListBox != null ? ((genderListBox
										.getSelectedItem() != null ? (String) genderListBox
										.getSelectedItem().getValue() : null))
										: null,
								age,
								ageListBox != null ? (ageListBox
										.getSelectedItem() != null ? (String) ageListBox
										.getSelectedItem().getValue() : null)
										: null, calulatedDate,null, null);
				
				
				if (UtilValidator.isNotEmpty(listDoctor) && (listDoctor.size()!= 0)) {
					map2.put("ID", Doctor.get(i).getId());
					map2.put("DOCTOR_NAME", Doctor.get(i).getName());
					map2.put("DOCTOR_LIST", listDoctor);
					map2.put("SIZE", listDoctor.size());
					maps.add(map2);
				}
				totalSize = totalSize + listDoctor.size();
			}
			singleModel.setVisible(false);
			pagingComp.setVisible(false);
			multipleModel.setVisible(true);
			patientCountLabelmultiple.setValue("Total No Of Patient Records:"
					+ totalSize.toString());

		} else {
			singleModel.setVisible(true);
			multipleModel.setVisible(false);
			maps = patientAdmissionFinder
					.searchDoctorBy(
							(admissionDate != null ? UtilDateTime
									.getDayStart(admissionDate) : null),
							(admissionDateThru != null ? UtilDateTime
									.getDayEnd(admissionDateThru) : null),
							nowDate,
							selectedDoctor != null ? selectedDoctor
									.getId() : null,
							genderListBox != null ? ((genderListBox
									.getSelectedItem() != null ? (String) genderListBox
									.getSelectedItem().getValue() : null))
									: null,
							age,
							ageListBox != null ? (ageListBox.getSelectedItem() != null ? (String) ageListBox
									.getSelectedItem().getValue() : null)
									: null, calulatedDate, minPage, maxPage);
			Long maxCount = (Long) patientAdmissionFinder
				.searchDoctorByCount(
					(admissionDate != null ? UtilDateTime
							.getDayStart(admissionDate) : null),
					(admissionDateThru != null ? UtilDateTime
							.getDayEnd(admissionDateThru) : null),
					nowDate,
					selectedDoctor != null ? selectedDoctor
							.getId() : null,
					genderListBox != null ? ((genderListBox
							.getSelectedItem() != null ? (String) genderListBox
							.getSelectedItem().getValue() : null))
							: null,
					age,
					ageListBox != null ? (ageListBox.getSelectedItem() != null ? (String) ageListBox
							.getSelectedItem().getValue() : null)
							: null, calulatedDate).get(0).get("CASECOUNT");
			
			
			
			patientCountLabel.setValue("Total :" +maxCount.intValue());
			patientCountLabel.setVisible(UtilValidator.isNotEmpty(maps));
			pagingComp.setVisible(true);
			pagingComp.setPageSize(maxPage);
			pagingComp.setTotalSize(maxCount.intValue());
			BindUtils.postNotifyChange(null, null, this, "maps");
		}
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
	public void selectDoctor(BindContext ctx) {
		Combobox combobox = (Combobox) ctx.getComponent();
		combobox.addEventListener("onReload", new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				selectedDoctor = ((Doctor) event.getData());
				patientAdmissionCommand.admittingDoctorId = selectedDoctor
						.getId();
			}
		});
		if (combobox.getSelectedItem() == null)
			return;
		selectedDoctor = ((Combobox) ctx.getComponent()).getSelectedItem()
				.getValue();
		if (selectedDoctor != null && selectedDoctor.getId() == null) {
			Component targetComp = ctx.getComponent();
			Map viewArgs = new HashMap();
			viewArgs.put("targetComp", targetComp);
			viewArgs.put("masterList", doctors);

		} else
			patientAdmissionCommand.admittingDoctorId = selectedDoctor
					.getId();

	}

	@Command
	public void displayDoctorName(@BindingParam("label1") Label label,
			@BindingParam("map") Map<String, ?> map) {
		
		if (map.get("DOCTOR") != null) {
			Doctor workingDoctor = crudDao.find(
					Doctor.class, (Long) map.get("DOCTOR"));
			
			label.setValue(workingDoctor.getName());
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

	public Doctor getSelectedDoctor() {
		return selectedDoctor;
	}

	public void setSelectedDoctor(Doctor selectedDoctor) {
		this.selectedDoctor = selectedDoctor;
	}

	public Set<Doctor> getdoctors() {
		return doctors;
	}
	@SuppressWarnings("unchecked")
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

	}

}
