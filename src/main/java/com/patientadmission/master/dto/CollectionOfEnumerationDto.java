package com.patientadmission.master.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.master.domain.Enumeration;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CollectionOfEnumerationDto {

	@XmlElementWrapper
	public List<Enumeration> enumerations;

	public CollectionOfEnumerationDto() {
	}
	
	
	public CollectionOfEnumerationDto(List<Enumeration> enumerations) {
		this.enumerations = enumerations;
	}
	
}
