package com.patientadmission.domain;

import com.master.domain.Enumeration;
import org.nthdimenzion.crud.ICrudEntity;
import org.nthdimenzion.ddd.domain.IdGeneratingArcheType;
import org.nthdimenzion.ddd.domain.Interval;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Nthdimenzion
 * Date: 20/1/13
 * Time: 9:11 PM
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames ={"inpatientNumber"}))
public class AdmissionDetails extends IdGeneratingArcheType implements ICrudEntity {

    private Interval admissionPeriod;
    private AdmittingDepartment admittingDept;
    private Doctor doctor;
    private Ward admittingWard;
    private Bed admittingBed;
    private Ward dischargeWard;
    private Bed dischargeBed;
    private Enumeration category;
    private String inpatientNumber;
    @Column(name = "MLC_NUMBER")
    public String mlcNumber;
    private ClinicalDetails clinicalDetails;
    private String fileName;
    private String filePath;
    private String contentType;

    public String getMlcNumber() {
        return mlcNumber;
    }

    public void setMlcNumber(String mlcNumber) {
        this.mlcNumber = mlcNumber;
    }

    @Embedded
    public Interval getAdmissionPeriod() {
        return admissionPeriod;
    }

    public void setAdmissionPeriod(Interval admissionPeriod) {
        this.admissionPeriod = admissionPeriod;
    }


    @ManyToOne
    public AdmittingDepartment getAdmittingDept() {
        return admittingDept;
    }

    public void setAdmittingDept(AdmittingDepartment admittingDept) {
        this.admittingDept = admittingDept;
    }

    @ManyToOne(optional = false)
    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    @ManyToOne
    public Ward getAdmittingWard() {
        return admittingWard;
    }

    public void setAdmittingWard(Ward admittingWard) {
        this.admittingWard = admittingWard;
    }

    @ManyToOne
    public Bed getAdmittingBed() {
        return admittingBed;
    }

    public void setAdmittingBed(Bed admittingBed) {
        this.admittingBed = admittingBed;
    }

    @ManyToOne
    public Ward getDischargeWard() {
        return dischargeWard;
    }

    public void setDischargeWard(Ward dischargeWard) {
        this.dischargeWard = dischargeWard;
    }

    @ManyToOne
    public Bed getDischargeBed() {
        return dischargeBed;
    }

    public void setDischargeBed(Bed dischargeBed) {
        this.dischargeBed = dischargeBed;
    }

    @ManyToOne
    public Enumeration getCategory() {
        return category;
    }

    public void setCategory(Enumeration category) {
        this.category = category;
    }

    public String getInpatientNumber() {
        return inpatientNumber;
    }

    public void setInpatientNumber(String inpatientNumber) {
        this.inpatientNumber = inpatientNumber;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "CLINICAL_DETAILS_ID")
    public ClinicalDetails getClinicalDetails() {
        return clinicalDetails;
    }

    public void setClinicalDetails(ClinicalDetails clinicalDetails) {
        this.clinicalDetails = clinicalDetails;
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
