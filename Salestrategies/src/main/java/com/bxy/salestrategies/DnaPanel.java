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
import com.google.common.collect.Maps;

public class DnaPanel extends Panel{

	private static final long serialVersionUID = 7050475224663541217L;
	final Map<String, IModel> models = Maps.newHashMap();

	//private String  
	public DnaPanel(String id,final String dnaID,final String dna_implementID) {
		super(id);
		//根据id获取dna和dnaimplement
		Dna dna = DAOImpl.getDnaByOpportunityId(dnaID);
		add(new Label("title","The Strategic Selling DNA Strand "));
		Form form = new Form("form") {
	            @Override
	            protected void onSubmit() {
	            	 String dna_tta = models.get("dna_tta").getObject() == null ? null : String.valueOf(models.get("dna_tta").getObject());
	            	 String dna_pop = models.get("dna_pop").getObject() == null ? null : String.valueOf(models.get("dna_pop").getObject());
	            	 String dna_qbu = models.get("dna_qbu").getObject() == null ? null : String.valueOf(models.get("dna_qbu").getObject());
	            	 String dna_cco = models.get("dna_cco").getObject() == null ? null : String.valueOf(models.get("dna_cco").getObject());
	            	 String dna_cpa = models.get("dna_cpa").getObject() == null ? null : String.valueOf(models.get("dna_cpa").getObject());
	                 if(DAOImpl.updatednaById(dnaID, dna_tta, dna_pop, dna_qbu, dna_cco, dna_cpa)){
	                	 System.out.println("修改成功");
	                 };
	            }
		};
		IModel<String> textModel = new Model<String>(dna.getTemporary_technichal_advantage());
		AjaxEditableMultiLineLabel dna_tta = new AjaxEditableMultiLineLabel("dna_tta", textModel);
		models.put("dna_tta", textModel);
		form.add(dna_tta);
		IModel<String> textModel_dna_pop = new Model<String>(dna.getProjects_or_problem());
		AjaxEditableMultiLineLabel dna_pop = new AjaxEditableMultiLineLabel("dna_pop", textModel_dna_pop);
		models.put("dna_pop", textModel_dna_pop);
		form.add(dna_pop);
		IModel<String> textModel_dna_qbu = new Model<String>(dna.getQuantifiable_business_outcome());
		AjaxEditableMultiLineLabel dna_qbu = new AjaxEditableMultiLineLabel("dna_qbu", textModel_dna_qbu);
		models.put("dna_qbu", textModel_dna_qbu);
		form.add(dna_qbu);
		IModel<String> textModel_dna_cco = new Model<String>(dna.getCustomer_coals_and_objectives());
		AjaxEditableMultiLineLabel dna_cco = new AjaxEditableMultiLineLabel("dna_cco", textModel_dna_cco);
		models.put("dna_cco", textModel_dna_cco);
		form.add(dna_cco);
		IModel<String> textModel_dna_cpa = new Model<String>(dna.getCustomer_political_agenda());
		AjaxEditableMultiLineLabel dna_cpa = new AjaxEditableMultiLineLabel("dna_cpa", textModel_dna_cpa);
		
		models.put("dna_cpa", textModel_dna_cpa);
		form.add(dna_cpa);
		add(form);
		
		DnaImplement DnaImplement = DAOImpl.getDnaImplementByOpptunityId(dna_implementID);
		Form dnaImplementForm = new Form("dnaImplementForm") {
	            @Override
	            protected void onSubmit() {
	            	 String dna_tta = models.get("dnaimplement_tta").getObject() == null ? null : String.valueOf(models.get("dnaimplement_tta").getObject());
	            	 String dna_pop = models.get("dnaimplement_pop").getObject() == null ? null : String.valueOf(models.get("dnaimplement_pop").getObject());
	            	 String dna_qbu = models.get("dnaimplement_qbu").getObject() == null ? null : String.valueOf(models.get("dnaimplement_qbu").getObject());
	            	 String dna_cco = models.get("dnaimplement_cco").getObject() == null ? null : String.valueOf(models.get("dnaimplement_cco").getObject());
	            	 String dna_cpa = models.get("dnaimplement_cpa").getObject() == null ? null : String.valueOf(models.get("dnaimplement_cpa").getObject());
	                 if(DAOImpl.updatednaImplementById(dna_implementID, dna_tta, dna_pop, dna_qbu, dna_cco, dna_cpa)){
	                	 System.out.println("修改成功");
	                 };
	            }
		};
		IModel<String> textModel_dnaimplement_tta = new Model<String>(DnaImplement.getTemporary_technichal_advantage());
		AjaxEditableMultiLineLabel dnaimplement_tta = new AjaxEditableMultiLineLabel("dnaimplement_tta", textModel_dnaimplement_tta);
		models.put("dnaimplement_tta", textModel_dnaimplement_tta);
		dnaImplementForm.add(dnaimplement_tta);
		IModel<String> textModel_dnaimplement_pop = new Model<String>(DnaImplement.getProjects_or_problem());
		AjaxEditableMultiLineLabel dnaimplement_pop = new AjaxEditableMultiLineLabel("dnaimplement_pop", textModel_dnaimplement_pop);
		models.put("dnaimplement_pop", textModel_dnaimplement_pop);
		dnaImplementForm.add(dnaimplement_pop);
		IModel<String> textModel_dnaimplement_qbu = new Model<String>(DnaImplement.getQuantifiable_business_outcome());
		AjaxEditableMultiLineLabel dnaimplement_qbu = new AjaxEditableMultiLineLabel("dnaimplement_qbu", textModel_dnaimplement_qbu);
		models.put("dnaimplement_qbu", textModel_dnaimplement_qbu);
		dnaImplementForm.add(dnaimplement_qbu);
		IModel<String> textModel_dnaimplement_cco = new Model<String>(DnaImplement.getCustomer_coals_and_objectives());
		AjaxEditableMultiLineLabel dnaimplement_cco = new AjaxEditableMultiLineLabel("dnaimplement_cco", textModel_dnaimplement_cco);
		models.put("dnaimplement_cco", textModel_dnaimplement_cco);
		dnaImplementForm.add(dnaimplement_cco);
		IModel<String> textModel_dnaimplement_cpa = new Model<String>(DnaImplement.getCustomer_political_agenda());
		AjaxEditableMultiLineLabel dnaimplement_cpa = new AjaxEditableMultiLineLabel("dnaimplement_cpa", textModel_dnaimplement_cpa);
		models.put("dnaimplement_cpa", textModel_dnaimplement_cpa);
		dnaImplementForm.add(dnaimplement_cpa);
		add(dnaImplementForm);
       
    }
    public DnaPanel(String id, IModel<?> model) {
        super(id, model);
    }
}
