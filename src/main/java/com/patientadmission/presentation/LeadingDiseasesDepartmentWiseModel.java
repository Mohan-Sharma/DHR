package com.patientadmission.presentation;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.nthdimenzion.crud.ICrud;
import org.nthdimenzion.object.utils.UtilDateTime;
import org.nthdimenzion.presentation.annotations.Composer;
import org.nthdimenzion.presentation.infrastructure.AbstractZKModel;
import org.nthdimenzion.security.domain.IUserLoginRepository;
import org.nthdimenzion.security.domain.IsADoctor;
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
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;

import com.google.common.collect.Sets;
import com.patientadmission.command.RecordPatientAdmissionCommand;
import com.patientadmission.domain.AdmittingDepartment;
import com.patientadmission.domain.Doctor;
import com.patientadmission.presentation.queries.PatientAdmissionFinder;

@Composer
@VariableResolver(DelegatingVariableResolver.class)
public class LeadingDiseasesDepartmentWiseModel extends AbstractZKModel {
    private List<Map<String, ?>> maps = new ArrayList<Map<String, ?>>();

    private Long totalCount;

    private RecordPatientAdmissionCommand patientAdmissionCommand = new RecordPatientAdmissionCommand();

    private Set<AdmittingDepartment> admittingDepartments = Sets.newHashSet();

    private AdmittingDepartment selectedDepartment;

    private Date dischargeDate;

    private Date dischargeDateThru;

    private String criteria;

    @Wire("#criteriaListBox")
    private Listbox criteriaListBox;

    @WireVariable
    private PatientAdmissionFinder patientAdmissionFinder;

    @WireVariable
    private ICrud crudDao;
    
	@WireVariable
    private IUserLoginRepository userLoginRepository;
	private IsADoctor isADoctor;

    @Init
    @AfterCompose
    public void init(@ContextParam(ContextType.VIEW) Component view) {
	Selectors.wireComponents(view, this, true);
	isADoctor = userLoginRepository.findUserLoginWithUserName(loggedInUser.getUsername());
	List<Map<String, ?>> admittingDeptMapList = patientAdmissionFinder.findAllAdmittingDepartments();
	for (Map<String, ?> m : admittingDeptMapList) {
	    for (Map.Entry<String, ?> mEntry : m.entrySet()) {
		if ("departmentId".equals(mEntry.getKey())) {
		    admittingDepartments.add((AdmittingDepartment) crudDao.find(AdmittingDepartment.class, (Long) mEntry.getValue()));
		}
	    }
	}
    }

    public List<Map<String, ?>> getMaps() {
	return maps;
    }

    @Command
    public void criteria(@BindingParam("content") String criteria) {
	this.criteria = criteria;
    }

    @Command
    @NotifyChange({ "maps" })
    public void search() {

	totalCount = Long.valueOf("0");
	
	if (selectedDepartment == null)
	    return;

	if ("10".equals(criteriaListBox.getSelectedItem().getValue())) {
	    maps = patientAdmissionFinder.findDischargeDiagnosisAndDepartmentsByDischargeDateByLimit(null, null, selectedDepartment.getId(), 0, 10,isADoctor.getDoctorId());
	}
	if ("20".equals(criteriaListBox.getSelectedItem().getValue())) {
	    maps = patientAdmissionFinder.findDischargeDiagnosisAndDepartmentsByDischargeDateByLimit(null, null, selectedDepartment.getId(), 0, 20,isADoctor.getDoctorId());
	}
	if ("100".equals(criteriaListBox.getSelectedItem().getValue())) {
	    maps = patientAdmissionFinder.findDischargeDiagnosisAndDepartmentsByDischargeDateByLimit(null, null, selectedDepartment.getId(), 0, 100,isADoctor.getDoctorId());
	}
	if ("ALL".equals(criteriaListBox.getSelectedItem().getValue())) {
	    maps = patientAdmissionFinder.findDischargeDiagnosisAndDepartmentsByDischargeDateByLimit(null, null, selectedDepartment.getId(), 0, 100000,isADoctor.getDoctorId());
	}
	
	for (Map<String, ?> map : patientAdmissionFinder.findDischargeDiagnosisAndDepartmentsByDischargeDateByLimit(null, null, selectedDepartment.getId().longValue(), 0, 100000,isADoctor.getDoctorId())) {
	    totalCount = (Long) (map.get("CASECOUNT")) + totalCount;
	}

    }

    public PatientAdmissionFinder getPatientAdmissionFinder() {
	return patientAdmissionFinder;
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
		patientAdmissionCommand.admittingDepartmentId = selectedDepartment.getId();
	    }
	});
	if (combobox.getSelectedItem() == null)
	    return;
	selectedDepartment = ((Combobox) ctx.getComponent()).getSelectedItem().getValue();
	if (selectedDepartment != null && selectedDepartment.getId() == null) {
	    Component targetComp = ctx.getComponent();
	    Map viewArgs = new HashMap();
	    viewArgs.put("targetComp", targetComp);
	    viewArgs.put("masterList", admittingDepartments);

	} else
	    patientAdmissionCommand.admittingDepartmentId = selectedDepartment.getId();

    }
    
    @Command
    public void TotalPercentage(@BindingParam("caseCount") Float count, @BindingParam("label") Label label) {
	NumberFormat nf = NumberFormat.getInstance();
	nf.setMaximumFractionDigits(2);
	Float percentage = null;
	if (count != null && totalCount != null) {
	    percentage = ((count / totalCount) * 100);
	    label.setValue(nf.format(percentage.doubleValue()) + " % ");
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

    public Date getDischargeDate() {
	return dischargeDate;
    }

    public void setDischargeDate(Date dischargeDate) {
	this.dischargeDate = dischargeDate;
    }

    public Date getDischargeDateThru() {
	return dischargeDateThru;
    }

    public void setDischargeDateThru(Date dischargeDateThru) {
	this.dischargeDateThru = dischargeDateThru;
    }

}
