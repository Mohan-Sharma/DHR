package org.nthdimenzion.security.application.services;

import com.patientadmission.PatientAdmissionConstants;
import com.patientadmission.domain.Doctor;
import com.patientadmission.domain.User;
import org.nthdimenzion.crud.ICrud;
import org.nthdimenzion.ddd.domain.annotations.DomainService;
import org.nthdimenzion.security.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.dao.SystemWideSaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.UUID;

@DomainService
public class UserService implements UserDetailsService {

    private UserDetailsService userDetailsService;

    @Autowired
    private SystemUser systemUser;

    @Autowired
    private IUserLoginRepository userLoginRepository;

    @Autowired
    private ICrud crudDao;

    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private SystemWideSaltSource saltSource;


    UserService() {
    }

    @Autowired
    public UserService(UserDetailsService userValidationService) {
        this.userDetailsService = userValidationService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        systemUser.uses(userDetails);
        return new SystemUser(userDetails);
    }

    @Transactional
    public void changePassword(String username, String newPassword) {
        UserLogin userLogin = userLoginRepository.findUserLoginWithUserName(username);
        crudDao.save(userLogin.changePassword(newPassword));
    }

    @Transactional
    public void resetPasswordToDefaultPassword(String username) {
        UserLogin userLogin = userLoginRepository.findUserLoginWithUserName(username);
        userLogin = userLogin.resetToDefaultPassword();
        crudDao.save(userLogin);
    }

    @Transactional
    public void disableUser(String username) {
        userLoginRepository.disableUser(username);
    }

    @Transactional
    public void changeRole(String username, String role) {
        UserLogin userLogin = userLoginRepository.findUserLoginWithUserName(username);
        userLogin.setRole(role);
        if (role.equals("ADMIN")){
            userLogin.setHomepageViewId("viewDemography");
        } else if (role.equals("DATAOP")) {
            userLogin.setHomepageViewId("registerPatient");
        } else if (role.equals("USER_CREATER")) {
            userLogin.setHomepageViewId("createUser");
        }
        crudDao.save(userLogin);
    }

    @Transactional
    public void createUserLoginForDoctor(final String userName, final Long doctorId){
        final Doctor doctor = crudDao.find(Doctor.class,doctorId);
        final SecurityGroup securityGroupForDoctor = crudDao.find(SecurityGroup.class, new Long(500));
        UserLogin userLogin = UserLogin.createUserLogin(userName,PatientAdmissionConstants.DEFAULT_PASSWORD,
                passwordEncoder,saltSource).forDoctor(doctor, securityGroupForDoctor);
        userLogin.setRole("DOCTOR");
        crudDao.save(userLogin);
    }

    @Transactional
    public void createUserLoginForUser(final String userName, final Long userId, final String role){
        final User user = crudDao.find(User.class,userId);
        final SecurityGroup securityGroupForDoctor = crudDao.find(SecurityGroup.class, new Long(500));
        UserLogin userLogin = UserLogin.createUserLogin(userName,PatientAdmissionConstants.DEFAULT_PASSWORD,
                passwordEncoder,saltSource).forUser(user, securityGroupForDoctor, role);
        userLogin.setRole(role);
        crudDao.save(userLogin);
    }

    @Transactional
    public void changePasswordAfterSecurityVerified(String username, String password) {
        UserLogin userLogin = userLoginRepository.findUserLoginWithUserName(username);
        userLogin = userLogin.changePasswordAfterSecurityVerified(password);
        crudDao.save(userLogin);
    }

    @Transactional
    public UserLogin loadUserLoginByUserName(String username) {
        UserLogin userLogin = userLoginRepository.findUserLoginWithUserName(username);
        return userLogin;
    }
}
