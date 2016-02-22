package com.patientadmission.command;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.nthdimenzion.cqrs.command.ICommand;
import org.nthdimenzion.cqrs.command.annotations.Command;

import com.master.domain.Enumeration;

@Command
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class RemoveIndicationsCommand implements ICommand {

	@XmlElementWrapper
	public List<Enumeration> indicationsToBeRemoved;
	
	@XmlElement
	public String inPatientNumber;
	
	public RemoveIndicationsCommand() {
	}

	
	public RemoveIndicationsCommand(List<Enumeration> indicationsToBeRemoved,
			String inPatientNumber) {
		super();
		this.indicationsToBeRemoved = indicationsToBeRemoved;
		this.inPatientNumber = inPatientNumber;
	}

	@Override
	public void validate() {

	}

}
