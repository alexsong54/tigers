package com.bxy.salestrategies;

import java.util.Map;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.ajax.markup.html.AjaxEditableMultiLineLabel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.bxy.salestrategies.db.DAOImpl;
import com.bxy.salestrategies.model.Dna;
import com.bxy.salestrategies.model.DnaImplement;
import com.bxy.salestrategies.model.Summary;
import com.bxy.salestrategies.model.Swot;
import com.google.common.collect.Maps;

public class SwotPanel extends Panel{

	private static final long serialVersionUID = 7050475224663541217L;
	final Map<String, IModel> models = Maps.newHashMap();

	//private String  
	public SwotPanel(String id,final String opportunityID) {
		super(id);
		//根据id获取dna和dnaimplement
		Swot swot = DAOImpl.getSwotByOpportunityId(opportunityID);
		IModel<String> textModel = null;
		IModel<String> textModel_dna_pop = null;
		IModel<String> textModel_dna_qbu = null;
		IModel<String> textModel_dna_cco = null;
	    if(null==swot){
	    	textModel = new Model<String>("");
	    	textModel_dna_pop = new Model<String>("");
	    	textModel_dna_qbu = new Model<String>("");
	    	textModel_dna_cco = new Model<String>("");
	    }else{
	    	textModel = new Model<String>(swot.getStrengths());
	    	textModel_dna_pop = new Model<String>(swot.getWeaknesses());
	    	textModel_dna_qbu = new Model<String>(swot.getOpportunities());
	    	textModel_dna_cco = new Model<String>(swot.getThreats());
	    }
		Form form = new Form("form") {
	            @Override
	            protected void onSubmit() {
	            	 String dna_tta = models.get("strengths").getObject() == null ? null : String.valueOf(models.get("strengths").getObject());
	            	 String dna_pop = models.get("weaknesses").getObject() == null ? null : String.valueOf(models.get("weaknesses").getObject());
	            	 String dna_cco = models.get("opportunities").getObject() == null ? null : String.valueOf(models.get("opportunities").getObject());
	            	 String dna_cpa = models.get("threats").getObject() == null ? null : String.valueOf(models.get("threats").getObject());
	            	 try {
	            		 Swot swot = DAOImpl.getSwotByOpportunityId(opportunityID);
						if(null == swot){
							 //新增信息
							 DAOImpl.addSwot(opportunityID, dna_tta, dna_pop, dna_cco, dna_cpa);
						 }else{
							 DAOImpl.updateSwotById(String.valueOf(swot.getId()),dna_tta,dna_pop,dna_cco,dna_cpa);
						 }
					} catch (Exception e) {
						e.printStackTrace();
					}
	            }
		};
		AjaxEditableMultiLineLabel dna_tta = new AjaxEditableMultiLineLabel("strengths", textModel);
		models.put("strengths", textModel);
		form.add(dna_tta);
		
		AjaxEditableMultiLineLabel dna_pop = new AjaxEditableMultiLineLabel("weaknesses", textModel_dna_pop);
		models.put("weaknesses", textModel_dna_pop);
		form.add(dna_pop);
		
		AjaxEditableMultiLineLabel dna_qbu = new AjaxEditableMultiLineLabel("opportunities", textModel_dna_qbu);
		models.put("opportunities", textModel_dna_qbu);
		form.add(dna_qbu);
		
		AjaxEditableMultiLineLabel dna_cco = new AjaxEditableMultiLineLabel("threats", textModel_dna_cco);
		models.put("threats", textModel_dna_cco);
		form.add(dna_cco);
		add(form);
		form.add(new Label("title","策略分析"));
		Summary summary = DAOImpl.getSummaryByOpportunityId(opportunityID);
		IModel<String> textModel_dnaimplement_tta = null;
		IModel<String> textModel_dnaimplement_pop = null;
		IModel<String> textModel_dnaimplement_qbu = null;
		IModel<String> textModel_dnaimplement_cco = null;
		if(null==summary){
			 textModel_dnaimplement_tta = new Model("");
			 textModel_dnaimplement_pop = new Model("");
			 textModel_dnaimplement_qbu = new Model("");
			 textModel_dnaimplement_cco = new Model("");
		}else{
			textModel_dnaimplement_tta = new Model<String>(summary.getVertial_market());
			textModel_dnaimplement_pop = new Model<String>(summary.getCompelling_mechanism());
			textModel_dnaimplement_qbu = new Model<String>(summary.getOur_solution());
			textModel_dnaimplement_cco = new Model<String>(summary.getOur_quantified());
		}
		Form dnaImplementForm = new Form("summaryForm") {
	            @Override
	            protected void onSubmit() {
	            	 String dna_tta = models.get("vertial_market").getObject() == null ? null : String.valueOf(models.get("vertial_market").getObject());
	            	 String dna_pop = models.get("compelling_mechanism").getObject() == null ? null : String.valueOf(models.get("compelling_mechanism").getObject());
	            	 String dna_qbu = models.get("our_solution").getObject() == null ? null : String.valueOf(models.get("our_solution").getObject());
	            	 String dna_cco = models.get("our_quantified").getObject() == null ? null : String.valueOf(models.get("our_quantified").getObject());
	            	 try {
	            		 Summary summary = DAOImpl.getSummaryByOpportunityId(opportunityID);
							if(null == summary){
								 //新增信息
								 DAOImpl.addSummary(opportunityID, dna_tta, dna_pop, dna_qbu, dna_cco);
							 }else{
								 DAOImpl.updateSummaryById(String.valueOf(summary.getId()), dna_tta, dna_pop, dna_qbu, dna_cco);
							 }
						} catch (Exception e) {
							e.printStackTrace();
						}
	            }
		};
		AjaxEditableMultiLineLabel dnaimplement_tta = new AjaxEditableMultiLineLabel("vertial_market", textModel_dnaimplement_tta);
		models.put("vertial_market", textModel_dnaimplement_tta);
		dnaImplementForm.add(dnaimplement_tta);
		
		AjaxEditableMultiLineLabel dnaimplement_pop = new AjaxEditableMultiLineLabel("compelling_mechanism", textModel_dnaimplement_pop);
		models.put("compelling_mechanism", textModel_dnaimplement_pop);
		dnaImplementForm.add(dnaimplement_pop);
		
		AjaxEditableMultiLineLabel dnaimplement_qbu = new AjaxEditableMultiLineLabel("our_solution", textModel_dnaimplement_qbu);
		models.put("our_solution", textModel_dnaimplement_qbu);
		dnaImplementForm.add(dnaimplement_qbu);
		
		AjaxEditableMultiLineLabel dnaimplement_cco = new AjaxEditableMultiLineLabel("our_quantified", textModel_dnaimplement_cco);
		models.put("our_quantified", textModel_dnaimplement_cco);
		dnaImplementForm.add(dnaimplement_cco);
		
		add(dnaImplementForm);
		dnaImplementForm.add(new Label("summaryName","总结分析"));
    }
    public SwotPanel(String id, IModel<?> model) {
        super(id, model);
    }
}
