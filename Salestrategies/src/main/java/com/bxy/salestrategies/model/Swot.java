package com.bxy.salestrategies.model;

public class Swot {
	private int id;
	private int opportunity_id;
	private String strengths;
	private String weaknesses;
	private String opportunities;
	private String threats;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getStrengths() {
		return strengths;
	}
	public void setStrengths(String strengths) {
		this.strengths = strengths;
	}
	public String getWeaknesses() {
		return weaknesses;
	}
	public void setWeaknesses(String weaknesses) {
		this.weaknesses = weaknesses;
	}
	public String getOpportunities() {
		return opportunities;
	}
	public void setOpportunities(String opportunities) {
		this.opportunities = opportunities;
	}
	public String getThreats() {
		return threats;
	}
	public void setThreats(String threats) {
		this.threats = threats;
	}
	public int getOpportunity_id() {
		return opportunity_id;
	}
	public void setOpportunity_id(int opportunity_id) {
		this.opportunity_id = opportunity_id;
	}
	
}
