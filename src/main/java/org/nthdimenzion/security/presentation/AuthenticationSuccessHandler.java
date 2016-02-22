package org.nthdimenzion.security.presentation;

import com.dhr.security.LicenseValidation;
import com.patientadmission.presentation.queries.PatientAdmissionFinder;
import org.jasypt.contrib.org.apache.commons.codec_1_3.binary.Base64;
import org.nthdimenzion.crud.ICrud;
import org.nthdimenzion.object.utils.UtilValidator;
import org.nthdimenzion.presentation.infrastructure.Navigation;
import org.nthdimenzion.security.domain.SystemUser;
import org.nthdimenzion.security.domain.UserLogin;
import org.nthdimenzion.security.infrastructure.repositories.hibernate.UserLoginRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;

public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler implements LogoutSuccessHandler {

    private final Logger logger = LoggerFactory.getLogger(AuthenticationSuccessHandler.class);

    @Autowired
    private Navigation navigation;

    @Autowired
    private SystemUser systemUser;

    @Autowired
    private UserLoginRepository userLoginRepository;

    @Autowired
    private LicenseValidation licenseValidation;

    private Boolean isLicenseValidationRequired = Boolean.FALSE;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {
        UserLogin userLogin = userLoginRepository.findUserLoginWithUserName(systemUser.getUsername());
        logger.debug("Entry onAuthenticationSuccess " + " getDefaultTargetUrl() " + getDefaultTargetUrl() + " isAlwaysUseDefaultTargetUrl() " + isAlwaysUseDefaultTargetUrl() + " navigation " + navigation
                + "userDetails " + systemUser + " userLogin.getHomepageViewId()  " + userLogin.getHomepageViewId());

        request.getSession().setAttribute("loggedInUser", systemUser);
        String viewUrl = "";
        if (isLicenseValidationRequired && licenseValidation.isInvalidLicense()) {
            viewUrl = navigation.findViewUrl("licenseError");
        } else if (userLogin.getPasswordResetPending()) {
            viewUrl = navigation.findViewUrl("changePassword");
        } else {
            String homepageViewId = userLogin.getHomepageViewId();
            viewUrl = navigation.findViewUrl(homepageViewId);
        }
        super.setTargetUrlParameter(viewUrl);
        super.setDefaultTargetUrl(viewUrl);
        super.onAuthenticationSuccess(request, response, authentication);

    }


    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        logger.debug("Entry onLogoutSuccess");
        request.getSession().invalidate();
        navigation.redirect("login");
    }

    public void setLicenseValidationRequired(Boolean licenseValidationRequired) {
        isLicenseValidationRequired = licenseValidationRequired;
    }
}