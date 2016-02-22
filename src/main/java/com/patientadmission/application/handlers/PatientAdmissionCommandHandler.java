package com.patientadmission.application.handlers;

import com.google.common.collect.Sets;
import com.master.domain.Enumeration;
import com.master.services.IMasterService;
import com.patientadmission.command.*;
import com.patientadmission.domain.*;
import com.patientadmission.domain.error.InPatientNumberAlreadyExistsException;
import com.patientadmission.presentation.queries.PatientAdmissionFinder;
import org.hibernate.exception.ConstraintViolationException;
import org.joda.time.DateTime;
import org.nthdimenzion.cqrs.command.AbstractCommandHandler;
import org.nthdimenzion.cqrs.command.annotations.CommandHandler;
import org.nthdimenzion.crud.ICrud;
import org.nthdimenzion.ddd.domain.Interval;
import org.nthdimenzion.ddd.infrastructure.exception.ErrorDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Nthdimenzion
 * Date: 19/1/13
 * Time: 11:06 PM
 */
@CommandHandler
public class PatientAdmissionCommandHandler extends AbstractCommandHandler {

    @Autowired
    private IMasterService masterService;

    @Autowired
    private ICrud crudDao;

    @Autowired
    private PatientAdmissionFinder patientAdmissionFinder;

    private Boolean updatePatient(RegisterPatientCommand registerPatientCommand){
    	
    	Patient patient = crudDao.find(Patient.class, registerPatientCommand.patientId);
    	
        Enumeration title = masterService.getEnumerationByEnumCode(registerPatientCommand.titleCode);
        Patient updatedPatient = patient.updatePatientWithName(patient, title, registerPatientCommand.firstName,
                registerPatientCommand.middleName, registerPatientCommand.lastName);
        updatedPatient = updatedPatient.updateWithGender(updatedPatient, registerPatientCommand.gender);
        if(registerPatientCommand.dateOfBirth != null)
        	updatedPatient = updatedPatient.updateBornOn(updatedPatient, new DateTime(registerPatientCommand.dateOfBirth));
        updatedPatient = updatedPatient.updateWithContactNumber(updatedPatient, registerPatientCommand
                        .phoneNumber);

        Enumeration guardianRelationShip = masterService.getEnumerationByEnumCode(registerPatientCommand
                .guardianRelationShipCode);

      updatedPatient =  updatedPatient.updateWithGuardianDetails(updatedPatient,registerPatientCommand.guardianName, guardianRelationShip, registerPatientCommand.medicalRecordNumber);
        Country country = crudDao.find(Country.class, registerPatientCommand.countryId);
        State state = crudDao.find(State.class, registerPatientCommand.stateId);

        Address patientAddress = updatedPatient.getAddress();
        patientAddress.updateAddress(patientAddress,registerPatientCommand.addressLine1, registerPatientCommand.addressLine2,
                registerPatientCommand.addressLine3, country, state, registerPatientCommand.city);
        updatedPatient = updatedPatient.livingAt(patientAddress);
        updatedPatient.setCalculatedAge(registerPatientCommand.calculatedAge);
        crudDao.save(updatedPatient);
        return success;
    }


    @Transactional
    public Boolean registerPatient(RegisterPatientCommand registerPatientCommand) {
    	if(registerPatientCommand.patientId != null)
    		return updatePatient(registerPatientCommand);
        Enumeration title = masterService.getEnumerationByEnumCode(registerPatientCommand.titleCode);

        Patient newPatient = Patient.createPatientWithName(title, registerPatientCommand.firstName,
                registerPatientCommand.middleName, registerPatientCommand.lastName).withGender(registerPatientCommand
                .gender).withContactNumber
                (registerPatientCommand
                        .phoneNumber);
        if(registerPatientCommand.dateOfBirth != null)
        	newPatient = newPatient.bornOn(new DateTime(registerPatientCommand.dateOfBirth));
        
        Enumeration guardianRelationShip = masterService.getEnumerationByEnumCode(registerPatientCommand
                .guardianRelationShipCode);

      newPatient =  newPatient.withGuardianDetails(registerPatientCommand.guardianName, guardianRelationShip, registerPatientCommand.medicalRecordNumber);
        Country country = crudDao.find(Country.class, registerPatientCommand.countryId);
        State state = crudDao.find(State.class, registerPatientCommand.stateId);

        Address patientAddress = new Address(registerPatientCommand.addressLine1, registerPatientCommand.addressLine2,
                registerPatientCommand.addressLine3, country, state, registerPatientCommand.city);
        newPatient = newPatient.livingAt(patientAddress);
        newPatient.setCalculatedAge(registerPatientCommand.calculatedAge);

        crudDao.save(newPatient);
        return success;
    }



    @Transactional
    public Boolean registerDoctor(RegisterDoctorCommand registerDoctorCommand) {
    	Doctor doctor = Doctor.createDoctorWithName(registerDoctorCommand.firstName,
                registerDoctorCommand.middleName, registerDoctorCommand.lastName).withContactNumber
                (registerDoctorCommand.contactNumber);
        doctor = crudDao.save(doctor);
        return success;
    }

//    @Transactional
//    public Boolean recordPatientAdmissionDetails(RecordPatientAdmissionCommand recordPatientAdmissionCommand) {
//        Map<String, ?> patientDetails = patientAdmissionFinder.getPatientByMRN(recordPatientAdmissionCommand.mrnNumber);
//        Patient patient = crudDao.find(Patient.class, (Serializable) patientDetails.get("patientId"));
//        Interval admissionPeriod = new Interval(new DateTime(recordPatientAdmissionCommand.admissionDateAndTime),
//                new DateTime(recordPatientAdmissionCommand.dischargedDateAndTime));
//        Doctor doctor = crudDao.find(Doctor.class, recordPatientAdmissionCommand.admittingDoctorId);
//        Ward admittingWard = crudDao.find(Ward.class, recordPatientAdmissionCommand.admittingWardId);
//        Bed admittingBed = crudDao.find(Bed.class, recordPatientAdmissionCommand.admittingBedId);
//        Ward dischargeWard = crudDao.find(Ward.class, recordPatientAdmissionCommand.dischargeWardId);
//        Bed dischargeBed = crudDao.find(Bed.class, recordPatientAdmissionCommand.dischargeBedId);
//        Enumeration category = masterService.getEnumerationByEnumCode(recordPatientAdmissionCommand.categoryCode);
//        AdmittingDepartment admittingDepartment = crudDao.find(AdmittingDepartment.class,
//                recordPatientAdmissionCommand.admittingDepartmentId);
//
//        AdmissionDetails admissionDetails = new AdmissionDetails();
//        admissionDetails.setInpatientNumber(recordPatientAdmissionCommand.inPatientNumber);
//        admissionDetails.setAdmissionPeriod(admissionPeriod);
//        admissionDetails.setDoctor(doctor);
//        admissionDetails.setAdmittingDept(admittingDepartment);
//        admissionDetails.setAdmittingWard(admittingWard);
//        admissionDetails.setAdmittingBed(admittingBed);
//        admissionDetails.setDischargeWard(dischargeWard);
//        admissionDetails.setDischargeBed(dischargeBed);
//        admissionDetails.setCategory(category);
//        admissionDetails.setInpatientNumber(recordPatientAdmissionCommand.inPatientNumber);
//        admissionDetails.setFilePath(recordPatientAdmissionCommand.filePath);
//        admissionDetails.setFileName(recordPatientAdmissionCommand.fileName);
//        admissionDetails.setContentType(recordPatientAdmissionCommand.contentType);
//        admissionDetails.setClinicalDetails(createPatientClinicalDetails(recordPatientAdmissionCommand.patientClinicalDetails));
//        patient.addAdmissionDetail(admissionDetails);
//        patient = crudDao.save(patient);
//        return success;
//    }

//    private ClinicalDetails createPatientClinicalDetails(PatientClinicalDetails patientClinicalDetails) {
//        ClinicalDetails clinicalDetails = new ClinicalDetails();
//        for (String indicationForAdmissionEnumCode : patientClinicalDetails.indicationForAdmissionIds) {
//            clinicalDetails.addIndicationForAdmission(masterService.getEnumerationByEnumCode
//                    (indicationForAdmissionEnumCode));
//        }
//        Set<IcdElement> admittingIcdElements = Sets.newHashSet();
//        for(Long admittingDiagnosisId : patientClinicalDetails.selectedAdmittingDiagnosisIds){
//        	IcdElement element = crudDao.find(IcdElement.class,admittingDiagnosisId);
//        	admittingIcdElements.add(element);
//        }
//        clinicalDetails.setAdmittingDiagnosis(admittingIcdElements);
//        
//        Set<IcdElement> dischargeIcdElements = Sets.newHashSet();
//        for(Long dischargeDiagnosisId : patientClinicalDetails.selectedDischargeDiagnosisIds){
//        	IcdElement element = crudDao.find(IcdElement.class,dischargeDiagnosisId);
//        	dischargeIcdElements.add(element);
//        }
//        clinicalDetails.setDischargeDiagnosis(dischargeIcdElements);
//        
//        Set<IcdElement> finalIcdElements = Sets.newHashSet();
//        for(Long selectedFinalDiagnosisId : patientClinicalDetails.selectedFinalDiagnosisIds){
//        	IcdElement element = crudDao.find(IcdElement.class,selectedFinalDiagnosisId);
//        	finalIcdElements.add(element);
//        }
//        clinicalDetails.setFinalDiagnosis(finalIcdElements);
//        
//        Set<Procedure> procedures = Sets.newHashSet();
//        for(Long procedureDoneId : patientClinicalDetails.selectedProceduresDoneIds){
//        	Procedure procedure = crudDao.find(Procedure.class, procedureDoneId);
//        	procedures.add(procedure);
//        }
//        clinicalDetails.setProceduresPerformed(procedures);
//        
//        Enumeration referredFrom = masterService.getEnumerationByEnumCode(patientClinicalDetails.referredFromId);
//        Enumeration hospitalUnit = masterService.getEnumerationByEnumCode(patientClinicalDetails.hospitalUnitId);
//        Enumeration clinicalUnit = masterService.getEnumerationByEnumCode(patientClinicalDetails.clinicalUnitId);
//        Enumeration conditionAtDischarge = masterService.getEnumerationByEnumCode(patientClinicalDetails.conditionAtDischargeId);
//        Enumeration dischargeDestination = masterService.getEnumerationByEnumCode(patientClinicalDetails
//                .dischargeDestinationId);
//
//        clinicalDetails.setReferredFrom(referredFrom);
//        clinicalDetails.setHospitalUnit(hospitalUnit);
//        clinicalDetails.setClinicalUnit(clinicalUnit);
//        clinicalDetails.setConditionAtDischarge(conditionAtDischarge);
//        clinicalDetails.setDischargeDestination(dischargeDestination);
//        clinicalDetails.setOutsideReferral(patientClinicalDetails.outsideReferral);
//
//        clinicalDetails.addAdmittingDiagnosis(clinicalDetails.getAdmittingDiagnosis()).addDischargeDiagnosis
//                (clinicalDetails.getDischargeDiagnosis()).addFinalDiagnosis(clinicalDetails.getFinalDiagnosis())
//                .addProceduresPerformed(clinicalDetails.getProceduresPerformed());
//        return clinicalDetails;
//    }

    @Transactional
    public Boolean createPatientAdmissionDetails(CreatePatientAdmissionCommand createPatientAdmissionCommand) throws InPatientNumberAlreadyExistsException {
    	Map<String, ?> patientDetails = patientAdmissionFinder.getPatientByMRN(createPatientAdmissionCommand.mrnNumber);
    	Patient patient = crudDao.find(Patient.class, (Serializable) patientDetails.get("patientId"));
    	if(createPatientAdmissionCommand.admissionId != null){
    		AdmissionDetails admissionDetails = crudDao.find(AdmissionDetails.class, createPatientAdmissionCommand.admissionId);
            if(createPatientAdmissionCommand.admissionDateAndTime!=null && createPatientAdmissionCommand
                    .dischargedDateAndTime!=null){

                Interval admissionPeriod = new Interval(new DateTime(createPatientAdmissionCommand.admissionDateAndTime),
                        new DateTime(createPatientAdmissionCommand.dischargedDateAndTime));
                admissionDetails.setAdmissionPeriod(admissionPeriod);
            }
            if(createPatientAdmissionCommand.admissionDateAndTime != null && createPatientAdmissionCommand.dischargedDateAndTime == null){
                Interval admissionFromDateInterval = new Interval(new DateTime(createPatientAdmissionCommand.admissionDateAndTime));
                admissionDetails.setAdmissionPeriod(admissionFromDateInterval);
            }
            System.out.println("***************"+createPatientAdmissionCommand.admissionDateAndTime+"       "+createPatientAdmissionCommand.dischargedDateAndTime);
            Doctor doctor = crudDao.find(Doctor.class, createPatientAdmissionCommand.admittingDoctorId);
            Ward admittingWard = crudDao.find(Ward.class, createPatientAdmissionCommand.admittingWardId);
            Bed admittingBed = crudDao.find(Bed.class, createPatientAdmissionCommand.admittingBedId);
            Ward dischargeWard = crudDao.find(Ward.class, createPatientAdmissionCommand.dischargeWardId);
            Bed dischargeBed = crudDao.find(Bed.class, createPatientAdmissionCommand.dischargeBedId);
            Enumeration category = masterService.getEnumerationByEnumCode(createPatientAdmissionCommand.categoryCode);
            AdmittingDepartment admittingDepartment = crudDao.find(AdmittingDepartment.class,
                    createPatientAdmissionCommand.admittingDepartmentId);
            admissionDetails.setDoctor(doctor);
            admissionDetails.setAdmittingDept(admittingDepartment);
            admissionDetails.setAdmittingWard(admittingWard);
            admissionDetails.setAdmittingBed(admittingBed);
            admissionDetails.setDischargeWard(dischargeWard);
            admissionDetails.setDischargeBed(dischargeBed);
            admissionDetails.setCategory(category);
            admissionDetails.setInpatientNumber(createPatientAdmissionCommand.inPatientNumber);
            admissionDetails.setFilePath(createPatientAdmissionCommand.filePath);
            admissionDetails.setFileName(createPatientAdmissionCommand.fileName);
            admissionDetails.setContentType(createPatientAdmissionCommand.contentType);
            admissionDetails.setMlcNumber(createPatientAdmissionCommand.mlcNumber);
            patient.addAdmissionDetail(admissionDetails);
            try{
                crudDao.save(patient);
            }catch (ConstraintViolationException constraintViolationException){
                throw new InPatientNumberAlreadyExistsException(new ErrorDetails.Builder("301").build());
            }
            return success;
    	}

        Doctor doctor = crudDao.find(Doctor.class, createPatientAdmissionCommand.admittingDoctorId);
        Ward admittingWard = crudDao.find(Ward.class, createPatientAdmissionCommand.admittingWardId);
        Bed admittingBed = crudDao.find(Bed.class, createPatientAdmissionCommand.admittingBedId);
        Ward dischargeWard = crudDao.find(Ward.class, createPatientAdmissionCommand.dischargeWardId);
        Bed dischargeBed = crudDao.find(Bed.class, createPatientAdmissionCommand.dischargeBedId);
        Enumeration category = masterService.getEnumerationByEnumCode(createPatientAdmissionCommand.categoryCode);
        AdmittingDepartment admittingDepartment = crudDao.find(AdmittingDepartment.class,
                createPatientAdmissionCommand.admittingDepartmentId);

        AdmissionDetails admissionDetails = new AdmissionDetails();
        admissionDetails.setInpatientNumber(createPatientAdmissionCommand.inPatientNumber);
        admissionDetails.setMlcNumber(createPatientAdmissionCommand.mlcNumber);
        if(createPatientAdmissionCommand.admissionDateAndTime!=null && createPatientAdmissionCommand
                .dischargedDateAndTime!=null){
            Interval admissionPeriod = new Interval(new DateTime(createPatientAdmissionCommand.admissionDateAndTime),
                    new DateTime(createPatientAdmissionCommand.dischargedDateAndTime));
            admissionDetails.setAdmissionPeriod(admissionPeriod);
        }
        if(createPatientAdmissionCommand.admissionDateAndTime != null && createPatientAdmissionCommand.dischargedDateAndTime == null){
            Interval admissionFromDateInterval = new Interval(new DateTime(createPatientAdmissionCommand.admissionDateAndTime));
            admissionDetails.setAdmissionPeriod(admissionFromDateInterval);
        }
        admissionDetails.setDoctor(doctor);
        admissionDetails.setAdmittingDept(admittingDepartment);
        admissionDetails.setAdmittingWard(admittingWard);
        admissionDetails.setAdmittingBed(admittingBed);
        admissionDetails.setDischargeWard(dischargeWard);
        admissionDetails.setDischargeBed(dischargeBed);
        admissionDetails.setCategory(category);
        admissionDetails.setInpatientNumber(createPatientAdmissionCommand.inPatientNumber);
        admissionDetails.setFilePath(createPatientAdmissionCommand.filePath);
        admissionDetails.setFileName(createPatientAdmissionCommand.fileName);
        admissionDetails.setContentType(createPatientAdmissionCommand.contentType);
        admissionDetails.setClinicalDetails(new ClinicalDetails());
        patient.addAdmissionDetail(admissionDetails);
        try{
            crudDao.save(admissionDetails);
            crudDao.save(patient);
        }catch (Throwable throwable){
            throw new InPatientNumberAlreadyExistsException(new ErrorDetails.Builder("301").build());
        }
        return success;
    }
    
    @Transactional
    public Boolean createPatientClinicalDetails(PatientClinicalDetails patientClinicalDetails) {
        ClinicalDetails clinicalDetails = masterService.fetchClinicalDetailsByInPatientNumber(patientClinicalDetails.inPatientNumber);
        clinicalDetails.setReferred(patientClinicalDetails.referred);
        for (String indicationForAdmissionEnumCode : patientClinicalDetails.indicationForAdmissionIds) {
            clinicalDetails.addIndicationForAdmission(masterService.getEnumerationByEnumCode
                    (indicationForAdmissionEnumCode));
        }
        Set<IcdElement> admittingIcdElements = Sets.newHashSet();
        for(String admittingDiagnosisId : patientClinicalDetails.selectedAdmittingDiagnosisIds){
        	IcdElement element = crudDao.find(IcdElement.class,admittingDiagnosisId);
        	admittingIcdElements.add(element);
        }
        clinicalDetails.setAdmittingDiagnosis(admittingIcdElements);
        
        Set<IcdElement> dischargeIcdElements = Sets.newHashSet();
        for(String dischargeDiagnosisId : patientClinicalDetails.selectedDischargeDiagnosisIds){
        	IcdElement element = crudDao.find(IcdElement.class,dischargeDiagnosisId);
        	dischargeIcdElements.add(element);
        }
        clinicalDetails.setDischargeDiagnosis(dischargeIcdElements);
        
        Set<IcdElement> finalIcdElements = Sets.newHashSet();
        for(String selectedFinalDiagnosisId : patientClinicalDetails.selectedFinalDiagnosisIds){
        	IcdElement element = crudDao.find(IcdElement.class,selectedFinalDiagnosisId);
        	finalIcdElements.add(element);
        }
        clinicalDetails.setFinalDiagnosis(finalIcdElements);
        
        Set<Procedure> procedures = Sets.newHashSet();
        for(Long procedureDoneId : patientClinicalDetails.selectedProceduresDoneIds){
        	Procedure procedure = crudDao.find(Procedure.class, procedureDoneId);
        	procedures.add(procedure);
        }
        clinicalDetails.setProceduresPerformed(procedures);
        
        Enumeration referredFrom = masterService.getEnumerationByEnumCode(patientClinicalDetails.referredFromId);
        Enumeration hospitalUnit = masterService.getEnumerationByEnumCode(patientClinicalDetails.hospitalUnitId);
        Enumeration clinicalUnit = masterService.getEnumerationByEnumCode(patientClinicalDetails.clinicalUnitId);
        Enumeration conditionAtDischarge = masterService.getEnumerationByEnumCode(patientClinicalDetails.conditionAtDischargeId);
        Enumeration dischargeDestination = masterService.getEnumerationByEnumCode(patientClinicalDetails
                .dischargeDestinationId);

        clinicalDetails.setReferredFrom(referredFrom);
        clinicalDetails.setHospitalUnit(hospitalUnit);
        clinicalDetails.setClinicalUnit(clinicalUnit);
        clinicalDetails.setConditionAtDischarge(conditionAtDischarge);
        clinicalDetails.setDischargeDestination(dischargeDestination);
        clinicalDetails.setOutsideReferral(patientClinicalDetails.outsideReferral);

        clinicalDetails.addAdmittingDiagnosis(clinicalDetails.getAdmittingDiagnosis()).addDischargeDiagnosis
                (clinicalDetails.getDischargeDiagnosis()).addFinalDiagnosis(clinicalDetails.getFinalDiagnosis())
                .addProceduresPerformed(clinicalDetails.getProceduresPerformed());
        crudDao.save(clinicalDetails);
        return success;
    }
    
    @Transactional
    public Boolean removeIndications(RemoveIndicationsCommand removeIndicationsCommand){
    	ClinicalDetails clinicalDetails = masterService.fetchClinicalDetailsByInPatientNumber(removeIndicationsCommand.inPatientNumber);
    	clinicalDetails.getIndicationForAdmission().removeAll(removeIndicationsCommand.indicationsToBeRemoved);
    	crudDao.save(clinicalDetails);
    	return success;
    }

    
    @Transactional
    public Boolean removeAdmissionDiagnosis(RemoveAdmissionDiagnosisCommand removeAdmissionDiagnosisCommand){
    	ClinicalDetails clinicalDetails = masterService.fetchClinicalDetailsByInPatientNumber(removeAdmissionDiagnosisCommand.inPatientNumber);
    	clinicalDetails.getAdmittingDiagnosis().removeAll(removeAdmissionDiagnosisCommand.admissionDiagnosisToBeRemoved);
    	crudDao.save(clinicalDetails);
    	return success;
    }

    @Transactional
    public Boolean removeDischargeDiagnosis(RemoveDischargeDiagnosisCommand removeDischargeDiagnosisCommand){
    	ClinicalDetails clinicalDetails = masterService.fetchClinicalDetailsByInPatientNumber(removeDischargeDiagnosisCommand.inPatientNumber);
    	clinicalDetails.getDischargeDiagnosis().removeAll(removeDischargeDiagnosisCommand.dischargeDiagnosisToBeRemoved);
    	crudDao.save(clinicalDetails);
    	return success;
    }
    
    @Transactional
    public Boolean removeFinalDiagnosis(RemoveFinalDiagnosisCommand removeFinalDiagnosisCommand){
    	ClinicalDetails clinicalDetails = masterService.fetchClinicalDetailsByInPatientNumber(removeFinalDiagnosisCommand.inPatientNumber);
    	clinicalDetails.getFinalDiagnosis().removeAll(removeFinalDiagnosisCommand.finalDiagnosisToBeRemoved);
    	crudDao.save(clinicalDetails);
    	return success;
    }

    @Transactional
    public Boolean removeProceduresDone(RemoveProceduresDoneCommand removeProceduresDoneCommand){
    	ClinicalDetails clinicalDetails = masterService.fetchClinicalDetailsByInPatientNumber(removeProceduresDoneCommand.inPatientNumber);
    	clinicalDetails.getProceduresPerformed().removeAll(removeProceduresDoneCommand.proceduresToBeRemoved);
    	crudDao.save(clinicalDetails);
    	return success;
    }
    
    @Transactional
    public Boolean updatePreference(UpdatePreferenceCommand updatePreferenceCommand){
    	Preference preference = crudDao.find(Preference.class,updatePreferenceCommand.preferenceId);
        if(preference==null){
            preference = new Preference();
        }
    	Country country = (Country) crudDao.find(Country.class, updatePreferenceCommand.countryId);
    	preference.setCountry(country);
    	State state = (State) crudDao.find(State.class, updatePreferenceCommand.stateId);
    	preference.setState(state);
    	preference.setCity(updatePreferenceCommand.city);
    	crudDao.save(preference);
    	return success;
    }
}
