package com.bxy.salestrategies;

import java.util.List;
import java.util.Map;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.ajax.markup.html.AjaxEditableMultiLineLabel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.bxy.salestrategies.common.Entity;
import com.bxy.salestrategies.db.DAOImpl;
import com.bxy.salestrategies.model.Contact;
import com.bxy.salestrategies.model.Dna;
import com.bxy.salestrategies.model.DnaImplement;
import com.bxy.salestrategies.model.Opportunitycontactteam;
import com.bxy.salestrategies.util.Configuration;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

public class PoliticalPanel extends Panel{

	private static final long serialVersionUID = 7050475224663541217L;
	final Map<String, IModel> models = Maps.newHashMap();

	//private String  
	public PoliticalPanel(String id,final String opportunityID) {
		super(id);
		//读取数据
		 Map<String, Entity> entities = Configuration.getEntityTable();
		 String sql = "select * from opportunitycontactteam where opportunity_id = "+opportunityID;
		 List<Opportunitycontactteam> tdata = DAOImpl.queryOppContactTeams(sql);
		 for(Opportunitycontactteam team :tdata ){
			 //职位ID 
			 Contact contact = DAOImpl.getContactById(team.getContact_id());
			 int job = contact.getJob_title();
			 team.setName(contact.getName());
			 team.setRank(DAOImpl.getChoiceById("job_title_pl",job).getVal());
			 team.setGender(contact.getGender());
			 if(0==team.getBuying_style()||0==team.getDecision_role()||0==team.getRelat_status()||0==team.getTime_spent()){
				 team.setBuyingStyleName("");
				 team.setDecisionName("");
				 team.setRelationStatusName("");
				 team.setTiemSpentName("");
			 }else{
				 team.setBuyingStyleName(DAOImpl.getChoiceById("buying_style",team.getBuying_style()).getName());
				 team.setDecisionName(DAOImpl.getChoiceById("descision",team.getDecision_role()).getName());
				 team.setRelationStatusName(DAOImpl.getChoiceById("relation_status",team.getRelat_status()).getName());
				 team.setTiemSpentName(DAOImpl.getChoiceById("time_spent_pl",team.getTime_spent()).getName());
			 }
			 if(0!=team.getInfluence_to()){
				 team.setCore(team.getId());
			 }
		 }
		 TextField<String> text = new TextField<String>("tdata");
		 Gson gson = new Gson();
		 text.add(new AttributeAppender("value",gson.toJson(tdata)));
		 add(text);
    }
    public PoliticalPanel(String id, IModel<?> model) {
        super(id, model);
    }
}
