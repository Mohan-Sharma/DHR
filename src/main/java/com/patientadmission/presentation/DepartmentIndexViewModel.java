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
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.event.PagingEvent;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.patientadmission.command.RecordPatientAdmissionCommand;
import com.patientadmission.domain.AdmittingDepartment;
import com.patientadmission.presentation.queries.PatientAdmissionFinder;

@Composer
@VariableResolver(DelegatingVariableResolver.class)
public class DepartmentIndexViewModel extends AbstractZKModel {

	private List<Map<String, ?>> maps = new ArrayList<Map<String, ?>>();

	private HashMap<Long, List<Map<String, ?>>> mapMultiple = new HashMap<Long, List<Map<String, ?>>>();

	private RecordPatientAdmissionCommand patientAdmissionCommand = new RecordPatientAdmissionCommand();

	private Set<AdmittingDepartment> admittingDepartments = Sets.newHashSet();

	private AdmittingDepartment selectedDepartment;

	@WireVariable
	private PatientAdmissionFinder patientAdmissionFinder;

	private Integer age;

	private Date admissionDate;

	@Wire
	private Listbox ageListBox;

	@Wire("#genderListBox")
	private Listbox genderListBox;

	@Wire("#pagingSingel")
	private Paging pagingSingel;

	@Wire("#pagingMultiple")
	private Paging pagingMultiple;

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
	
	@WireVariable
    private IUserLoginRepository userLoginRepository;
	private IsADoctor isADoctor;
	private List<Map<String, ?>> departments;
	private Map<String, ?> selectedDep;
	
	

	@Init
	@AfterCompose
	public void init(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, true);
		isADoctor = userLoginRepository.findUserLoginWithUserName(loggedInUser.getUsername());
		departments = patientAdmissionFinder.findAllDepartments();
		HashMap<String, Object> departmentMap = new HashMap();
		departmentMap.put("id", new Long(0));
		departmentMap.put("departmentName", "All Department");
		departments.add(0, departmentMap);
		
	}
	
	 public void setUserRepository(IUserLoginRepository userRepository) {
	        this.userLoginRepository = userRepository;
	    }

	public List<Map<String, ?>> getMaps() {
		return maps;
	}

	@Command
	public void selectGender(@BindingParam("content") String gender) {
		this.gender = gender;
	}
	public PatientAdmissionFinder getPatientAdmissionFinder() {
        return patientAdmissionFinder;
    }

	@Command
	@NotifyChange({ "maps", "patientCountLabel", "patientMrnNumberCount",
	"mapMultiple" })
	public void search(@BindingParam("minPage") int minPage, @BindingParam("maxPage") int maxPage) {
		Date nowDate = UtilDateTime.nowDate();
		Integer totalSize = 0;
		Date calulatedDate = null;

		if (ageListBox.getSelectedItem() != null && age != null) {
			if (ageListBox.getSelectedItem().getValue().equals("In Between"))
				nowDate = UtilDateTime.addYearsToDate(UtilDateTime.nowDate(),
						-thruAge);
			calulatedDate = UtilDateTime.addYearsToDate(UtilDateTime.nowDate(),
					-age);
		}

		if (selectedDep != null && "All Department".equals(selectedDep.get("departmentName"))) {
			maps = new ArrayList<Map<String, ?>>();
			int  maxCount ;
			for (int i = 0; i < (crudDao.getAll(AdmittingDepartment.class)
					.size()); i++) {
				List<AdmittingDepartment> department = crudDao
						.getAll(AdmittingDepartment.class);

				Map<String, Object> map2 = Maps.newHashMap();
				List<Map<String, ?>> listDepartment = new ArrayList<Map<String, ?>>();
				listDepartment = patientAdmissionFinder
						.searchDepartmentBy(
								admissionDate != null ? UtilDateTime
										.getDayStart(admissionDate) : null,
										(admissionDateThru != null ? UtilDateTime
												.getDayEnd(admissionDateThru) : null),
												nowDate,
												department.get(i).getId(),
												genderListBox != null ? ((genderListBox
														.getSelectedItem() != null ? (String) genderListBox
																.getSelectedItem().getValue() : null))
																: null,
																age,
																ageListBox != null ? (ageListBox
																		.getSelectedItem() != null ? (String) ageListBox
																				.getSelectedItem().getValue() : null)
																				: null, calulatedDate,null,null,isADoctor.getDoctorId(),((UserLogin)isADoctor).getRole());
				if (UtilValidator.isNotEmpty(listDepartment) && (listDepartment.size()!= 0)) {
					map2.put("ID", department.get(i).getId());
					map2.put("DEPARTMENT_NAME", department.get(i)
							.getDepartmentName());
					map2.put("DEPARTMENT_LIST", listDepartment);
					map2.put("SIZE", listDepartment.size());
					maps.add(map2);
				}
				totalSize = totalSize + listDepartment.size();
			}
			singleModel.setVisible(false);
			multipleModel.setVisible(true);
			pagingSingel.setVisible(false);
			//patientCountLabelmultiple.setValue("Total No Of Patient Records:"+ totalSize.toString());

		} else {
			singleModel.setVisible(true);
			multipleModel.setVisible(false);
            maps = patientAdmissionFinder
					.searchDepartmentBy(
							(admissionDate != null ? UtilDateTime
									.getDayStart(admissionDate) : null),
									(admissionDateThru != null ? UtilDateTime
											.getDayEnd(admissionDateThru) : null),
											nowDate,
											(Long) (selectedDep != null ? selectedDep.get("id") : null),
											genderListBox != null ? ((genderListBox
													.getSelectedItem() != null ? (String) genderListBox
															.getSelectedItem().getValue() : null))
															: null,
															age,
															ageListBox != null ? (ageListBox.getSelectedItem() != null ? (String) ageListBox
																	.getSelectedItem().getValue() : null)
																	: null, calulatedDate,minPage,maxPage,isADoctor.getDoctorId(),((UserLogin)isADoctor).getRole());
			Long maxCont = (Long) patientAdmissionFinder
				.searchDepartmentCount(
					(admissionDate != null ? UtilDateTime
						.getDayStart(admissionDate) : null),
				(admissionDateThru != null ? UtilDateTime
						.getDayEnd(admissionDateThru) : null),
				nowDate,
				(Long) (selectedDep != null ? selectedDep.get("id") : null),
				genderListBox != null ? ((genderListBox
						.getSelectedItem() != null ? (String) genderListBox
						.getSelectedItem().getValue() : null))
						: null,
				age,
				ageListBox != null ? (ageListBox.getSelectedItem() != null ? (String) ageListBox
						.getSelectedItem().getValue() : null)
						: null, calulatedDate,isADoctor.getDoctorId(),((UserLogin)isADoctor).getRole()).get(0).get("CASECOUNT");

			//patientCountLabel.setValue("Total :" + maxCont);
			//patientCountLabel.setVisible(UtilValidator.isNotEmpty(maps));
			pagingSingel.setVisible(true);
			pagingSingel.setPageSize(maxPage);
			pagingSingel.setTotalSize(maxCont.intValue());
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

	/*@Command
	public void selectAdmittingDepartment(BindContext ctx) {
		Combobox combobox = (Combobox) ctx.getComponent();
		combobox.addEventListener("onReload", new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				selectedDepartment = ((AdmittingDepartment) event.getData());
				patientAdmissionCommand.admittingDepartmentId = selectedDepartment
						.getId();
			}
		});
		if (combobox.getSelectedItem() == null)
			return;
		selectedDepartment = ((Combobox) ctx.getComponent()).getSelectedItem()
				.getValue();
		if (selectedDepartment != null && selectedDepartment.getId() == null) {
			Component targetComp = ctx.getComponent();
			Map viewArgs = new HashMap();
			viewArgs.put("targetComp", targetComp);
			viewArgs.put("masterList", admittingDepartments);

		} else
			patientAdmissionCommand.admittingDepartmentId = selectedDepartment
					.getId();

	}*/

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

	public AdmittingDepartment getSelectedDepartment() {
		return selectedDepartment;
	}

	public void setSelectedDepartment(AdmittingDepartment selectedDepartment) {
		this.selectedDepartment = selectedDepartment;
	}

	public Set<AdmittingDepartment> getAdmittingDepartments() {
		return admittingDepartments;
	}

	public HashMap<Long, List<Map<String, ?>>> getMapMultiple() {
		return mapMultiple;
	}

	public void setMapMultiple(HashMap<Long, List<Map<String, ?>>> mapMultiple) {
		this.mapMultiple = mapMultiple;
	}

	@SuppressWarnings("unchecked")
	@Command("attachPagingEventListner")
	public void attachPagingEventListner() {
		final int PAGE_SIZE = 10;
		pagingSingel.addEventListener("onPaging", new EventListener() {
			public void onEvent(Event event) {
				PagingEvent pe = (PagingEvent) event;
				int pgno = pe.getActivePage();
				int ofs = pgno * PAGE_SIZE;
				search(ofs, PAGE_SIZE);
			}

		});
	}

	public List<Map<String, ?>> getDepartments() {
		return departments;
	}

	public void setDepartments(List<Map<String, ?>> departments) {
		this.departments = departments;
	}

	public Map<String, ?> getSelectedDep() {
		return selectedDep;
	}

	public void setSelectedDep(Map<String, ?> selectedDep) {
		this.selectedDep = selectedDep;
	}

}
