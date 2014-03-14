package com.bxy.salestrategies.model;

public class Opportunitycontactteam {
	private int id;
	private String name;
	private String rank;
	private int ranklevel;
	private int report_to;
	//是否为核心
	private int core;
	//线
	private int influence_to;
	private int gender;
	private int decision_role;
	private int buying_style;
	private int relat_status;
	private int time_spent;
	private int contact_id;
	private String decisionName;
	private String buyingStyleName;
	private String relationStatusName;
	private String tiemSpentName;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRank() {
		return rank;
	}
	public void setRank(String rank) {
		this.rank = rank;
	}
	public int getRanklevel() {
		return ranklevel;
	}
	public void setRanklevel(int ranklevel) {
		this.ranklevel = ranklevel;
	}
	
	public int getReport_to() {
		return report_to;
	}
	public void setReport_to(int report_to) {
		this.report_to = report_to;
	}
	public int getCore() {
		return core;
	}
	public void setCore(int core) {
		this.core = core;
	}
	
	public int getInfluence_to() {
		return influence_to;
	}
	public void setInfluence_to(int influence_to) {
		this.influence_to = influence_to;
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	
	public int getContact_id() {
		return contact_id;
	}
	public void setContact_id(int contact_id) {
		this.contact_id = contact_id;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDecisionName() {
		return decisionName;
	}
	public void setDecisionName(String decisionName) {
		this.decisionName = decisionName;
	}
	public String getRelationStatusName() {
		return relationStatusName;
	}
	public void setRelationStatusName(String relationStatusName) {
		this.relationStatusName = relationStatusName;
	}
	public String getTiemSpentName() {
		return tiemSpentName;
	}
	public void setTiemSpentName(String tiemSpentName) {
		this.tiemSpentName = tiemSpentName;
	}
	public int getDecision_role() {
		return decision_role;
	}
	public void setDecision_role(int decision_role) {
		this.decision_role = decision_role;
	}
	public int getBuying_style() {
		return buying_style;
	}
	public void setBuying_style(int buying_style) {
		this.buying_style = buying_style;
	}
	
	public int getTime_spent() {
		return time_spent;
	}
	public void setTime_spent(int time_spent) {
		this.time_spent = time_spent;
	}
	public String getBuyingStyleName() {
		return buyingStyleName;
	}
	public void setBuyingStyleName(String buyingStyleName) {
		this.buyingStyleName = buyingStyleName;
	}
	public int getRelat_status() {
		return relat_status;
	}
	public void setRelat_status(int relat_status) {
		this.relat_status = relat_status;
	}
	
};

