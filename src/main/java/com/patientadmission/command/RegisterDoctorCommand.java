package com.patientadmission.command;

import org.joda.time.DateTime;
import org.nthdimenzion.cqrs.command.ICommand;
import org.nthdimenzion.cqrs.command.annotations.Command;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by IntelliJ IDEA.
 * User: Nthdimenzion
 * Date: 25/1/13
 * Time: 6:45 PM
 */

@Command
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class RegisterDoctorCommand implements ICommand{

    public String titleCode;

    public String firstName;

    public String middleName;

    public String lastName;

    public String contactNumber;

    public String getTitleCode() {
        return titleCode;
    }

    public void setTitleCode(String titleCode) {
        this.titleCode = titleCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    @Override
    public void validate() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
