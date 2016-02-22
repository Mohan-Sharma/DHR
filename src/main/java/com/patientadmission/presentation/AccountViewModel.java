package com.patientadmission.presentation;

import com.patientadmission.PatientAdmissionConstants;
import com.patientadmission.domain.User;
import com.patientadmission.presentation.queries.PatientAdmissionFinder;
import org.nthdimenzion.presentation.annotations.Composer;
import org.nthdimenzion.presentation.infrastructure.AbstractZKModel;
import org.nthdimenzion.security.application.services.UserService;
import org.nthdimenzion.security.domain.UserLogin;
import org.zkoss.bind.annotation.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Composer
@VariableResolver(DelegatingVariableResolver.class)
public class AccountViewModel extends AbstractZKModel {

    @WireVariable
    private PatientAdmissionFinder patientAdmissionFinder;

    @Wire
    Listbox departmentListbox;

    private List<Map<String, ?>> allUsers;
    private List<Map<String, ?>> doctors;
    private List<String> roles;
    private String selectedRole;

    private Map<String, ?> selectedDoctor;

    private Map newUser;

    @WireVariable
    private UserService userService;

    @AfterCompose
    public void init(@ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, true);
        doctors = patientAdmissionFinder.findAllDoctorsWithOutUserLogins();
        allUsers = patientAdmissionFinder.findAllUsers();
        newUser = createMapWithGivenKeys("userName", "defaultPassword");
        newUser.put("defaultPassword", PatientAdmissionConstants.DEFAULT_PASSWORD);
        roles = new ArrayList<String>(Arrays.asList("ADMIN", "DATAOP", "USER_CREATER"));
    }

    @NotifyChange("users")
    @Command("addUser")
    public void addUser() {
        String userName = (String) newUser.get("userName");
        userService.createUserLoginForDoctor(userName, (Long) selectedDoctor.get("id"));
        navigation.redirect("viewUsers");
    }

    @Command
    @NotifyChange("allUsers")
    public void disableUser(@BindingParam("user") Map user) {
        userService.disableUser((String) user.get("userName"));
        allUsers.remove(user);
        displayMessages.displaySuccess();
    }

    @Command
    public void assignDefaultPassword(@BindingParam("user") Map user) {
        userService.resetPasswordToDefaultPassword((String) user.get("userName"));
        displayMessages.displaySuccess();
    }

    @Command
    public void changeRole(@BindingParam("user") Map user) {
        if (selectedRole != null) {
            String role = selectedRole.substring(1, selectedRole.length() - 1);
            userService.changeRole((String) user.get("userName"), role);
            displayMessages.displaySuccess();
        } else {
            displayMessages.displayError("please select role");
        }
    }

    @Command
    public void checkSelectedDepartments(@BindingParam("listItem") Listitem listitem, @BindingParam("user") Map user){
        UserLogin userLogin = userService.loadUserLoginByUserName(user.get("userName").toString());
        if(userLogin.getRole().equals(listitem.getLabel().toString())){
            listitem.setSelected(true);
        }
        System.out.print("hi");
    }

    public List<Map<String, ?>> getDoctors() {
        return doctors;
    }

    public void setSelectedDoctor(Map<String, ?> selectedDoctor) {
        this.selectedDoctor = selectedDoctor;
    }

    public Map getNewUser() {
        return newUser;
    }

    public void setNewUser(Map newUser) {
        this.newUser = newUser;
    }

    public List<Map<String, ?>> getAllUsers() {
        return allUsers;
    }

    public List<String> getRoles() {
        return roles;
    }

    public String getSelectedRole() {
        return selectedRole;
    }

    public void setSelectedRole(String selectedRole) {
        this.selectedRole = selectedRole;
    }
}
