package com.patientadmission.domain;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.Setter;
import org.nthdimenzion.crud.ICrudEntity;
import org.nthdimenzion.ddd.domain.IdGeneratingArcheType;

import javax.persistence.*;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Nthdimenzion
 * Date: 20/1/13
 * Time: 11:50 AM
 */
@Entity
public class Doctor extends IdGeneratingArcheType implements ICrudEntity {

    private String firstName;
    private String middleName;
    private String lastName;
    private String contactNumber;
    private String additionalPassword;
    private String gender;
    private String emailId;
    Set<AdmittingDepartment> admittingDepartments = Sets.newHashSet();
    public Doctor() {
    }

    public Doctor(String firstName, String middleName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
    }

    public Doctor(Person person) {
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

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public static Doctor createDoctorWithName(String firstName, String middleName, String lastName) {
        Doctor doctor = new Doctor();
        doctor.firstName = firstName;
        doctor.middleName = middleName;
        doctor.lastName = lastName;
        return doctor;
    }

    public Doctor withContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
        return this;
    }

    @Transient
    public String getName() {
        if(firstName==null&&lastName==null)return "";
        StringBuilder name = new StringBuilder(firstName);
        name.append("  ").append(lastName);
        return name.toString();
    }

    public String getAdditionalPassword() {
        return additionalPassword;
    }

    public void setAdditionalPassword(String additionalPassword) {
        this.additionalPassword = additionalPassword;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "DOCTOR_ADMITTING_DEPARTMENT", joinColumns = {@JoinColumn(name = "DOCTOR_ID")}, inverseJoinColumns = {@JoinColumn(name = "ADMITTING_DEPARTMENT_ID")})
    public Set<AdmittingDepartment> getAdmittingDepartments() {
        return admittingDepartments;
    }

    public void setAdmittingDepartments(Set<AdmittingDepartment> admittingDepartments) {
        this.admittingDepartments = admittingDepartments;
    }

    @Transient
    public Set<String> getAdmittingDepartmentName() {
        Set<String> departments = Sets.newHashSet();
        for(AdmittingDepartment department : this.getAdmittingDepartments()){
            Map<String, String> map = Maps.newLinkedHashMap();
            departments.add(department.getDepartmentName());
        }
        return departments;
    }
}
