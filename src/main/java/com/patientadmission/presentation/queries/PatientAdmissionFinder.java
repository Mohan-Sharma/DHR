package com.patientadmission.presentation.queries;

import org.apache.ibatis.annotations.Param;
import org.nthdimenzion.cqrs.query.annotations.Finder;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA. User: Nthdimenzion Date: 24/1/13 Time: 11:45 AM
 */
@Finder
public interface PatientAdmissionFinder {

    List<Map<String, ?>> findAllBeds();

    List<Map<String, ?>> findAllCountries();

    List<Map<String, ?>> findAllStatesInCountry(Long countryId);

    List<Map<String, ?>> findAllCitiesInState(Long stateId);

    Map<String, ?> getPatientByMRN(String medicalRecordNumber);

    List<Map<String, ?>> findAllDoctors();

    List<Map<String, ?>> findAllWards();

    List<Map<String, ?>> admissionDetailsPatient(Long patientId);

    List<Map<String, ?>> findAllAdmittingDepartments();

    List<Map<String, ?>> findAllIcdElements();

    List<Map<String, ?>> findAllCpts();

    List<Map<String, ?>> getIcdElementByCodeOrCategory(@Param("icdCode") String icdCode, @Param("description") String description);

    List<Map<String, ?>> getCptCodeOrDescription(@Param("cptCode") String cptCode, @Param("description") String description);

    Map<String, ?> getPatientAdmissionDetailsByInPatientNumber(String inPatientNumber);

    List<Map<String, ?>> findIndicationsForAdmissionByInpatientNumber(String inPatientNumber);

    List<Map<String, ?>> findAdmittingDiagnosisByInpatientNumber(String inPatientNumber);

    List<Map<String, ?>> findDischargeDiagnosisByInpatientNumber(String inPatientNumber);

    List<Map<String, ?>> findFinalDiagnosisByInpatientNumber(String inPatientNumber);

    List<Map<String, ?>> findProceduresPerformedByInpatientNumber(String inPatientNumber);

    List<Map<String, ?>> findAllUsers();

    List<Map<String, ?>> findAllDoctorsWithOutUserLogins();
    
    List<Map<String, ?>> findAllDepartments();

    Map<String, ?> findPatientClinicalDetailsById(Long clinicalDetailsId);

    List<Map<String, ?>> getIcdElementByDescription(@Param("description") String description);

    List<Map<String, ?>> getProceduresByDescription(@Param("description") String description);

    List<Map<String, ?>> searchPatientAdmissionDetailsBy(@Param("inPatientNumber") String inPatientNumber, @Param("mrnNumber") String mrnNumber,
                                                         @Param("firstName") String firstName, @Param("lastName") String lastName, @Param("admissionDate") Date admissionDate,
                                                         @Param("admissionDateThru") Date admissionDateThru, @Param("dischargeDate") Date dischargeDate, @Param("dischargeDateThru") Date dischargeDateThru,
                                                         @Param("admissionDiagnosisId") String admisisonDiagnosisId, @Param("dischargeDiagnosisId") String dischargeDiagnosisId,
                                                         @Param("finalDiagnosisId") String finalDiagnosisId, @Param("procedureId") Long procedureId, @Param("gender") String gender, @Param("age") Integer age,
                                                         @Param("comparision") String comparision, @Param("nowDate") Date nowDate, @Param("calculatedDate") Date calculatedDate, @Param("minLimit") Integer minLimit,
                                                         @Param("maxLimit") Integer maxLimit, @Param("doctorId") Long doctorId, @Param("role") String role);

    List<Map<String, ?>> findAllInpatientNumberAndCaseSheetsByMrnNumber(String mrnNumber);

    List<Map<String, ?>> searchProceduresBy(@Param("admissionFromDate") Date admissionFromDate, @Param("admissionThruDate") Date admissionThruDate, @Param("nowDate") Date nowDate,
                                            @Param("gender") String gender, @Param("age") Integer age, @Param("comparision") String comparision,
                                            @Param("calculatedDate") Date calculatedDate, @Param("procedureId") String procedureId, @Param("doctorId") Long doctorId);

    Map<String, ?> findFileNameBy(String inPatientNumber);

    List<Map<String, ?>> searchDiseasesBy(@Param("admissionFromDate") Date admissionFromDate, @Param("admissionThruDate") Date admissionThruDate, @Param("nowDate") Date nowDate,
                                          @Param("icdElementId") String icdElementId, @Param("gender") String gender, @Param("age") Integer age, @Param("comparision") String comparision,
                                          @Param("calculatedDate") Date calculatedDate, @Param("diagnosisType") String diagnosisType, @Param("doctorId") Long doctorId);
    
    List<Map<String, ?>> searchDiseasesByCount(@Param("admissionFromDate") Date admissionFromDate, @Param("admissionThruDate") Date admissionThruDate, @Param("nowDate") Date nowDate,
                                               @Param("icdElementId") String icdElementId, @Param("gender") String gender, @Param("age") Integer age, @Param("comparision") String comparision,
                                               @Param("calculatedDate") Date calculatedDate, @Param("diagnosisType") String diagnosisType, @Param("doctorId") Long doctorId);
    

    List<Map<String, ?>> findDischargeDiagnosisByDischargeDate(@Param("dischargeFromDate") Date dischargeFromDate, @Param("dischargeThruDate") Date dischargeThruDate, @Param("doctorId") Long doctorId);

    List<Map<String, ?>> findDischargeDiagnosisPatientIdsByICDElementId(@Param("icdElementId") String icdElementId, @Param("dischargeFromDate") Date dischargeFromDate,
                                                                        @Param("dischargeThruDate") Date dischargeThruDate);

    List<Map<String, ?>> findProceduresPerformedByDischargeDate(@Param("dischargeFromDate") Date dischargeFromDate, @Param("dischargeThruDate") Date dischargeThruDate, @Param("doctorId") Long doctorId);

    List<Map<String, ?>> findDepartmentByDischargeDate(@Param("dischargeFromDate") Date dischargeFromDate, @Param("dischargeThruDate") Date dischargeThruDate);

    List<Map<String, ?>> findProcedureDonePatientIdsByProcedureId(@Param("cptId") Long cptId, @Param("dischargeFromDate") Date dischargeFromDate,
                                                                  @Param("dischargeThruDate") Date dischargeThruDate);

    List<Map<String, ?>> searchAllDiseasesBy(@Param("admissionFromDate") Date admissionFromDate, @Param("admissionThruDate") Date admissionThruDate,
                                             @Param("nowDate") Date nowDate, @Param("icdElementId") String icdElementId, @Param("gender") String gender, @Param("age") Integer age,
                                             @Param("comparision") String comparision, @Param("calculatedDate") Date calculatedDate, @Param("doctorId") Long doctorId);
    
    List<Map<String, ?>> searchAllDiseasesByCount(@Param("admissionFromDate") Date admissionFromDate, @Param("admissionThruDate") Date admissionThruDate,
                                                  @Param("nowDate") Date nowDate, @Param("icdElementId") String icdElementId, @Param("gender") String gender, @Param("age") Integer age,
                                                  @Param("comparision") String comparision, @Param("calculatedDate") Date calculatedDate, @Param("doctorId") Long doctorId);

    List<Map<String, ?>> searchDepartmentBy(@Param("admissionFromDate") Date admissionFromDate, @Param("admissionThruDate") Date admissionThruDate, @Param("nowDate") Date nowDate,
                                            @Param("departmentId") Long departmentId, @Param("gender") String gender, @Param("age") Integer age, @Param("comparision") String comparision,
                                            @Param("calculatedDate") Date calculatedDate, @Param("minPage") Integer minPage, @Param("maxPage") Integer maxPage, @Param("doctorId") Long doctorId, @Param("role") String role);

    List<Map<String, ?>> findWardByDischargeDate(@Param("dischargeFromDate") Date dischargeFromDate, @Param("dischargeThruDate") Date dischargeThruDate);

    List<Map<String, ?>> findDischargeDiagnosisByDischargeDateByLimit(@Param("dischargeFromDate") Date dischargeFromDate, @Param("dischargeThruDate") Date dischargeThruDate,
                                                                      @Param("limit") Integer limit, @Param("limit1") Integer limit1, @Param("doctorId") Long doctorId);

    List<Map<String, ?>> findDischargeDiagnosisAndDepartmentsByDischargeDateByLimit(@Param("dischargeFromDate") Date dischargeFromDate,
                                                                                    @Param("dischargeThruDate") Date dischargeThruDate, @Param("departmentId") Long departmentId, @Param("limit") Integer limit, @Param("limit1") Integer limit1, @Param("doctorId") Long doctorId);

    List<Map<String, ?>> searchPatientAdmissionByDates(@Param("admissionDate") Date admissionDate, @Param("admissionDateThru") Date admissionDateThru,
                                                       @Param("dischargeDate") Date dischargeDate, @Param("dischargeDateThru") Date dischargeDateThru);

    List<Map<String, ?>> searchPatientAdmissionByDepartmentAndDates(@Param("admissionDate") Date admissionDate, @Param("admissionDateThru") Date admissionDateThru,
                                                                    @Param("dischargeDate") Date dischargeDate, @Param("dischargeDateThru") Date dischargeDateThru, @Param("departmentId") Long departmentId, @Param("gender") String gender);

    List<Map<String, ?>> searchPatientAdmissionByWardAndDates(@Param("admissionDate") Date admissionDate, @Param("admissionDateThru") Date admissionDateThru,
                                                              @Param("dischargeDate") Date dischargeDate, @Param("dischargeDateThru") Date dischargeDateThru, @Param("wardId") Long wardId, @Param("gender") String gender);

    List<Map<String, ?>> searchPatientAdmissionDetailsCount(@Param("inPatientNumber") String inPatientNumber, @Param("mrnNumber") String mrnNumber,
                                                            @Param("firstName") String firstName, @Param("lastName") String lastName, @Param("admissionDate") Date admissionDate,
                                                            @Param("admissionDateThru") Date admissionDateThru, @Param("dischargeDate") Date dischargeDate, @Param("dischargeDateThru") Date dischargeDateThru,
                                                            @Param("admissionDiagnosisId") String admisisonDiagnosisId, @Param("dischargeDiagnosisId") String dischargeDiagnosisId,
                                                            @Param("finalDiagnosisId") String finalDiagnosisId, @Param("procedureId") Long procedureId, @Param("gender") String gender, @Param("age") Integer age,
                                                            @Param("comparision") String comparision, @Param("nowDate") Date nowDate, @Param("calculatedDate") Date calculatedDate, @Param("doctorId") Long doctorId, @Param("role") String role);

    List<Map<String, ?>> searchProceduresByCount(@Param("admissionFromDate") Date admissionFromDate, @Param("admissionThruDate") Date admissionThruDate,
                                                 @Param("nowDate") Date nowDate, @Param("gender") String gender, @Param("age") Integer age,
                                                 @Param("comparision") String comparision, @Param("calculatedDate") Date calculatedDate, @Param("procedureId") String procedureId, @Param("doctorId") Long doctorId);

    List<Map<String, ?>> searchAdmittedWardBy(@Param("admissionFromDate") Date admissionFromDate, @Param("admissionThruDate") Date admissionThruDate,
                                              @Param("nowDate") Date nowDate, @Param("wardId") Long wardId, @Param("gender") String gender, @Param("age") Integer age, @Param("comparision") String comparision,
                                              @Param("calculatedDate") Date calculatedDate, @Param("doctorId") Long doctorId, @Param("role") String role);

    List<Map<String, ?>> searchDoctorBy(@Param("admissionFromDate") Date admissionFromDate, @Param("admissionThruDate") Date admissionThruDate, @Param("nowDate") Date nowDate,
                                        @Param("doctorId") Long doctorId, @Param("gender") String gender, @Param("age") Integer age, @Param("comparision") String comparision,
                                        @Param("calculatedDate") Date calculatedDate, @Param("minPage") Integer minPage, @Param("maxPage") Integer maxPage);

    List<Map<String, ?>> searchDoctorByCount(@Param("admissionFromDate") Date admissionFromDate, @Param("admissionThruDate") Date admissionThruDate,
                                             @Param("nowDate") Date nowDate, @Param("doctorId") Long doctorId, @Param("gender") String gender, @Param("age") Integer age, @Param("comparision") String comparision,
                                             @Param("calculatedDate") Date calculatedDate);
    
    List<Map<String, ?>> searchDepartmentCount(@Param("admissionFromDate") Date admissionFromDate, @Param("admissionThruDate") Date admissionThruDate, @Param("nowDate") Date nowDate,
                                               @Param("departmentId") Long departmentId, @Param("gender") String gender, @Param("age") Integer age, @Param("comparision") String comparision,
                                               @Param("calculatedDate") Date calculatedDate, @Param("doctorId") Long doctorId, @Param("role") String role);
    


    List<Map<String, ?>> searchDiseasesCount(@Param("admissionFromDate") Date admissionFromDate, @Param("admissionThruDate") Date admissionThruDate, @Param("nowDate") Date nowDate,
                                             @Param("icdElementId") String icdElementId, @Param("gender") String gender, @Param("age") Integer age, @Param("comparision") String comparision,
                                             @Param("calculatedDate") Date calculatedDate, @Param("diagnosisType") String diagnosisType);
    
    List<Map<String, ?>> searchAllDiseasesCount(@Param("admissionFromDate") Date admissionFromDate, @Param("admissionThruDate") Date admissionThruDate,
                                                @Param("nowDate") Date nowDate, @Param("icdElementId") String icdElementId, @Param("gender") String gender, @Param("age") Integer age,
                                                @Param("comparision") String comparision, @Param("calculatedDate") Date calculatedDate);

    Map<String,?> findPatientById(Map<String, ?> param);
    
    Map<String, ?> findEncryptedMessage();

    List<Map<String, ?>> findSecurityQuestions();

}
