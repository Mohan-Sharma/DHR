package com.patientadmission.presentation;

import com.patientadmission.presentation.queries.PatientAdmissionFinder;
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
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Label;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Composer
@VariableResolver(DelegatingVariableResolver.class)
public class DiseaseWiseStatisticsViewModel extends AbstractZKModel {
	private List<Map<String, ?>> maps = new ArrayList<Map<String,?>>();

	@WireVariable
	private PatientAdmissionFinder patientAdmissionFinder;

	@WireVariable
	private ICrud crudDao;

	private Date dischargeDate;

	private Date dischargeDateThru;

	@WireVariable
	private IUserLoginRepository userLoginRepository;
	private IsADoctor isADoctor;



	@Init
	@AfterCompose
	public void init(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, true);
		isADoctor = userLoginRepository.findUserLoginWithUserName(loggedInUser.getUsername());
	}

	public List<Map<String, ?>> getMaps() {
		return maps;
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

	public void setMaps(List<Map<String, ?>> maps) {
		this.maps = maps;
	}



	@Command
	@NotifyChange({"maps","patientCountLabel","patientMrnNumberCount"})
	public void search(){
		maps = 
				patientAdmissionFinder.findDischargeDiagnosisByDischargeDate(dischargeDate != null ? UtilDateTime.getDayStart(dischargeDate): null ,
						dischargeDateThru != null ? UtilDateTime.getDayEnd(dischargeDateThru): null,isADoctor.getDoctorId());
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
	public void calulatePatientCount(@BindingParam("content")Map<String,?> map,@BindingParam("label")Label label){
		List<Map<String, ?>> list = patientAdmissionFinder.findDischargeDiagnosisPatientIdsByICDElementId((String)map.get("ID"),dischargeDate != null ? UtilDateTime.getDayStart(dischargeDate): null ,
				dischargeDateThru != null ? UtilDateTime.getDayEnd(dischargeDateThru): null);
		label.setValue(((Integer)list.size()).toString());
	}

}
