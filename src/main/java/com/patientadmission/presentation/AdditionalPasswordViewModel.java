package com.patientadmission.presentation;

import com.dhr.service.IEncryportDecryptor;
import com.patientadmission.PatientAdmissionConstants;
import com.patientadmission.domain.Doctor;
import com.patientadmission.presentation.queries.PatientAdmissionFinder;
import org.nthdimenzion.presentation.annotations.Composer;
import org.nthdimenzion.presentation.infrastructure.AbstractZKModel;
import org.nthdimenzion.security.application.services.UserService;
import org.nthdimenzion.security.domain.*;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Composer
@VariableResolver(DelegatingVariableResolver.class)
public class AdditionalPasswordViewModel extends AbstractZKModel {

    private String additionalPassword;

    @WireVariable
    private IUserLoginRepository userLoginRepository;

    private Doctor doctor;

    @WireVariable
    private IEncryportDecryptor encryportDecryptor;

    @AfterCompose
    public void init(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, true);
        System.out.println("Hello World");
        doctor = userLoginRepository.findUserLoginWithUserName(loggedInUser.getUsername()).getDoctor();
        additionalPassword = doctor.getAdditionalPassword();

    }

    @Command
    public void changeAdditionalPassword() {
        final String encryptedAdditionalPassword = encryportDecryptor.encrypt(additionalPassword);
        doctor.setAdditionalPassword(encryptedAdditionalPassword);
        crudDao.save(doctor);
        displayMessages.displaySuccess();
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public String getAdditionalPassword() {
        return additionalPassword;
    }

    public void setAdditionalPassword(String additionalPassword) {
        this.additionalPassword = additionalPassword;
    }
}
