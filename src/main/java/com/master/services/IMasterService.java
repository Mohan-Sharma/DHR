package com.master.services;

import java.util.List;

import com.master.domain.Enumeration;
import com.patientadmission.domain.ClinicalDetails;

public interface IMasterService {

	List<Enumeration> getEnumerationByEnumType(String enumType);

    Enumeration getEnumerationByEnumCode(String enumCode);
    
    ClinicalDetails fetchClinicalDetailsByInPatientNumber(String inPatientNumber);
}
