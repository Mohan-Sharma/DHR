package com.dhr.security;

import com.patientadmission.presentation.queries.PatientAdmissionFinder;
import org.jasypt.contrib.org.apache.commons.codec_1_3.binary.Base64;
import org.jasypt.util.text.TextEncryptor;
import org.joda.time.DateTime;
import org.nthdimenzion.object.utils.UtilValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by IntelliJ IDEA.
 * User: Nthdimenzion
 * Date: 24/5/13
 * Time: 9:16 AM
 */
@Service
public class LicenseValidation {

    private final Boolean validLicense = Boolean.FALSE;

    private final Boolean invalidLicense = Boolean.TRUE;

    @Autowired
    private PatientAdmissionFinder patientAdmissionFinder;

    private TextEncryptor basicTextEncryptor;

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public LicenseValidation(TextEncryptor basicTextEncryptor) {
        this.basicTextEncryptor = basicTextEncryptor;
    }

    public Boolean isInvalidLicense() {
        String encryptedMessage = (String) patientAdmissionFinder.findEncryptedMessage().get("EncryptedMessage");
        return isInvalidLicense(encryptedMessage, DateTime.now());
    }

    Boolean isInvalidLicense(String encryptedLicenseKey,DateTime today) {
        if(UtilValidator.isEmpty(encryptedLicenseKey))
            return invalidLicense;
        String currentMachineMacAddress;
        try {
            currentMachineMacAddress = getMachineMacAddress();
        } catch (Exception e) {
            return invalidLicense;
        }
        String decryptedLicenseKey = decryptLicenseKey(encryptedLicenseKey);

        final String[] key = decryptedLicenseKey.split("#");
        if(key.length !=2){
            return invalidLicense;
        }
        final String licenseKey = key[0];
        final DateTime expiryDate = DateTime.parse(key[1]);
        if(currentMachineMacAddress.equalsIgnoreCase(licenseKey) && today.isBefore(expiryDate.getMillis()))
            return validLicense;
        return invalidLicense;
    }

    String getMachineMacAddress() throws UnknownHostException, SocketException {
        InetAddress ip = InetAddress.getLocalHost();
        NetworkInterface network = NetworkInterface.getByInetAddress(ip);

        byte[] mac = network.getHardwareAddress();
        StringBuilder currentMachineMacAddress = new StringBuilder();
        for (int i = 0; i < mac.length; i++) {
            currentMachineMacAddress.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
        }
        System.out.println(currentMachineMacAddress.toString());
        return currentMachineMacAddress.toString();
    }

    String decryptLicenseKey(String encryptedLicenseKey){
        String decryptedLicenseKey = "";
        try{
            decryptedLicenseKey = basicTextEncryptor.decrypt(encryptedLicenseKey);
        }catch (Exception e){
            logger.error("Decryption failed ",e);
        }
        return decryptedLicenseKey;
    }
}
