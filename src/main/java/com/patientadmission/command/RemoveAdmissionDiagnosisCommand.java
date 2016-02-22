package com.patientadmission.command;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.nthdimenzion.cqrs.command.ICommand;
import org.nthdimenzion.cqrs.command.annotations.Command;

import com.patientadmission.domain.IcdElement;

@Command
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class RemoveAdmissionDiagnosisCommand implements ICommand{
	
	@XmlElementWrapper
	public List<IcdElement> admissionDiagnosisToBeRemoved;
	
	@XmlElement
	public String inPatientNumber;
	
	public RemoveAdmissionDiagnosisCommand() {
	}

	
	public RemoveAdmissionDiagnosisCommand(
			List<IcdElement> admissionDiagnosisToBeRemoved,
			String inPatientNumber) {
		super();
		this.admissionDiagnosisToBeRemoved = admissionDiagnosisToBeRemoved;
		this.inPatientNumber = inPatientNumber;
	}



	@Override
	public void validate() {
		
	}

}
