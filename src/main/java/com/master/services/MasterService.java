package com.master.services;

import com.master.domain.Enumeration;
import com.master.domain.Enumeration.EnumerationType;
import com.patientadmission.domain.AdmissionDetails;
import com.patientadmission.domain.ClinicalDetails;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.nthdimenzion.crud.ICrud;
import org.nthdimenzion.object.utils.UtilValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MasterService implements IMasterService{

	@Autowired
	public ICrud crudDao;

	@Override
	public List<Enumeration> getEnumerationByEnumType(String enumType){
		EnumerationType enumerationTypeSelected = null;
		Enumeration.EnumerationType[] enumerationTypes = Enumeration.EnumerationType.values();
		for(Enumeration.EnumerationType enumerationType : enumerationTypes)
			if(enumerationType.toString().equals(enumType)){
				enumerationTypeSelected = enumerationType;
				break;
			}
		DetachedCriteria criteria = DetachedCriteria.forClass(Enumeration.class);
		criteria.add(Restrictions.eq("enumType", enumerationTypeSelected));
		List<Enumeration> filteredEnumerations = crudDao.findByCriteria(criteria);
		return filteredEnumerations;
	}

    @Override
    public Enumeration getEnumerationByEnumCode(String enumCode){
    	if(UtilValidator.isEmpty(enumCode))
    		return null;
        DetachedCriteria criteria = DetachedCriteria.forClass(Enumeration.class);
        criteria.add(Restrictions.eq("enumCode", enumCode));
        return crudDao.findByUniqueKey(criteria);
    }

	@Override
	public ClinicalDetails fetchClinicalDetailsByInPatientNumber(String inPatientNumber) {
        System.out.println(inPatientNumber);
        DetachedCriteria admissionDetailsCriteria = DetachedCriteria.forClass(AdmissionDetails.class);
        //System.out.println(admissionDetailsCriteria);
        admissionDetailsCriteria.add(Restrictions.eq("inpatientNumber", inPatientNumber));
		AdmissionDetails admissionDetails = crudDao.findByUniqueKey(admissionDetailsCriteria);
		return admissionDetails.getClinicalDetails();
	}
}
