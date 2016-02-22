package com.patientadmission.domain;

import org.nthdimenzion.crud.ICrudEntity;

import javax.persistence.*;

@Entity
@Table(name = "cpt_9_cm", uniqueConstraints = @UniqueConstraint(columnNames ={"MainID4"}))
public class Procedure implements ICrudEntity {

	private String cptCode;
	
	private String shortDescription;
	
	private String longDescription;
	
	private String description;
	
	private String mainId1;
	
	private String mainId2;
	
	private String mainId3;
	
	private String level1CategoryText;
	
	private String level2CategoryText;
	
	private String level2Header;
	
	private Long IDNum;
	
	@Column(name="MainID4",insertable = false,updatable = false)
	public String getCptCode() {
		return cptCode;
	}

	public void setCptCode(String cptCode) {
		this.cptCode = cptCode;
	}

	@Lob
	@Column(name = "DescriptionL2",insertable = false,updatable = false)
	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	@Lob
	@Column(name = "Description",insertable = false,updatable = false)
	public String getLongDescription() {
		return longDescription;
	}

	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}

	@Lob
	@Column(name = "Text",insertable = false,updatable = false)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "MainID1",insertable = false,updatable = false)
	public String getMainId1() {
		return mainId1;
	}

	public void setMainId1(String mainId1) {
		this.mainId1 = mainId1;
	}

	@Column(name = "MainID2",insertable = false,updatable = false)
	public String getMainId2() {
		return mainId2;
	}

	public void setMainId2(String mainId2) {
		this.mainId2 = mainId2;
	}

	@Column(name = "MainID3",insertable = false,updatable = false)
	public String getMainId3() {
		return mainId3;
	}

	public void setMainId3(String mainId3) {
		this.mainId3 = mainId3;
	}

	@Column(name = "Level1CategoryText",insertable = false,updatable = false)
	public String getLevel1CategoryText() {
		return level1CategoryText;
	}

	public void setLevel1CategoryText(String level1CategoryText) {
		this.level1CategoryText = level1CategoryText;
	}

	@Column(name = "Level2CategoryText",insertable = false,updatable = false)
	public String getLevel2CategoryText() {
		return level2CategoryText;
	}

	public void setLevel2CategoryText(String level2CategoryText) {
		this.level2CategoryText = level2CategoryText;
	}

	public String getLevel2Header() {
		return level2Header;
	}

	public void setLevel2Header(String level2Header) {
		this.level2Header = level2Header;
	}

	@Id
	@Column(name = "IDNum",insertable = false,updatable = false)
	public Long getIDNum() {
		return IDNum;
	}

	public void setIDNum(Long iDNum) {
		IDNum = iDNum;
	}
}
