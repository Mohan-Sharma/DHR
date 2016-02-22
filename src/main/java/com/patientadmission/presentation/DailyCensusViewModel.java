package com.patientadmission.presentation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.nthdimenzion.crud.ICrud;
import org.nthdimenzion.object.utils.UtilDateTime;
import org.nthdimenzion.object.utils.UtilValidator;
import org.nthdimenzion.presentation.annotations.Composer;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import com.google.common.collect.Maps;
import com.patientadmission.presentation.queries.PatientAdmissionFinder;

@Composer
@VariableResolver(DelegatingVariableResolver.class)
public class DailyCensusViewModel {

    private List<Map<String, ?>> maps;

    @WireVariable
    private PatientAdmissionFinder patientAdmissionFinder;

    private Date admissionDate;

    private Date admissionDateThru;

    @WireVariable
    private ICrud crudDao;

    @Init
    @AfterCompose
    public void init(@ContextParam(ContextType.VIEW) Component view) {
	Selectors.wireComponents(view, this, true);
    }

    public List<Map<String, ?>> getMaps() {
	return maps;
    }

    public void setMaps(List<Map<String, ?>> maps) {
	this.maps = maps;
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

    public PatientAdmissionFinder getPatientAdmissionFinder() {
	return patientAdmissionFinder;
    }

    @Command
    @NotifyChange({ "maps" })
    public void search() {
	if (admissionDate == null) {
	    return;
	}
	Date end = UtilDateTime.getMonthEnd(admissionDate);
	maps = new ArrayList<Map<String, ?>>();
	for (int i = 1; i <= end.getDate(); i++) {
	    Date critera = UtilDateTime.getMonthEnd(admissionDate);
	    critera.setDate(i);
	    List<Map<String, ?>> mapAdmission = new ArrayList<Map<String, ?>>();
	    List<Map<String, ?>> mapDischarge = new ArrayList<Map<String, ?>>();
	    mapAdmission = patientAdmissionFinder.searchPatientAdmissionByDates(UtilDateTime.getDayStart(critera), UtilDateTime.getDayEnd(critera), null, null);
	    mapDischarge = patientAdmissionFinder.searchPatientAdmissionByDates(null, null, UtilDateTime.getDayStart(critera), UtilDateTime.getDayEnd(critera));
	    Map<String, Object> map2 = Maps.newHashMap();
	    map2.put("DAY", i);
	    map2.put("ADMISSION", mapAdmission.get(0).get("CASECOUNT"));
	    map2.put("DISCHARGE", mapDischarge.get(0).get("CASECOUNT"));
	    map2.put("TOTAL", ((Long) mapDischarge.get(0).get("CASECOUNT")) + ((Long) mapAdmission.get(0).get("CASECOUNT")));
	    maps.add(map2);
	}
	end = null;

    }

}
