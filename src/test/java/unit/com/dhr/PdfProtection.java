package com.dhr;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.BadSecurityHandlerException;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Nthdimenzion
 * Date: 3/4/13
 * Time: 6:17 PM
 */
public class PdfProtection {
    public static void main(String[] args) throws IOException, BadSecurityHandlerException, COSVisitorException {
        System.out.println("Hello World");
        File originalPDF = new File("E:\\Bookmarks.pdf");

        System.out.println(originalPDF.getName());
        System.out.println(originalPDF.getPath());

        PDFParser parser = new PDFParser(new BufferedInputStream(new FileInputStream(
                originalPDF)));
        parser.parse();

        PDDocument originialPdfDoc = parser.getPDDocument();
        System.out.println(originialPdfDoc);

        AccessPermission accessPermission = new AccessPermission();

        accessPermission.setCanExtractContent(false);
        accessPermission.setCanModify(false);
        accessPermission.setCanPrint(false);
        accessPermission.setReadOnly();
        accessPermission.setCanAssembleDocument(false);
        accessPermission.setCanExtractForAccessibility(false);
        accessPermission.setCanPrintDegraded(false);
        originialPdfDoc.protect(new StandardProtectionPolicy("password", "password1", accessPermission));
        originialPdfDoc.save("E:\\Bookmarks1.pdf");
        originialPdfDoc.close();
    }
}
