package org.nthdimenzion.security.domain;

import com.google.common.collect.Sets;
import com.patientadmission.PatientAdmissionConstants;
import com.patientadmission.domain.Doctor;
import com.patientadmission.domain.User;
import org.nthdimenzion.ddd.domain.BaseAggregateRoot;
import org.nthdimenzion.ddd.domain.annotations.AggregateRoot;
import org.nthdimenzion.ddd.infrastructure.exception.ErrorDetails;
import org.nthdimenzion.object.utils.UtilValidator;
import org.nthdimenzion.security.domain.error.HomePageAlreadyExistsForUser;
import com.patientadmission.domain.SecurityQuestion;
import org.springframework.security.authentication.dao.SystemWideSaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@AggregateRoot
@Entity
public class UserLogin extends BaseAggregateRoot implements IsADoctor{

    private Credentials credentials;
    private UserLoginId userLoginId;
    private Set<SecurityGroup> securityGroups = Sets.newHashSet();
    private Boolean isEnabled = Boolean.TRUE;
    private String homepageViewId;
    private Doctor doctor;
    private User user;
    private Boolean isPasswordResetPending = Boolean.TRUE;
    private SecurityQuestion securityQuestion;
    private String role;

    protected UserLogin() {
    }

    UserLogin(Credentials credentials, UserLoginId userLoginId) {
        this.credentials = credentials;
        this.userLoginId = userLoginId;
    }

    public static UserLogin createUserLogin(String username,String password,PasswordEncoder passwordEncoder,SystemWideSaltSource saltSource){
        Credentials credentials = new Credentials.Builder(username,password).passwordEncoder(passwordEncoder).saltSource(saltSource).build();
        UserLogin userLogin = new UserLogin(credentials,new UserLoginId(UUID.randomUUID().toString()));
        return userLogin;

    }

    public void assignEncoder(PasswordEncoder passwordEncoder,SystemWideSaltSource systemWideSaltSource){
        this.credentials.assignEncoder(passwordEncoder,systemWideSaltSource);
    }

    @ManyToMany
    Set<SecurityGroup> getSecurityGroups() {
        return securityGroups;
    }

    void setSecurityGroups(Set<SecurityGroup> securityGroups) {
        this.securityGroups = securityGroups;
    }

    public void add(SecurityGroup securityGroup) {
        this.securityGroups.add(securityGroup);
    }

    public UserLogin forDoctor(Doctor doctor,SecurityGroup securityGroup){
        this.doctor = doctor;
        this.add(securityGroup);
        this.homepageViewId = "viewDemography";
        return this;
    }

    public UserLogin forUser(User user,SecurityGroup securityGroup, String role){
        this.user = user;
        this.add(securityGroup);
        if ((role.equals("ADMIN")) || (role.equals("DOCTOR"))){
            this.homepageViewId = "viewDemography";
        } else if (role.equals("DATAOP")){
            this.homepageViewId = "registerPatient";
        } else {
            this.homepageViewId = "createUser";
        }
        return this;
    }

    public void addAll(Set<SecurityGroup> securityGroups) {
        if (UtilValidator.isNotEmpty(securityGroups)) {
            for (SecurityGroup securityGroup : securityGroups) {
                add(securityGroup);
            }
        }
    }

    public void assignHomePage(HomePageDetails homePageDetails) throws HomePageAlreadyExistsForUser {
        assignHomePage(homePageDetails, true);
    }


    public void assignHomePage(HomePageDetails homepageViewId, boolean override) throws HomePageAlreadyExistsForUser {
        if (UtilValidator.isNotEmpty(this.homepageViewId) && !override) {
            throw new HomePageAlreadyExistsForUser(new ErrorDetails.Builder("003").build());
        } else {
            this.homepageViewId = homepageViewId.getHomepageViewId();
        }
    }

    @Embedded
    public UserLoginId getUserLoginId() {
        return userLoginId;
    }

    void setUserLoginId(UserLoginId userLoginId) {
        this.userLoginId = userLoginId;
    }

    @Embedded
    public Credentials getCredentials() {
        return credentials;
    }

    void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }


    Boolean getEnabled() {
        return isEnabled;
    }

    public void setEnabled(Boolean enabled) {
        isEnabled = enabled;
    }

    public String getHomepageViewId() {
        return homepageViewId;
    }

    public void setHomepageViewId(String homepageViewId) {
        this.homepageViewId = homepageViewId;
    }

    @OneToOne
    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    @OneToOne
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return credentials.toString();
    }


    @Override
    @Transient

    public boolean isADoctor() {
        return doctor!=null;
    }

    @Override
    @Transient
    public Long getDoctorId() {
        if(isADoctor()){
            return doctor.getId();
        }
        return null;
    }

    public Boolean getPasswordResetPending() {
        return isPasswordResetPending;
    }

    public void setPasswordResetPending(Boolean passwordResetPending) {
        isPasswordResetPending = passwordResetPending;
    }

    public UserLogin resetToDefaultPassword(){
        isPasswordResetPending = Boolean.TRUE;
        credentials.changePassword(PatientAdmissionConstants.DEFAULT_PASSWORD);
        return this;
    }

    public UserLogin changePassword(String password){
        credentials.changePassword(password);
        isPasswordResetPending = Boolean.FALSE;
        return this;
    }

    @Transient
    public UserLogin changePasswordAfterSecurityVerified(String password){
        credentials.changePassword(password);
        isPasswordResetPending = Boolean.TRUE;
        return this;
    }

    @Embedded
    public SecurityQuestion getSecurityQuestion() {
        return securityQuestion;
    }

    public void setSecurityQuestion(SecurityQuestion securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    @Transient
    public boolean isSecurityQuestionCorrectlyAnswered(SecurityQuestion securityQuestion){
        return this.securityQuestion.equals(securityQuestion);
    }

    @Transient
    public String getSecurityQuestionForLoggedInUser(){
        return this.securityQuestion.getQuestion();
    }

    @Transient
    public String getCurrentPasswordForLoggedInUser(){
        return this.credentials.getPassword();
    }
    @Transient
    public String getEncodedPassword(String password){
        return this.credentials.getEncodedPassword(password);
    }

    @Transient
    public String getSecurityAnswerForLoggedInUser(){
        return this.securityQuestion.getAnswer();
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

