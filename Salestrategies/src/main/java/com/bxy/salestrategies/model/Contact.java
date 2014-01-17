package com.bxy.salestrategies.model;

import java.util.Date;

public class Contact {

	private int id;
	private String name;
	private int account_id;
	private int department;
	private int status;
	private String office_tel;
	private int job_title;
	private String telephone;
	private String fax;
	private String email;
	private int gender ;
	private int reportto;
	private int create_by;
	private Date create_date;
	private int owner;
	private int modified_by;
	private Date modifier_date;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAccount_id() {
		return account_id;
	}
	public void setAccount_id(int account_id) {
		this.account_id = account_id;
	}
	public int getDepartment() {
		return department;
	}
	public void setDepartment(int department) {
		this.department = department;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getOffice_tel() {
		return office_tel;
	}
	public void setOffice_tel(String office_tel) {
		this.office_tel = office_tel;
	}
	public int getJob_title() {
		return job_title;
	}
	public void setJob_title(int job_title) {
		this.job_title = job_title;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public int getReportto() {
		return reportto;
	}
	public void setReportto(int reportto) {
		this.reportto = reportto;
	}
	public int getCreate_by() {
		return create_by;
	}
	public void setCreate_by(int create_by) {
		this.create_by = create_by;
	}
	public Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}
	public int getOwner() {
		return owner;
	}
	public void setOwner(int owner) {
		this.owner = owner;
	}
	public int getModified_by() {
		return modified_by;
	}
	public void setModified_by(int modified_by) {
		this.modified_by = modified_by;
	}
	public Date getModifier_date() {
		return modifier_date;
	}
	public void setModifier_date(Date modifier_date) {
		this.modifier_date = modifier_date;
	}
}
