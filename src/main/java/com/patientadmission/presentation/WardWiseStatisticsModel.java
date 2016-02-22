package com.patientadmission.presentation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.nthdimenzion.crud.ICrud;
import org.nthdimenzion.object.utils.UtilDateTime;
import org.nthdimenzion.presentation.annotations.Composer;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import com.patientadmission.presentation.queries.PatientAdmissionFinder;

@Composer
@VariableResolver(DelegatingVariableResolver.class)
public class WardWiseStatisticsModel {
	
	private List<Map<String, ?>> maps = new ArrayList<Map<String, ?>>();

	@WireVariable
	private PatientAdmissionFinder patientAdmissionFinder;

	@WireVariable
	private ICrud crudDao;

	private Date dischargeDate;

	private Date dischargeDateThru;

	public List<Map<String, ?>> getMaps() {
		return maps;
	}

	public void setMaps(List<Map<String, ?>> maps) {
		this.maps = maps;
	}

	@Command
	@NotifyChange({ "maps" })
	public void search() {
		maps = patientAdmissionFinder.findWardByDischargeDate(
				dischargeDate != null ? UtilDateTime.getDayStart(dischargeDate)
						: null,
				dischargeDateThru != null ? UtilDateTime
						.getDayEnd(dischargeDateThru) : null);
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
