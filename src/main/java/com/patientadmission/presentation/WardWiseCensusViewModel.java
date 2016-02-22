package com.patientadmission.presentation;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nthdimenzion.crud.ICrud;
import org.nthdimenzion.object.utils.UtilValidator;
import org.nthdimenzion.presentation.annotations.Composer;
import org.zkoss.bind.BindContext;
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
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Combobox;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.master.domain.Enumeration;
import com.patientadmission.command.RecordPatientAdmissionCommand;
import com.patientadmission.domain.AdmittingDepartment;
import com.patientadmission.domain.Bed;
import com.patientadmission.domain.Doctor;
import com.patientadmission.domain.Ward;
import com.patientadmission.presentation.queries.PatientAdmissionFinder;

@Composer
@VariableResolver(DelegatingVariableResolver.class)
public class WardWiseCensusViewModel {

	private RecordPatientAdmissionCommand patientAdmissionCommand = new RecordPatientAdmissionCommand();
	private List<Ward> wards = Lists.newArrayList();
	private Ward selectedWard;
	private List<Map<String, ?>> maps ;	
	

	@WireVariable
	private PatientAdmissionFinder patientAdmissionFinder;

	private Date admissionDate;

	private Date admissionDateThru;

	@WireVariable
	private ICrud crudDao;

	@Init
	public void init(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, true);
		List<Map<String, ?>> wardMapList = patientAdmissionFinder
				.findAllWards();
		for (Map<String, ?> m : wardMapList) {
			for (Map.Entry<String, ?> mEntry : m.entrySet()) {
				if ("id".equals(mEntry.getKey())) {
					wards.add((Ward) crudDao.find(Ward.class,
							(Long) mEntry.getValue()));
				}
			}
		}
		wards.add(0, new Ward("All Ward"));

	}
	
	@Command
	@NotifyChange({ "maps" })
	public void search() {
		
		maps= new ArrayList<Map<String, ?>>();
		Map<String, Object> map2 = Maps.newHashMap();
		map2.put("WARDNAME", selectedWard!=null?selectedWard.getWardName():"All");
		map2.put("ADMISSION_MALE",patientAdmissionFinder.searchPatientAdmissionByWardAndDates(admissionDate, admissionDateThru, null , null ,selectedWard!=null?selectedWard.getId():null,"MALE").get(0).get("CASECOUNT"));
		map2.put("ADMISSION_FEMALE",  patientAdmissionFinder.searchPatientAdmissionByWardAndDates(admissionDate , admissionDateThru, null , null ,selectedWard!=null?selectedWard.getId():null,"FEMALE").get(0).get("CASECOUNT"));
		map2.put("DISCHARGE_MALE", patientAdmissionFinder.searchPatientAdmissionByWardAndDates(null,null,admissionDate , admissionDateThru,selectedWard!=null?selectedWard.getId():null,"MALE").get(0).get("CASECOUNT"));
		map2.put("DISCHARGE_FEMALE",patientAdmissionFinder.searchPatientAdmissionByWardAndDates(null,null,admissionDate, admissionDateThru,selectedWard!=null?selectedWard.getId():null,"FEMALE").get(0).get("CASECOUNT"));
		maps.add(map2);
	}

	

	@Command
	public void selectWard(BindContext ctx) {
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
		selectedWard = ((Combobox) ctx.getComponent())
				.getSelectedItem().getValue();
		if (selectedWard != null
				&& selectedWard.getId() == null) {
			Component targetComp = ctx.getComponent();
			Map viewArgs = new HashMap();
			viewArgs.put("targetComp", targetComp);
			viewArgs.put("masterList", wards);
			
		} else {
			patientAdmissionCommand.admittingWardId = selectedWard
					.getId();
		}
	}

	public List<Ward> getWards() {
		return wards;
	}

	public void setWards(List<Ward> wards) {
		this.wards = wards;
	}

	public Ward getSelectedWard() {
		return selectedWard;
	}

	public void setSelectedWard(Ward selectedWard) {
		this.selectedWard = selectedWard;
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

	public RecordPatientAdmissionCommand getPatientAdmissionCommand() {
		return patientAdmissionCommand;
	}

	public PatientAdmissionFinder getPatientAdmissionFinder() {
		return patientAdmissionFinder;
	}
	
	public List<Map<String, ?>> getMaps() {
		return maps;
	}

	public void setMaps(List<Map<String, ?>> maps) {
		this.maps = maps;
	}
	

}
