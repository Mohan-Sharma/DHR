package com.patientadmission.application.handlers.servlets;

import com.patientadmission.presentation.queries.PatientAdmissionFinder;
import org.nthdimenzion.object.utils.UtilValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.HttpRequestHandler;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Map;


public class DocumentDownloadServlet implements HttpRequestHandler {

    private static final String PDF_CONTENT_TYPE = "application/pdf";

    private static final int BUFSIZE = 4096;

    @Autowired
    private PatientAdmissionFinder patientAdmissionFinder;


    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String inPatientNumber = request.getParameter("inPatientNumber");

        Map<String, ?> map = patientAdmissionFinder.findFileNameBy(inPatientNumber);
        String filePath = (String) map.get("FILE_PATH");
        if (UtilValidator.isEmpty(filePath))
            return;
        String fileName =  (String) map.get("FILE_NAME");
        File caseSheet = new File(filePath);
        FileInputStream fileInputStream = new FileInputStream(caseSheet);
        response.setContentType(PDF_CONTENT_TYPE);
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        byte[] byteBuffer = new byte[BUFSIZE];
        DataInputStream dataInputStream = new DataInputStream(fileInputStream);
        int length   = 0;
        OutputStream outputStream = response.getOutputStream();
        // reads the file's bytes and writes them to the response stream
        while ((dataInputStream != null) && ((length = dataInputStream.read(byteBuffer)) != -1))
        {
            outputStream.write(byteBuffer,0,length);
        }
        dataInputStream.close();
        outputStream.close();
    }



}
