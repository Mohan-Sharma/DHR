package com.patientadmission.presentation;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.nthdimenzion.crud.ICrud;
import org.nthdimenzion.object.utils.UtilValidator;
import org.nthdimenzion.presentation.annotations.Composer;
import org.nthdimenzion.presentation.infrastructure.AbstractZKModel;
import org.nthdimenzion.security.domain.IUserLoginRepository;
import org.nthdimenzion.security.domain.IsADoctor;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;

import com.patientadmission.domain.Doctor;
import com.patientadmission.presentation.queries.PatientAdmissionFinder;

@Composer
@VariableResolver(DelegatingVariableResolver.class)
public class FileViewerModel extends AbstractZKModel{

	@WireVariable
	private PatientAdmissionFinder patientAdmissionFinder;

	@WireVariable
	private ICrud crudDao;

	@WireVariable
	private IUserLoginRepository userLoginRepository;

	private IsADoctor isADoctor;
	private String filePath;
	private String contentType;
	private String inPatientNumber;

	@Wire("#errorLabel")
	private Label errorLabel;

	/*@Wire("#downloadButton")
	private Button downloadButton;*/

	public void setUserRepository(IUserLoginRepository userRepository) {
		this.userLoginRepository = userRepository;
	}

	@AfterCompose
	public void init(@ContextParam(ContextType.VIEW) Component view, @BindingParam("content") String inPatientNumber) {
		Selectors.wireComponents(view, this, true);
		isADoctor = userLoginRepository.findUserLoginWithUserName(loggedInUser.getUsername());
		Map<String, ?> map = patientAdmissionFinder.findFileNameBy(inPatientNumber);
		this.inPatientNumber = inPatientNumber;
		filePath = (String) map.get("FILE_PATH");
		contentType = (String) map.get("CONTENT_TYPE");
		if (filePath == null)
		{	
			errorLabel.setValue("Case Sheet Not Attatched! Contact Administrator");
			errorLabel.setVisible(true);
		}

		/*if(isADoctor.isADoctor()){
		
			downloadButton.setVisible(true);
			
		}*/
		/*else {
			downloadButton.setVisible(false);
		}*/
		
	}

	public PatientAdmissionFinder getPatientAdmissionFinder() {
		return patientAdmissionFinder;
	}

	public ICrud getCrudDao() {
		return crudDao;
	}

	public void setCrudDao(ICrud crudDao) {
		this.crudDao = crudDao;
	}

	@Command
	public void downloadDocument() throws IOException {

		Doctor doctor = crudDao.find(Doctor.class,isADoctor.getDoctorId());
		try {
			DocumentViewHelper.downloadPdf(filePath,contentType,doctor.getAdditionalPassword());
			// DocumentViewHelper.openDocumentInNewWindow(navigation,inPatientNumber);

		} catch (Exception e) {
			logger.error("Could not download case sheet for " + inPatientNumber,e);
			errorLabel.setValue("Cannot Download Case Sheet! Contact Administrator");
			errorLabel.setVisible(true);
		}
	}
}
