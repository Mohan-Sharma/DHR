package com.dhr.security;

import junit.framework.Assert;
import org.jasypt.util.text.BasicTextEncryptor;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.net.SocketException;
import java.net.UnknownHostException;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;


/**
 * Created by IntelliJ IDEA.
 * User: Nthdimenzion
 * Date: 24/5/13
 * Time: 9:21 AM
 */
public class LicenseValidationTest {

    @Test
    public void test(){}

   /* private static final String VALID_KEY_WITH_TODAY_AS_EXPIRY = "mLbKMpWE+Keg32zs2AMIbBcJ8pVTRCjn0pPSLDUl+/Yfxk7w72lMo4JTS4GxRj4utO6XQkxarGM=";
    LicenseValidation licenseValidation;

    String currentMachineMacAddress = "00-50-56-C0-00-01";
    private DateTime licenseShouldHaveExpiredDate;
    private DateTime licenseShouldBeValidDate;

    @Before
    public void init(){
        BasicTextEncryptor basicTextEncryptor = new BasicTextEncryptor();
        basicTextEncryptor.setPassword("dhr");
        licenseValidation = new LicenseValidation(basicTextEncryptor);

    }

    @Test
    public void givenLicenseKey_WhenLicenseKeyIsNotPresent_ThenKeyIsConsideredInvalid() throws SocketException, UnknownHostException {
        Assert.assertTrue(licenseValidation.isInvalidLicense("", DateTime.now()));
    }

    @Test
    public void givenLicenseKey_WhenInValidLicenseKeyIsPresent_ThenReturnTrue() throws SocketException,
            UnknownHostException {
        LicenseValidation spyLicenseValidation = spy(licenseValidation);
        when(spyLicenseValidation.getMachineMacAddress()).thenReturn(currentMachineMacAddress);
        final String invalidEncryptedKey = "tey0ViIiw2ouk+=";
        Assert.assertTrue(spyLicenseValidation.isInvalidLicense(invalidEncryptedKey, DateTime.now()));

    }

    @Test
    public void givenLicenseKey_WhenValidLicenseKeyIsPresent_ThenReturnFalse() throws SocketException,
            UnknownHostException {
        LicenseValidation spyLicenseValidation = spy(licenseValidation);
        when(spyLicenseValidation.getMachineMacAddress()).thenReturn(currentMachineMacAddress);
        licenseShouldBeValidDate = DateTime.now().minusDays(2);
        Assert.assertFalse(spyLicenseValidation.isInvalidLicense(VALID_KEY_WITH_TODAY_AS_EXPIRY, licenseShouldBeValidDate));

    }

    @Test
    public void givenLicenseKey_WhenValidLicenseKeyHasExpired_ThenReturnTrue() throws SocketException,
            UnknownHostException {
        LicenseValidation spyLicenseValidation = spy(licenseValidation);
        when(spyLicenseValidation.getMachineMacAddress()).thenReturn(currentMachineMacAddress);
        licenseShouldHaveExpiredDate = DateTime.now().plusDays(1);
        Assert.assertTrue(spyLicenseValidation.isInvalidLicense(VALID_KEY_WITH_TODAY_AS_EXPIRY, licenseShouldHaveExpiredDate));

    }

    public static void main(String[] args) {
        String currentMachineMacAddress = "00-50-56-C0-00-01";
        String key = currentMachineMacAddress.concat("#").concat(DateTime.now().plusDays(10).toString());
        BasicTextEncryptor basicTextEncryptor = new BasicTextEncryptor();
        basicTextEncryptor.setPassword("dhr");
        System.out.println("encrypt " + basicTextEncryptor.encrypt(key));
    }*/



}
