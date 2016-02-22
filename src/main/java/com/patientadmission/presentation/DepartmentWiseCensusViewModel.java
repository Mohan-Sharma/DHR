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
import com.patientadmission.domain.AdmittingDepartment;
import com.patientadmission.presentation.queries.PatientAdmissionFinder;

@Composer
@VariableResolver(DelegatingVariableResolver.class)
public class DepartmentWiseCensusViewModel {

	private List<Map<String, ?>> maps ;

	private RecordPatientAdmissionCommand patientAdmissionCommand = new RecordPatientAdmissionCommand();

	private Set<AdmittingDepartment> admittingDepartments = Sets.newHashSet();

	private AdmittingDepartment selectedDepartment;

	@WireVariable
	private PatientAdmissionFinder patientAdmissionFinder;

	private Date admissionDate;

	private Date admissionDateThru;

	@WireVariable
	private ICrud crudDao;

	@Wire("#singleModel")
	private Grid singleModel;

	@Init
	@AfterCompose
	public void init(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, true);
		List<Map<String, ?>> admittingDeptMapList = patientAdmissionFinder
				.findAllAdmittingDepartments();
		for (Map<String, ?> m : admittingDeptMapList) {
			for (Map.Entry<String, ?> mEntry : m.entrySet()) {
				if ("departmentId".equals(mEntry.getKey())) {
					admittingDepartments.add((AdmittingDepartment) crudDao
							.find(AdmittingDepartment.class,
									(Long) mEntry.getValue()));
				}
			}
		}
		admittingDepartments.add(new AdmittingDepartment("All Department"));

	}

	public List<Map<String, ?>> getMaps() {
		return maps;
	}

	@Command
	@NotifyChange({ "maps" })
	public void search() {
		maps= new ArrayList<Map<String, ?>>();
		Map<String, Object> map2 = Maps.newHashMap();
		map2.put("DEPARTMENTNAME", selectedDepartment!=null?selectedDepartment.getDepartmentName():"All");
		map2.put("ADMISSION_MALE",patientAdmissionFinder.searchPatientAdmissionByDepartmentAndDates(admissionDate, admissionDateThru, null , null ,selectedDepartment!=null?selectedDepartment.getId():null,"MALE").get(0).get("CASECOUNT"));
		map2.put("ADMISSION_FEMALE",  patientAdmissionFinder.searchPatientAdmissionByDepartmentAndDates(admissionDate, admissionDateThru, null , null ,selectedDepartment!=null?selectedDepartment.getId():null,"FEMALE").get(0).get("CASECOUNT"));
		map2.put("DISCHARGE_MALE", patientAdmissionFinder.searchPatientAdmissionByDepartmentAndDates(null,null,admissionDate, admissionDateThru,selectedDepartment!=null?selectedDepartment.getId():null,"MALE").get(0).get("CASECOUNT"));
		map2.put("DISCHARGE_FEMALE",patientAdmissionFinder.searchPatientAdmissionByDepartmentAndDates(null,null,admissionDate, admissionDateThru,selectedDepartment!=null?selectedDepartment.getId():null,"FEMALE").get(0).get("CASECOUNT"));
		maps.add(map2);
	}

	@Command
	public void downloadDocument(@BindingParam("content") Map<String, ?> map) {
		File file = new File((String) map.get("FILE_PATH"));
		String contentType = (String) map.get("CONTENT_TYPE");
		try {
			Filedownload.save(file, contentType);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public Date getAdmissionDate() {
		return admissionDate;
	}

	public void setAdmissionDate(Date admissionDate) {
		this.admissionDate = admissionDate;
	}

	public Date getAdmissionDateThru() {
		return admissionDateThru;
	}

	public void setAdmissionDateThru(Date admissionDateThru) {
		this.admissionDateThru = admissionDateThru;
	}

	public void setMaps(List<Map<String, ?>> maps) {
		this.maps = maps;
	}

	@Command
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

	public AdmittingDepartment getSelectedDepartment() {
		return selectedDepartment;
	}

	public void setSelectedDepartment(AdmittingDepartment selectedDepartment) {
		this.selectedDepartment = selectedDepartment;
	}

	public Set<AdmittingDepartment> getAdmittingDepartments() {
		return admittingDepartments;
	}

}
