package com.patientadmission.domain;

import org.nthdimenzion.crud.ICrudEntity;

import javax.persistence.*;

@Entity
@Table(name = "icd_10_code",uniqueConstraints = @UniqueConstraint(columnNames ={"ICD_CODE"}))
public class IcdElement implements ICrudEntity{

	private String icdCode;
	
	private String description;
	
	private String codeLevel;

	private String classification;
	
	private String whoCode;
	
	private String terminalNode;
	
	private String chapterId;
	
	private String blockId;
	
	private String icdCode2;
	
	private String mortality_1;
	
	private String mortality_2;
	
	private String mortality_3;
	
	private String mortality_4;
	
	private String morbidity;

    @Id
    @Column(name ="ICD_CODE",insertable = false,updatable=false)
	public String getIcdCode() {
		return icdCode;
	}

	public void setIcdCode(String icdCode) {
		this.icdCode = icdCode;
	}

	@Column(name = "ICD_TITLE",insertable = false,updatable=false)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "CODE_LEVEL",insertable = false,updatable=false)
	public String getCodeLevel() {
		return codeLevel;
	}

	public void setCodeLevel(String codeLevel) {
		this.codeLevel = codeLevel;
	}

	
	@Column(name = "CLASSIFICATION",insertable = false,updatable=false)
	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	@Column(name = "WHO_CODE",insertable = false,updatable=false)
	public String getWhoCode() {
		return whoCode;
	}

	public void setWhoCode(String whoCode) {
		this.whoCode = whoCode;
	}

	@Column(name = "TERMINAL_NODE",insertable = false,updatable=false)
	public String getTerminalNode() {
		return terminalNode;
	}

	public void setTerminalNode(String terminalNode) {
		this.terminalNode = terminalNode;
	}

	@Column(name = "CHAPTER_ID",insertable = false,updatable=false)
	public String getChapterId() {
		return chapterId;
	}

	public void setChapterId(String chapterId) {
		this.chapterId = chapterId;
	}

	@Column(name = "BLOCK_ID",insertable = false,updatable=false)
	public String getBlockId() {
		return blockId;
	}

	public void setBlockId(String blockId) {
		this.blockId = blockId;
	}

	@Column(name = "ICD_CODE_2",insertable = false,updatable=false)
	public String getIcdCode2() {
		return icdCode2;
	}

	public void setIcdCode2(String icdCode2) {
		this.icdCode2 = icdCode2;
	}

	@Column(name = "mortality_1",insertable = false,updatable=false)
	public String getMortality_1() {
		return mortality_1;
	}

	public void setMortality_1(String mortality_1) {
		this.mortality_1 = mortality_1;
	}

	@Column(name = "mortality_2",insertable = false,updatable=false)
	public String getMortality_2() {
		return mortality_2;
	}

	public void setMortality_2(String mortality_2) {
		this.mortality_2 = mortality_2;
	}

	@Column(name = "mortality_3",insertable = false,updatable=false)
	public String getMortality_3() {
		return mortality_3;
	}

	public void setMortality_3(String mortality_3) {
		this.mortality_3 = mortality_3;
	}

	@Column(name = "mortality_4",insertable = false,updatable=false)
	public String getMortality_4() {
		return mortality_4;
	}

	public void setMortality_4(String mortality_4) {
		this.mortality_4 = mortality_4;
	}
	
	@Column(name = "morbidity",insertable = false,updatable=false)
	public String getMorbidity() {
		return morbidity;
	}

	public void setMorbidity(String morbidity) {
		this.morbidity = morbidity;
	}


}
