package com.patientadmission.command;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.StringUtils;
import org.nthdimenzion.cqrs.command.ICommand;
import org.nthdimenzion.cqrs.command.annotations.Command;

import com.google.common.collect.Lists;

@Command
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PatientClinicalDetails implements ICommand {

	public List<String> indicationForAdmissionIds = new ArrayList<String>();

	public String referredFromId = StringUtils.EMPTY;

	public String hospitalUnitId = StringUtils.EMPTY;

	public String clinicalUnitId = StringUtils.EMPTY;

	public String conditionAtDischargeId = StringUtils.EMPTY;

	public String dischargeDestinationId = StringUtils.EMPTY;

	public String outsideReferral;

	public List<String> selectedAdmittingDiagnosisIds = Lists.newArrayList();

	public List<String> selectedDischargeDiagnosisIds = Lists.newArrayList();

	public List<String> selectedFinalDiagnosisIds = Lists.newArrayList();

	public List<Long> selectedProceduresDoneIds = Lists.newArrayList();

	public String inPatientNumber;
	
	public String referred;

	public List<String> getIndicationForAdmissionIds() {
		return indicationForAdmissionIds;
	}

	public void setIndicationForAdmissionIds(
			List<String> indicationForAdmissionIds) {
		this.indicationForAdmissionIds = indicationForAdmissionIds;
	}

	public String getReferredFromId() {
		return referredFromId;
	}

	public void setReferredFromId(String referredFromId) {
		this.referredFromId = referredFromId;
	}

	public String getHospitalUnitId() {
		return hospitalUnitId;
	}

	public void setHospitalUnitId(String hospitalUnitId) {
		this.hospitalUnitId = hospitalUnitId;
	}

	public String getClinicalUnitId() {
		return clinicalUnitId;
	}

	public void setClinicalUnitId(String clinicalUnitId) {
		this.clinicalUnitId = clinicalUnitId;
	}

	public String getConditionAtDischargeId() {
		return conditionAtDischargeId;
	}

	public void setConditionAtDischargeId(String conditionAtDischargeId) {
		this.conditionAtDischargeId = conditionAtDischargeId;
	}

	public String getDischargeDestinationId() {
		return dischargeDestinationId;
	}

	public void setDischargeDestinationId(String dischargeDestinationId) {
		this.dischargeDestinationId = dischargeDestinationId;
	}

	public String getOutsideReferral() {
		return outsideReferral;
	}

	public void setOutsideReferral(String outsideReferral) {
		this.outsideReferral = outsideReferral;
	}
	
	public List<String> getSelectedAdmittingDiagnosisIds() {
		return selectedAdmittingDiagnosisIds;
	}

	public void setSelectedAdmittingDiagnosisIds(
			List<String> selectedAdmittingDiagnosisIds) {
		this.selectedAdmittingDiagnosisIds = selectedAdmittingDiagnosisIds;
	}

	public List<String> getSelectedDischargeDiagnosisIds() {
		return selectedDischargeDiagnosisIds;
	}

	public void setSelectedDischargeDiagnosisIds(
			List<String> selectedDischargeDiagnosisIds) {
		this.selectedDischargeDiagnosisIds = selectedDischargeDiagnosisIds;
	}

	public List<String> getSelectedFinalDiagnosisIds() {
		return selectedFinalDiagnosisIds;
	}

	public void setSelectedFinalDiagnosisIds(List<String> selectedFinalDiagnosisIds) {
		this.selectedFinalDiagnosisIds = selectedFinalDiagnosisIds;
	}

	public List<Long> getSelectedProceduresDoneIds() {
		return selectedProceduresDoneIds;
	}

	public void setSelectedProceduresDoneIds(
			List<Long> selectedProceduresDoneIds) {
		this.selectedProceduresDoneIds = selectedProceduresDoneIds;
	}

	@Override
	public String toString() {
		return "PatientClinicalDetails [clinicalUnitId=" + clinicalUnitId
				+ ", conditionAtDischargeId=" + conditionAtDischargeId
				+ ", dischargeDestinationId=" + dischargeDestinationId
				+ ", hospitalUnitId=" + hospitalUnitId
				+ ", indicationForAdmissionIds=" + indicationForAdmissionIds
				+ ", outsideReferral=" + outsideReferral + ", procedureId="
				+ ", referredFromId=" + referredFromId + "]";
	}
	
	public String getReferred() {
		return referred;
	}

	public void setReferred(String referred) {
		this.referred = referred;
	}

	@Override
	public void validate() {
		// TODO Auto-generated method stub

	}
}
