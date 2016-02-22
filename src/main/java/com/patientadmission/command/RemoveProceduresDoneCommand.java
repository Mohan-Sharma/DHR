package com.patientadmission.command;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.nthdimenzion.cqrs.command.ICommand;
import org.nthdimenzion.cqrs.command.annotations.Command;

import com.patientadmission.domain.Procedure;

@Command
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class RemoveProceduresDoneCommand implements ICommand {
	
	@XmlElementWrapper
	public List<Procedure> proceduresToBeRemoved;
	
	@XmlElement
	public String inPatientNumber;
	
	public RemoveProceduresDoneCommand() {
	}

	
	public RemoveProceduresDoneCommand(
			List<Procedure> admissionDiagnosisToBeRemoved,
			String inPatientNumber) {
		super();
		this.proceduresToBeRemoved = admissionDiagnosisToBeRemoved;
		this.inPatientNumber = inPatientNumber;
	}


	@Override
	public void validate() {
		// TODO Auto-generated method stub

	}

}
