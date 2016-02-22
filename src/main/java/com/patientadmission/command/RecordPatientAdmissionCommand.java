package com.patientadmission.command;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.patientadmission.domain.AdmittingDepartment;
import org.nthdimenzion.cqrs.command.ICommand;
import org.nthdimenzion.cqrs.command.annotations.Command;

@Command
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class RecordPatientAdmissionCommand implements ICommand {

	public Date admissionDateAndTime;

	public Date dischargedDateAndTime;

	public Long admittingDepartmentId;

	public Long admittingDoctorId;

	public Long admittingWardId;

	public Long admittingBedId;

	public Long dischargeWardId;

	public Long dischargeBedId;

	public String categoryCode;

	public PatientClinicalDetails patientClinicalDetails = new PatientClinicalDetails();

	public String mrnNumber;

	public String inPatientNumber;

	public String fileName;

	public String filePath;

	public String contentType;
	
	public Long admissionId;
	
	public String firstName;
	
	public String middleName;
	
	public String lastName;
	
	public Date getAdmissionDateAndTime() {
		return admissionDateAndTime;
	}

	public void setAdmissionDateAndTime(Date admissionDateAndTime) {
		this.admissionDateAndTime = admissionDateAndTime;
	}

	public Date getDischargedDateAndTime() {
		return dischargedDateAndTime;
	}

	public void setDischargedDateAndTime(Date dischargedDateAndTime) {
		this.dischargedDateAndTime = dischargedDateAndTime;
	}

	public Long getAdmittingDepartmentId() {
		return admittingDepartmentId;
	}

	public void setAdmittingDepartmentId(Long admittingDepartmentId) {
		this.admittingDepartmentId = admittingDepartmentId;
	}

    public void setSelectedDepartment(AdmittingDepartment admittingDepartment) {
        this.admittingDepartmentId = admittingDepartment.getId();
    }

	public Long getAdmittingDoctorId() {
		return admittingDoctorId;
	}

	public void setAdmittingDoctorId(Long admittingDoctorId) {
		this.admittingDoctorId = admittingDoctorId;
	}

	public Long getAdmittingWardId() {
		return admittingWardId;
	}

	public void setAdmittingWardId(Long admittingWardId) {
		this.admittingWardId = admittingWardId;
	}

	public Long getAdmittingBedId() {
		return admittingBedId;
	}

	public void setAdmittingBedId(Long admittingBedId) {
		this.admittingBedId = admittingBedId;
	}

	public Long getDischargeWardId() {
		return dischargeWardId;
	}

	public void setDischargeWardId(Long dischargeWardId) {
		this.dischargeWardId = dischargeWardId;
	}

	public Long getDischargeBedId() {
		return dischargeBedId;
	}

	public void setDischargeBedId(Long dischargeBedId) {
		this.dischargeBedId = dischargeBedId;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public PatientClinicalDetails getPatientClinicalDetails() {
		return patientClinicalDetails;
	}

	public void setPatientClinicalDetails(
			PatientClinicalDetails patientClinicalDetails) {
		this.patientClinicalDetails = patientClinicalDetails;
	}

	public String getMrnNumber() {
		return mrnNumber;
	}

	public void setMrnNumber(String mrnNumber) {
		this.mrnNumber = mrnNumber;
	}

	public String getInPatientNumber() {
		return inPatientNumber;
	}

	public void setInPatientNumber(String inPatientNumber) {
		this.inPatientNumber = inPatientNumber;
	}

	@Override
	public void validate() {
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public String toString() {
		return "RecordPatientAdmissionCommand [admissionDateAndTime="
				+ admissionDateAndTime + ", admittingBedId=" + admittingBedId
				+ ", admittingDepartmentId=" + admittingDepartmentId
				+ ", admittingDoctorId=" + admittingDoctorId
				+ ", admittingWardId=" + admittingWardId + ", categoryCode="
				+ categoryCode + ", dischargeBedId=" + dischargeBedId
				+ ", dischargeWardId=" + dischargeWardId
				+ ", dischargedDateAndTime=" + dischargedDateAndTime
				+ ", inPatientNumber=" + inPatientNumber + ", mrnNumber="
				+ mrnNumber + ", patientClinicalDetails="
				+ patientClinicalDetails + "]";
	}
}
