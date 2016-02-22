package com.patientadmission.presentation;

import com.google.common.collect.ImmutableMap;
import com.patientadmission.domain.Doctor;
import com.patientadmission.domain.User;
import org.nthdimenzion.presentation.annotations.Composer;
import org.nthdimenzion.presentation.infrastructure.AbstractZKModel;
import org.nthdimenzion.security.application.services.UserService;
import org.nthdimenzion.security.domain.IUserLoginRepository;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import java.util.Map;


@Composer
public class ChangePasswordViewModel extends AbstractZKModel {

    Map userLoginDetails;

    @WireVariable
    private UserService userService;

    @WireVariable
    private IUserLoginRepository userLoginRepository;

    @AfterCompose
    public void init(@ContextParam(ContextType.VIEW) Component view) {
        userLoginDetails = createMapWithGivenKeys("userName", "password", "confirmPassword","additionalPassword");
        userLoginDetails.put("userName", loggedInUser.getUsername());

    }


    @Command
    public void changePassword() {
        String password = (String)userLoginDetails.get("password");
        String confirmPassword = (String)userLoginDetails.get("confirmPassword");

        if(!password.equals(confirmPassword)){
            displayMessages.displayError("Password Mismatch");
            return;
        }
        userService.changePassword(loggedInUser.getUsername(), password);
        Doctor doctor = userLoginRepository.findUserLoginWithUserName(loggedInUser.getUsername()).getDoctor();
        if (doctor != null){
            doctor.setAdditionalPassword((String) userLoginDetails.get("additionalPassword"));
            crudDao.save(doctor);
        } else {
            User user = userLoginRepository.findUserLoginWithUserName(loggedInUser.getUsername()).getUser();
            user.setAdditionalPassword((String) userLoginDetails.get("additionalPassword"));
            crudDao.save(user);
        }

        navigation.redirect("login", ImmutableMap.of("resetPassword","1"));
    }

    public Map getUserLoginDetails() {
        return userLoginDetails;
    }

    public void setUserLoginDetails(Map userLoginDetails) {
        this.userLoginDetails = userLoginDetails;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
