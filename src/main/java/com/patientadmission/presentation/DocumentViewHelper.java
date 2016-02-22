package com.patientadmission.presentation;

import com.patientadmission.PatientAdmissionConstants;
import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.BadSecurityHandlerException;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.nthdimenzion.object.utils.UtilValidator;
import org.nthdimenzion.presentation.infrastructure.Navigation;
import org.zkoss.zul.Filedownload;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Nthdimenzion
 * Date: 2/4/13
 * Time: 2:18 PM
 */
public final class DocumentViewHelper {

    public static void openDocumentInNewWindow(Navigation navigation, String inPatientNumber) {
        Map params = new HashMap();
        params.put("inPatientNumber", inPatientNumber);
        navigation.redirectToPopup("fileViewer", params);
    }

    public static void downloadPdf(final String filePath, final String contentType,
                                   String additionalPassword) throws IOException, BadSecurityHandlerException, COSVisitorException {
        if (UtilValidator.isEmpty(filePath))
            return;
        File originalPDF = new File(filePath);
        String encryptedPdfName = deriveEncryptedPdfNameFromOriginalPdf(originalPDF);
        String originalFilePath = originalPDF.getPath();

        File file = new File("./tmp");
        if (!file.exists()) {
            file.mkdir();
        } else {
            FileUtils.cleanDirectory(file);
        }

        String encryptedPdfPath = originalFilePath.replace(originalPDF.getName(), encryptedPdfName);
        encryptedPdfPath = encryptedPdfPath.replace(encryptedPdfPath.substring(0,encryptedPdfPath.indexOf(encryptedPdfName)-1),file.getPath());

        //String encryptedPdfPath = originalFilePath.replace(originalPDF.getName(), encryptedPdfName);

        /*if (isPdfPresent(encryptedPdfPath)) {
            downloadPdf(contentType, encryptedPdfPath);
            return;
        }*/

        PDDocument originalPdfDoc = createEncryptedPdf(additionalPassword, originalPDF, encryptedPdfPath);
        originalPdfDoc.close();
        downloadPdf(contentType, encryptedPdfPath);

    }

    private static PDDocument createEncryptedPdf(String additionalPassword, File originalPDF, String encryptedPdfPath) throws IOException, BadSecurityHandlerException, COSVisitorException {
        PDFParser parser = new PDFParser(new BufferedInputStream(new FileInputStream(originalPDF)));
        parser.parse();
        PDDocument originalPdfDoc = parser.getPDDocument();
        originalPdfDoc.protect(new StandardProtectionPolicy(PatientAdmissionConstants.PDF_OWNER_PASSWORD, additionalPassword, buildPermissions()));
        originalPdfDoc.save(encryptedPdfPath);
        return originalPdfDoc;
    }

    private static void downloadPdf(String contentType, String pdfPath) throws FileNotFoundException {
        File encryptedPdf = new File(pdfPath);
        Filedownload.save(encryptedPdf, contentType);

    }

    private static boolean isPdfPresent(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    private static String deriveEncryptedPdfNameFromOriginalPdf(File originalPDF) {
        return originalPDF.getName().replace(".pdf", "_protected").concat(".pdf");
    }

    private static AccessPermission buildPermissions() {
        AccessPermission accessPermission = new AccessPermission();

        accessPermission.setCanExtractContent(false);
        accessPermission.setCanModify(false);
        accessPermission.setCanPrint(false);
        accessPermission.setReadOnly();
        accessPermission.setCanAssembleDocument(false);
        accessPermission.setCanExtractForAccessibility(false);
        accessPermission.setCanPrintDegraded(false);
        return accessPermission;
    }
}

