package com.bxy.salestrategies.model;

import java.util.Date;

public class User {
	private int id;
	private String Name;
	private String Login_name;
	private String password;
	private String employee_number;
	private int gender;
	private int job_title;
	private int report_to;
	private String telephone;
	private String office_tel;
	private String fax;
	private String email;
	private int created_by;
	private Date create_date;
	private int modified_by;
	private Date modifier_date;
	public String getLogin_name() {
		return Login_name;
	}
	public void setLogin_name(String login_name) {
		Login_name = login_name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getEmployee_number() {
		return employee_number;
	}
	public void setEmployee_number(String employee_number) {
		this.employee_number = employee_number;
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public int getJob_title() {
		return job_title;
	}
	public void setJob_title(int job_title) {
		this.job_title = job_title;
	}
	public int getReport_to() {
		return report_to;
	}
	public void setReport_to(int report_to) {
		this.report_to = report_to;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getOffice_tel() {
		return office_tel;
	}
	public void setOffice_tel(String office_tel) {
		this.office_tel = office_tel;
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
	public int getCreated_by() {
		return created_by;
	}
	public void setCreated_by(int created_by) {
		this.created_by = created_by;
	}
	public Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
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
