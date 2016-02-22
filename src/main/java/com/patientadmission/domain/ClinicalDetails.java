package com.patientadmission.domain;

import com.google.common.collect.Sets;
import com.master.domain.Enumeration;

import org.nthdimenzion.crud.ICrudEntity;
import org.nthdimenzion.ddd.domain.IdGeneratingArcheType;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Nthdimenzion
 * Date: 20/1/13
 * Time: 9:36 PM
 */
@Entity
public class ClinicalDetails extends IdGeneratingArcheType implements ICrudEntity {

    private Set<Enumeration> indicationForAdmission = Sets.newHashSet();
    private Enumeration referredFrom;
    private Enumeration hospitalUnit;
    private Enumeration clinicalUnit;
    private Enumeration conditionAtDischarge;
    private Enumeration dischargeDestination;

    private Set<IcdElement> admittingDiagnosis = Sets.newHashSet();
    private Set<IcdElement> dischargeDiagnosis = Sets.newHashSet();
    private Set<IcdElement> finalDiagnosis = Sets.newHashSet();

    private Set<Procedure> proceduresPerformed = Sets.newHashSet();

    private String outsideReferral;
    
    private String referred;

    @ManyToMany
    public Set<Enumeration> getIndicationForAdmission() {
        return indicationForAdmission;
    }

    public void setIndicationForAdmission(Set<Enumeration> indicationForAdmission) {
        this.indicationForAdmission = indicationForAdmission;
    }

    public ClinicalDetails addIndicationForAdmission(Enumeration indicationForAdmission) {
        this.indicationForAdmission.add(indicationForAdmission);
        return this;
    }

    @ManyToOne
    public Enumeration getReferredFrom() {
        return referredFrom;
    }

    public void setReferredFrom(Enumeration referredFrom) {
        this.referredFrom = referredFrom;
    }

    @ManyToOne
    public Enumeration getHospitalUnit() {
        return hospitalUnit;
    }

    public void setHospitalUnit(Enumeration hospitalUnit) {
        this.hospitalUnit = hospitalUnit;
    }

    @ManyToOne
    public Enumeration getClinicalUnit() {
        return clinicalUnit;
    }

    public void setClinicalUnit(Enumeration clinicalUnit) {
        this.clinicalUnit = clinicalUnit;
    }

    @ManyToOne
    public Enumeration getConditionAtDischarge() {
        return conditionAtDischarge;
    }

    public void setConditionAtDischarge(Enumeration conditionAtDischarge) {
        this.conditionAtDischarge = conditionAtDischarge;
    }

    @ManyToOne
    public Enumeration getDischargeDestination() {
        return dischargeDestination;
    }

    public void setDischargeDestination(Enumeration dischargeDestination) {
        this.dischargeDestination = dischargeDestination;
    }

    @ManyToMany
    public Set<IcdElement> getAdmittingDiagnosis() {
        return admittingDiagnosis;
    }

    public void setAdmittingDiagnosis(Set<IcdElement> admittingDiagnosis) {
        this.admittingDiagnosis = admittingDiagnosis;
    }

    @ManyToMany
    public Set<IcdElement> getDischargeDiagnosis() {
        return dischargeDiagnosis;
    }

    public void setDischargeDiagnosis(Set<IcdElement> dischargeDiagnosis) {
        this.dischargeDiagnosis = dischargeDiagnosis;
    }

    @ManyToMany
    public Set<IcdElement> getFinalDiagnosis() {
        return finalDiagnosis;
    }

    public void setFinalDiagnosis(Set<IcdElement> finalDiagnosis) {
        this.finalDiagnosis = finalDiagnosis;
    }

    @ManyToMany
    public Set<Procedure> getProceduresPerformed() {
        return proceduresPerformed;
    }

    public void setProceduresPerformed(Set<Procedure> proceduresPerformed) {
        this.proceduresPerformed = proceduresPerformed;
    }

    public String getOutsideReferral() {
        return outsideReferral;
    }

    public void setOutsideReferral(String outsideReferral) {
        this.outsideReferral = outsideReferral;
    }

    public ClinicalDetails addAdmittingDiagnosis(Set<IcdElement> admittingDiagnosis) {
        this.admittingDiagnosis.addAll(admittingDiagnosis);
        return this;
    }

    public ClinicalDetails addDischargeDiagnosis(Set<IcdElement> dischargeDiagnosis) {
        this.dischargeDiagnosis.addAll(dischargeDiagnosis);
        return this;
    }

    public ClinicalDetails addProceduresPerformed(Set<Procedure> proceduresPerformed) {
        this.proceduresPerformed.addAll(proceduresPerformed);
        return this;
    }

    public ClinicalDetails addFinalDiagnosis(Set<IcdElement> finalDiagnosis) {
        this.finalDiagnosis.addAll(finalDiagnosis);
        return this;
    }

	public String getReferred() {
		return referred;
	}

	public void setReferred(String referred) {
		this.referred = referred;
	}
}
