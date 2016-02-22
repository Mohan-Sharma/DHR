package com.patientadmission.command;

import org.nthdimenzion.cqrs.command.ICommand;
import org.nthdimenzion.cqrs.command.annotations.Command;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@Command
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class RegisterPatientCommand implements ICommand {

    public String titleCode;

    public String firstName;

    public String middleName;

    public String lastName;

    public String gender;

    public Date dateOfBirth;

    public String guardianName;

    public String guardianRelationShipCode;

    public String addressLine1;

    public String addressLine2;

    public String addressLine3;

    public Long countryId;

    public Long stateId;

    public String city;

    public String phoneNumber;

    public String medicalRecordNumber;

    public Long patientId;
    
    public boolean calculatedAge;

    public RegisterPatientCommand(String firstName,
                                  String middleName, String lastName,
                                  Date dateOfBirth, String guardianName, String addressLine1,
                                  String addressLine2, String addressLine3, String city, String phoneNumber,
                                  String medicalRecordNumber) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.guardianName = guardianName;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.addressLine3 = addressLine3;
        this.city = city;
        this.phoneNumber = phoneNumber;
        this.medicalRecordNumber = medicalRecordNumber;
    }

    public RegisterPatientCommand() {
    }

    public String getTitleCode() {
        return titleCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public String getGuardianName() {
        return guardianName;
    }

    public String getGuardianRelationShipCode() {
        return guardianRelationShipCode;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public String getAddressLine3() {
        return addressLine3;
    }

    public Long getCountryId() {
        return countryId;
    }

    public Long getStateId() {
        return stateId;
    }

    public String getCity() {
        return city;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getMedicalRecordNumber() {
        return medicalRecordNumber;
    }

    public void setTitleCode(String titleCode) {
        this.titleCode = titleCode;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setGuardianName(String guardianName) {
        this.guardianName = guardianName;
    }

    public void setGuardianRelationShipCode(String guardianRelationShipCode) {
        this.guardianRelationShipCode = guardianRelationShipCode;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public void setAddressLine3(String addressLine3) {
        this.addressLine3 = addressLine3;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setMedicalRecordNumber(String medicalRecordNumber) {
        this.medicalRecordNumber = medicalRecordNumber;
    }

    public boolean isCalculatedAge() {
		return calculatedAge;
	}

	public void setCalculatedAge(boolean calculatedAge) {
		this.calculatedAge = calculatedAge;
	}

	@Override
    public void validate() {

    }
}
