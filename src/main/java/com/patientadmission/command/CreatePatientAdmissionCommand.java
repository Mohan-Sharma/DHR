package com.patientadmission.command;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.Setter;
import org.nthdimenzion.cqrs.command.ICommand;
import org.nthdimenzion.cqrs.command.annotations.Command;

@Command
public class CreatePatientAdmissionCommand implements ICommand {
	
	public Date admissionDateAndTime;

	public Date dischargedDateAndTime;

	public Long admittingDepartmentId;

	public Long admittingDoctorId;

    public Long admittingWardId;

	public Long admittingBedId;

	public Long dischargeWardId;

    public Long dischargeBedId;

	public String categoryCode;

	public String mrnNumber;

	public String inPatientNumber;

	public String fileName;

	public String filePath;

	public String contentType;
	
	public Long admissionId;

    @Getter
    @Setter
    public String mlcNumber;

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

    public Long getAdmissionId() {
        return admissionId;
    }

    public void setAdmissionId(Long admissionId) {
        this.admissionId = admissionId;
    }

    @Override
	public void validate() {
        Preconditions.checkNotNull(admittingDoctorId, "Doctor must be selected");
        if(admissionDateAndTime!=null && dischargedDateAndTime!=null){
            Preconditions.checkState(dischargedDateAndTime.after(admissionDateAndTime),
                    "Discharge Date should be after the Admission Date");
        }

	}

    @Override
    public String toString() {
        return "CreatePatientAdmissionCommand{" +
                "admissionDateAndTime=" + admissionDateAndTime +
                ", dischargedDateAndTime=" + dischargedDateAndTime +
                ", admittingDepartmentId=" + admittingDepartmentId +
                ", admittingDoctorId=" + admittingDoctorId +
                ", admittingWardId=" + admittingWardId +
                ", admittingBedId=" + admittingBedId +
                ", dischargeWardId=" + dischargeWardId +
                ", dischargeBedId=" + dischargeBedId +
                ", categoryCode='" + categoryCode + '\'' +
                ", mrnNumber='" + mrnNumber + '\'' +
                ", inPatientNumber='" + inPatientNumber + '\'' +
                ", fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", contentType='" + contentType + '\'' +
                ", mlcNumber='" + mlcNumber + '\'' +
                ", admissionId=" + admissionId +
                '}';
    }
}
