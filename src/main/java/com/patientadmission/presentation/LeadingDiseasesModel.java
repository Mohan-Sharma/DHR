package com.patientadmission.presentation;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.nthdimenzion.crud.ICrud;
import org.nthdimenzion.object.utils.UtilDateTime;
import org.nthdimenzion.presentation.annotations.Composer;
import org.nthdimenzion.presentation.infrastructure.AbstractZKModel;
import org.nthdimenzion.security.domain.IUserLoginRepository;
import org.nthdimenzion.security.domain.IsADoctor;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;

import com.google.common.collect.Sets;
import com.patientadmission.domain.AdmittingDepartment;
import com.patientadmission.presentation.queries.PatientAdmissionFinder;

@Composer
@VariableResolver(DelegatingVariableResolver.class)
public class LeadingDiseasesModel extends AbstractZKModel {

    private List<Map<String, ?>> maps = new ArrayList<Map<String, ?>>();
    private Long totalCount;
    @WireVariable
    private PatientAdmissionFinder patientAdmissionFinder;

    @WireVariable
    private ICrud crudDao;

	int coun = 0; 
    private Date dischargeDate;

    private Date dischargeDateThru;

    private String criteria;

    @Wire("#criteriaListBox")
    private Listbox criteriaListBox;

    @Wire("#criteriaGrid")
    private Grid criteriaGrid;
    
    private int count = 5;
    
	@WireVariable
    private IUserLoginRepository userLoginRepository;
	private IsADoctor isADoctor;

    @Init
    @AfterCompose
    public void init(@ContextParam(ContextType.VIEW) Component view) {
	Selectors.wireComponents(view, this, true);
	isADoctor = userLoginRepository.findUserLoginWithUserName(loggedInUser.getUsername());
    }

    @Command
    public void criteria(@BindingParam("content") String criteria) {
	this.criteria = criteria;
    }

    public List<Map<String, ?>> getMaps() {
	return maps;
    }

    public void setMaps(List<Map<String, ?>> maps) {
	this.maps = maps;
    }

    @Command
    @NotifyChange({ "maps" })
    public void search() {
	totalCount = Long.valueOf("0");
	int temp = 0;


	if ("10".equals(criteriaListBox.getSelectedItem().getValue())) {
	    maps = patientAdmissionFinder.findDischargeDiagnosisByDischargeDateByLimit(dischargeDate != null ? UtilDateTime.getDayStart(dischargeDate) : null,
		    dischargeDateThru != null ? UtilDateTime.getDayEnd(dischargeDateThru) : null, 0, 10,isADoctor.getDoctorId());
	}
	if ("20".equals(criteriaListBox.getSelectedItem().getValue())) {
	    maps = patientAdmissionFinder.findDischargeDiagnosisByDischargeDateByLimit(dischargeDate != null ? UtilDateTime.getDayStart(dischargeDate) : null,
		    dischargeDateThru != null ? UtilDateTime.getDayEnd(dischargeDateThru) : null, 0, 20,isADoctor.getDoctorId());
	}
	if ("100".equals(criteriaListBox.getSelectedItem().getValue())) {
	    maps = patientAdmissionFinder.findDischargeDiagnosisByDischargeDateByLimit(dischargeDate != null ? UtilDateTime.getDayStart(dischargeDate) : null,
		    dischargeDateThru != null ? UtilDateTime.getDayEnd(dischargeDateThru) : null, 0, 100,isADoctor.getDoctorId());
    
	}

	if ("ALL".equals(criteriaListBox.getSelectedItem().getValue())) {
	    maps = patientAdmissionFinder.findDischargeDiagnosisByDischargeDate(dischargeDate != null ? UtilDateTime.getDayStart(dischargeDate) : null,
		    dischargeDateThru != null ? UtilDateTime.getDayEnd(dischargeDateThru) : null,isADoctor.getDoctorId() );
	}

	for (Map<String, ?> map : patientAdmissionFinder.findDischargeDiagnosisByDischargeDate(dischargeDate != null ? UtilDateTime.getDayStart(dischargeDate) : null,
		dischargeDateThru != null ? UtilDateTime.getDayEnd(dischargeDateThru) : null,isADoctor.getDoctorId())) {

	    totalCount = (Long) (map.get("CASECOUNT")) + totalCount;
	}
	count +=count;
	
    }
  /*  @Command
    @NotifyChange({ "maps" })
    public void Next(){
    	    maps = patientAdmissionFinder.findDischargeDiagnosisByDischargeDateByLimit(dischargeDate != null ? UtilDateTime.getDayStart(dischargeDate) : null,
    		    dischargeDateThru != null ? UtilDateTime.getDayEnd(dischargeDateThru) : null, coun, 5);
    	    coun= coun+5;
    }
*/
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
}
