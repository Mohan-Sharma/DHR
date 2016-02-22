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
import org.zkoss.bind.BindContext;
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

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.patientadmission.command.RecordPatientAdmissionCommand;
import com.patientadmission.domain.Ward;
import com.patientadmission.presentation.queries.PatientAdmissionFinder;

@Composer
@VariableResolver(DelegatingVariableResolver.class)
public class WardIndexViewModel extends AbstractZKModel{

	private List<Map<String, ?>> maps = new ArrayList<Map<String, ?>>();

	private RecordPatientAdmissionCommand patientAdmissionCommand = new RecordPatientAdmissionCommand();

	//private Set<Ward> wards = Sets.newHashSet();

	//private Ward selectedWard;

	@WireVariable
	private PatientAdmissionFinder patientAdmissionFinder;

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
	
	private List<Map<String, ?>> allWards;
	private Map<String, ?> selectedWard;
	private List<Map<String, ?>> wards;
	private IsADoctor isADoctor;
	
	@WireVariable
    private IUserLoginRepository userLoginRepository;

	@Init
	@AfterCompose
	public void init(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, true);
		isADoctor = userLoginRepository.findUserLoginWithUserName(loggedInUser.getUsername());
		wards = patientAdmissionFinder
				.findAllWards();
		 HashMap<String, Object> wardMap = new HashMap();
		 wardMap.put("id", new Long(0));
		 wardMap.put("wardName", "All Wards");
		 wards.add(0, wardMap);
	}

	public List<Map<String, ?>> getMaps() {
		return maps;
	}

	@Command
	public void selectGender(@BindingParam("content") String gender) {
		this.gender = gender;
	}

	@Command
	@NotifyChange({ "maps", "patientCountLabel", "patientMrnNumberCount",
			"mapMultiple" })
	public void search() {
		Date nowDate = UtilDateTime.nowDate();
		Integer totalSize = 0;
		Date calulatedDate = null;

		if (selectedWard == null) {
			return;
		}

		if (ageListBox.getSelectedItem() != null && age != null) {
			if (ageListBox.getSelectedItem().getValue().equals("In Between"))
				nowDate = UtilDateTime.addYearsToDate(UtilDateTime.nowDate(),
						-thruAge);
			calulatedDate = UtilDateTime.addYearsToDate(UtilDateTime.nowDate(),
					-age);
		}

		if ("All Wards".equals(selectedWard.get("wardName"))) {
			maps = new ArrayList<Map<String, ?>>();
			for (int i = 0; i < (crudDao.getAll(Ward.class)
					.size()); i++) {
				List<Ward> ward = crudDao
						.getAll(Ward.class);
				
				Map<String, Object> map2 = Maps.newHashMap();
				List<Map<String, ?>> listWard = new ArrayList<Map<String, ?>>();
				listWard = patientAdmissionFinder.searchAdmittedWardBy(
								admissionDate != null ? UtilDateTime
										.getDayStart(admissionDate) : null,
								(admissionDateThru != null ? UtilDateTime
										.getDayEnd(admissionDateThru) : null),
								nowDate,
								ward.get(i).getId(),
								genderListBox != null ? ((genderListBox
										.getSelectedItem() != null ? (String) genderListBox
										.getSelectedItem().getValue() : null))
										: null,
								age,
								ageListBox != null ? (ageListBox
										.getSelectedItem() != null ? (String) ageListBox
										.getSelectedItem().getValue() : null)
										: null, calulatedDate,isADoctor.getDoctorId(),((UserLogin)isADoctor).getRole());
				
				
				if (UtilValidator.isNotEmpty(listWard) && (listWard.size()!= 0)) {
					map2.put("ID", ward.get(i).getId());
					map2.put("WARD_NAME", ward.get(i)
							.getWardName());
					map2.put("WARD_LIST", listWard);
					map2.put("SIZE", listWard.size());
					maps.add(map2);
				}
				totalSize = totalSize + listWard.size();
			}
			singleModel.setVisible(false);
			multipleModel.setVisible(true);
			//patientCountLabelmultiple.setValue("Total No Of Patient Records:"+ totalSize.toString());

		} else {
			singleModel.setVisible(true);
			multipleModel.setVisible(false);
			maps = patientAdmissionFinder
					.searchAdmittedWardBy(
							(admissionDate != null ? UtilDateTime
									.getDayStart(admissionDate) : null),
							(admissionDateThru != null ? UtilDateTime
									.getDayEnd(admissionDateThru) : null),
							nowDate,
							(Long) (selectedWard != null ? selectedWard.get("id") : null),
							genderListBox != null ? ((genderListBox
									.getSelectedItem() != null ? (String) genderListBox
									.getSelectedItem().getValue() : null))
									: null,
							age,
							ageListBox != null ? (ageListBox.getSelectedItem() != null ? (String) ageListBox
									.getSelectedItem().getValue() : null)
									: null, calulatedDate,isADoctor.getDoctorId(),((UserLogin)isADoctor).getRole());
			//patientCountLabel.setValue("Total :" + maps.size());
			//patientCountLabel.setVisible(UtilValidator.isNotEmpty(maps));
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
	public void selectAdmittingWard(BindContext ctx) {
		Combobox combobox = (Combobox) ctx.getComponent();
		combobox.addEventListener("onReload", new EventListener<Event>() {
			@Override
			public void onEvent(Event event) throws Exception {
				selectedWard = ((Ward) event.getData());
				patientAdmissionCommand.admittingWardId = selectedWard
						.getId();
			}
		});
		if (combobox.getSelectedItem() == null)
			return;
		selectedWard = ((Combobox) ctx.getComponent()).getSelectedItem()
				.getValue();
		if (selectedWard != null && selectedWard.getId() == null) {
			Component targetComp = ctx.getComponent();
			Map viewArgs = new HashMap();
			viewArgs.put("targetComp", targetComp);
			viewArgs.put("masterList", wards);

		} else
			patientAdmissionCommand.admittingWardId = selectedWard
					.getId();

	}*/

	@Command
	public void displayWardName(@BindingParam("label1") Label label,
			@BindingParam("map") Map<String, ?> map) {
		
		if (map.get("ADMITTINGWARD") != null) {
			Ward admittingWard = crudDao.find(
					Ward.class, (Long) map.get("ADMITTINGWARD"));
			
			label.setValue(admittingWard.getWardName());
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

	public Map<String, ?> getSelectedWard() {
		return selectedWard;
	}

	public void setSelectedWard(Map<String, ?> selectedWard) {
		this.selectedWard = selectedWard;
	}

	public List<Map<String, ?>> getDepartments() {
		return allWards;
	}

	public void setDepartments(List<Map<String, ?>> allWards) {
		this.allWards = allWards;
	}

	public List<Map<String, ?>> getWards() {
		return wards;
	}

	public void setWards(List<Map<String, ?>> wards) {
		this.wards = wards;
	}


	/**
	public HashMap<Long, List<Map<String, ?>>> getMapMultiple() {
		return mapMultiple;
	}

	public void setMapMultiple(HashMap<Long, List<Map<String, ?>>> mapMultiple) {
		this.mapMultiple = mapMultiple;
	}**/

}
