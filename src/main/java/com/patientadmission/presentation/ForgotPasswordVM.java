package com.patientadmission.presentation;

import com.google.common.collect.Lists;
import com.master.services.ForgotPasswordService;
import com.master.util.PasswordGenerator;
import com.patientadmission.presentation.queries.PatientAdmissionFinder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.nthdimenzion.crud.ICrud;
import org.nthdimenzion.presentation.annotations.Composer;
import org.nthdimenzion.presentation.infrastructure.AbstractZKModel;
import org.nthdimenzion.security.application.services.UserService;
import org.nthdimenzion.security.domain.Credentials;
import org.nthdimenzion.security.domain.IUserLoginRepository;
import org.nthdimenzion.security.domain.SystemUser;
import org.nthdimenzion.security.domain.UserLogin;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.zkoss.bind.annotation.*;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.*;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Window;

import java.util.*;

/**
 * Created by Administrator on 2/18/2015.
 */
@Composer
@NoArgsConstructor
@Getter
@Setter
@VariableResolver(DelegatingVariableResolver.class)
public class ForgotPasswordVM extends AbstractZKModel{
    @WireVariable
    private PatientAdmissionFinder patientAdmissionFinder;
    @WireVariable
    ForgotPasswordService forgotPasswordService;
    @WireVariable
    private IUserLoginRepository userLoginRepository;
    private List<Map<String,?>> securityQuestions;

    private Map<String,?> selectedQuestion;
    private String userName;
    private String answer;
    private UserLogin userLogin;
    private String newPassword;
    @Wire
    Window forgotWin;

    @Init
    public void init(){
    }

    @AfterCompose
    public void afterCompose(@ContextParam(ContextType.VIEW)Component component){
        Selectors.wireComponents(component, this, false);
        securityQuestions = patientAdmissionFinder.findSecurityQuestions();
    }

    @Command
    @NotifyChange({"userLogin","userName","answer","selectedQuestion"})
    public void changePassword() {
        userLogin = userLoginRepository.findUserLoginWithUserName(userName);
        boolean result = validateDetailsBeforeChangingPassword();
        if (result) {
            newPassword = PasswordGenerator.generateNewPassword();
            boolean success = forgotPasswordService.changePasswordAfterSecurityVerified(userName, newPassword);
            if(success){
                Messagebox.show("Password Updated, New Password To Login - "+newPassword, "Notification",
                        new Messagebox.Button[]{Messagebox.Button.OK}, null,
                        new EventListener<Messagebox.ClickEvent>() {
                            public void onEvent(org.zkoss.zul.Messagebox.ClickEvent event) {
                                if ("onOK".equals(event.getName())) {
                                    navigation.redirect("login");
                                }
                            }
                        });
            }
            else{
                displayMessages.displayError("unable to change please try again..");
            }
        }
    }

    @Command
    public void backToLogin(){
        navigation.redirect("login");
    }

    private boolean validateDetailsBeforeChangingPassword() {
        if(userLogin == null) {
            displayMessages.displayError("Invalid username..");
            userName = null;
            answer = null;
            return false;
        }
        if(userLogin.getSecurityQuestion() == null){
            displayMessages.displayError("You have not set any security Question. Please contact administrator to reset your password.");
            return false;
        }
        String loggedInUserSecurityQuestion = userLogin.getSecurityQuestionForLoggedInUser();
        String loggedInUserSecurityAnswer= userLogin.getSecurityAnswerForLoggedInUser();
        if(userLogin == null){
            displayMessages.displayError("Invalid User Login");
            return false;
        }
        if(!loggedInUserSecurityQuestion.equals(selectedQuestion.get("securityQuestion").toString())){
            displayMessages.displayError("Invalid Security Question");
            return false;
        }
        if(!loggedInUserSecurityAnswer.equals(answer)){
            displayMessages.displayError("Wrong Security Answer");
            return false;
        }
        displayMessages.clearMessage();
        return true;
    }

}
