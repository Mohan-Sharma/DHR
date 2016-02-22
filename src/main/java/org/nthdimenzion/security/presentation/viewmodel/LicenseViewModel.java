package org.nthdimenzion.security.presentation.viewmodel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.nthdimenzion.crud.ICrud;
import org.nthdimenzion.presentation.infrastructure.AbstractZKModel;
import org.nthdimenzion.presentation.infrastructure.Navigation;
import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.WireVariable;

import com.patientadmission.presentation.queries.PatientAdmissionFinder;

public class LicenseViewModel extends AbstractZKModel {

	private String licenseKey;
	@WireVariable
	private ICrud crudDao;
	@WireVariable
	private PatientAdmissionFinder patientAdmissionFinder;

	@WireVariable
	private DataSource dataSourceRef;
	
	@WireVariable
	private Navigation navigation;
	

	@AfterCompose
	public void init(@ContextParam(ContextType.VIEW) Component view, @BindingParam("content") String inPatientNumber) {
		Selectors.wireComponents(view, this, true);
	}

	@Command
	public void addLicenseKey(){
		Connection con = null;
		PreparedStatement prest;
		PreparedStatement deleteStmt;
		String sql=null;
		try{
			con = dataSourceRef.getConnection();
			try{
					String del = "DELETE FROM license";
				    sql = "INSERT INTO license(SerialNo,EncryptedMessage) VALUES (1,\"" + licenseKey + "\")";
				    deleteStmt = con.prepareStatement(del);
					prest = con.prepareStatement(sql);
					deleteStmt.executeUpdate();
				    prest.executeUpdate();
					navigation.redirect("login");}
				
			catch (SQLException s){
				s.printStackTrace();
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
		
	}



	public String getLicenseKey() {
		return licenseKey;
	}

	public void setLicenseKey(String licenseKey) {
		this.licenseKey = licenseKey;
	}
}
