package com.patientadmission.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.nthdimenzion.crud.ICrudEntity;
import org.nthdimenzion.ddd.domain.IdGeneratingArcheType;
import org.nthdimenzion.ddd.domain.annotations.Role;

import com.master.domain.Enumeration;

@Role
@Entity
public class Patient extends IdGeneratingArcheType implements ICrudEntity{

    private String guardianName;
    private Enumeration guardianRelationShip;
    private String medicalRecordNumber;
    private Address address;
    private Set<AdmissionDetails> admissionDetails;
    
    private Enumeration title;
    private String firstName;
    private String middleName;
    private String lastName;
    private String gender;
    private DateTime dob;
    private String contactNumber;
    private Boolean calculatedAge = false;


    Patient() {
    this.medicalRecordNumber = "";
    }

    public Patient withGuardianDetails(final String guardianName,final Enumeration guardianRelationShip,
                   final String medicalRecordNumber) {
        this.guardianName = guardianName;
        this.guardianRelationShip = guardianRelationShip;
        this.medicalRecordNumber = medicalRecordNumber;
        return this;
    }
    
    public void updatePatient(String guardianName,Enumeration guardianRelationShip,
             String medicalRecordNumber){
    	this.guardianName = guardianName;
    	this.guardianRelationShip = guardianRelationShip;
    	this.medicalRecordNumber = medicalRecordNumber;
    }

    String getGuardianName() {
        return guardianName;
    }

    void setGuardianName(String guardianName) {
        this.guardianName = guardianName;
    }

    @ManyToOne
    Enumeration getGuardianRelationShip() {
        return guardianRelationShip;
    }

    void setGuardianRelationShip(Enumeration guardianRelationShip) {
        this.guardianRelationShip = guardianRelationShip;
    }

    @Embedded
   public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Patient livingAt(Address address){
        setAddress(address);
        return this;
    }

    @Column(unique = true)
    String getMedicalRecordNumber() {
        return medicalRecordNumber;
    }

    void setMedicalRecordNumber(String medicalRecordNumber) {
        this.medicalRecordNumber = medicalRecordNumber;
    }

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "PATIENT_ID")
    Set<AdmissionDetails> getAdmissionDetails() {
        return admissionDetails;
    }

    void setAdmissionDetails(Set<AdmissionDetails> admissionDetails) {
        this.admissionDetails = admissionDetails;
    }

    public Patient addAdmissionDetail(AdmissionDetails admissionDetails){
        this.admissionDetails.add(admissionDetails);
        return this;
    }
    
    @ManyToOne
    Enumeration getTitle() {
        return title;
    }

    void setTitle(Enumeration title) {
        this.title = title;
    }

    String getFirstName() {
        return firstName;
    }

    void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    String getMiddleName() {
        return middleName;
    }

    void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    String getLastName() {
        return lastName;
    }

    void setLastName(String lastName) {
        this.lastName = lastName;
    }

    String getGender() {
        return gender;
    }

    void setGender(String gender) {
        this.gender = gender;
    }

    @Column
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    DateTime getDob() {
        return dob;
    }

    void setDob(DateTime dob) {
        this.dob = dob;
    }

    String getContactNumber() {
        return contactNumber;
    }

    void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
    
    public static Patient createPatientWithName(Enumeration title,String firstName,String middleName,String lastName){
        Patient patient = new Patient();
        patient.title = title;
        patient.firstName = firstName;
        patient.middleName = middleName;
        patient.lastName = lastName;
        return patient;
    }

    public Patient withGender(String gender){
        this.gender = gender;
        return this;
    }

    public Patient bornOn(DateTime dob){
        this.dob = dob;
        return this;
    }

    public Patient withContactNumber(String contactNumber){
        this.contactNumber = contactNumber;
        return this;
    }

    public Patient updatePatientWithName(Patient patient, Enumeration title,String firstName,String middleName,String lastName){
        patient.title = title;
        patient.firstName = firstName;
        patient.middleName = middleName;
        patient.lastName = lastName;
        return patient;
    }

    public Patient updateWithGender(Patient patient,String gender){
        patient.gender = gender;
        return patient;
    }

    public Patient updateBornOn(Patient patient,DateTime dob){
        patient.dob = dob;
        return patient;
    }

    public Patient updateWithContactNumber(Patient patient,String contactNumber){
        patient.contactNumber = contactNumber;
        return patient;
    }
    
    public Patient updateWithGuardianDetails(Patient patient, final String guardianName,final Enumeration guardianRelationShip,
            final String medicalRecordNumber) {
		 this.guardianName = guardianName;
		 this.guardianRelationShip = guardianRelationShip;
		 this.medicalRecordNumber = medicalRecordNumber;
		 return patient;
    	}

	public Boolean getCalculatedAge() {
		return calculatedAge;
	}

	public void setCalculatedAge(Boolean calculatedAge) {
		this.calculatedAge = calculatedAge;
	}
}