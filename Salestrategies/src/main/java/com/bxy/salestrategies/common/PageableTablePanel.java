package com.bxy.salestrategies.common;

import java.io.File;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.DownloadLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.AbstractItem;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.IRequestCycle;
import org.apache.wicket.request.handler.resource.ResourceStreamRequestHandler;
import org.apache.wicket.request.resource.ContentDisposition;
import org.apache.wicket.util.encoding.UrlEncoder;
import org.apache.wicket.util.file.Files;
import org.apache.wicket.util.resource.FileResourceStream;
import org.apache.wicket.util.resource.IResourceStream;

import com.bxy.salestrategies.db.DAOImpl;
import com.bxy.salestrategies.util.CRMUtility;
import com.bxy.salestrategies.util.Utility;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class PageableTablePanel extends Panel{
	private static final long serialVersionUID = 2501105233172820074L;
    private static final Logger logger = Logger.getLogger(PageableTablePanel.class);
	public PageableTablePanel(String id, final Entity entity, final List mapList,final Map<String,Object> params) {
	        super(id);
	        add(new Label("table_title",entity.getDisplay()));
	        final String primaryKeyName = entity.getPrimaryKeyName();
	        final List<Field> fields = entity.getFields();
	        final List<String> fn = entity.getFieldNames();
	        final String entityName = entity.getName();
	        // RepeatingView dataRowRepeater = new RepeatingView("dataRowRepeater");
	        // add(dataRowRepeater);

	        final Map<String, Map> tableData = Maps.newHashMap();
	        List<String> ids = Lists.newArrayList();
	        for (Map map : (List<Map>) mapList) {
	            String key = String.valueOf(map.get("id"));
	            ids.add(key);
	            tableData.put(key, map);
	        }

	        final WebMarkupContainer datacontainer = new WebMarkupContainer("data");
	        datacontainer.setOutputMarkupId(true);
	        add(datacontainer);

	        // set column name
	        RepeatingView columnNameRepeater = new RepeatingView("columnNameRepeater");
	        datacontainer.add(columnNameRepeater);
	        int count=0;
	        for (Field f : entity.getFields()) {
	            if (!f.isVisible()|| f.getPriority() >1)
	                continue;
	            AbstractItem item = new AbstractItem(columnNameRepeater.newChildId());
	            if(count==0){
	                item.add(new AttributeAppender("class", new Model("table-first-link"), " "));
	                count++;
	            }
	            columnNameRepeater.add(item);
	            item.add(new Label("columnName", f.getDisplay()));
	        }
	        // end of set column name
	        final PageableListView<String> listview = new PageableListView<String>("dataRowRepeater", ids, 15) {
	            
	            @Override          
	            protected void populateItem(ListItem<String> item) {
	                String key = item.getDefaultModelObjectAsString();
	                RepeatingView columnRepeater = new RepeatingView("columnRepeater");
	                Map map = tableData.get(key);
	                item.add(columnRepeater);
	                final String rowId = String.valueOf(map.get(primaryKeyName));
	                for (Field f : fields) {
	                    if (!f.isVisible() || f.getPriority() >1)
	                        continue;
	                    AbstractItem columnitem = new AbstractItem(columnRepeater.newChildId(), new Model() {
	                        @Override
	                        public Serializable getObject() {
	                            Param p = new Param();
	                            p.setId(rowId);
	                            p.setEntityName(entityName);
	                            return p;
	                        }

	                    });
	                    if (f.isDetailLink()) {
	                        
	                    	String value = Utility.formatValue(f.getFormatter(), String.valueOf(map.get(f.getName())));	
	                        if(value.equals("null")||value.isEmpty()){
	                          value = "无";
	                        }
	                        columnitem.add(new DetailLinkFragment("celldata", "detailFragment", this.getParent().getParent(), value,entity));
	                        columnitem.add(new AttributeAppender("class", new Model("table-first-link"), " "));
//	                        columnitem.add(new ButtonFragment("celldata","buttonFragment",this,"鍒犻櫎"));
	                    } else {
	                        if (f.getPicklist() != null) {
	                            // get option from picklist
	                            String value = Utility.formatValue(f.getFormatter(), DAOImpl.queryPickListByIdCached(f.getPicklist(), String.valueOf(map.get(f.getName()))));
	                            if(value.equals("null")||value.isEmpty()){
	                              value = "无";
	                            }
	                            columnitem.add(new Label("celldata", value));
	                        } else if(f.getRelationTable() != null){
	                            String value = Utility.formatValue(f.getFormatter(), DAOImpl.queryCachedRelationDataById(f.getRelationTable(), String.valueOf(map.get(f.getName()))));
	                            if(value.equals("null")||value.isEmpty()){
	                              value = "无";
	                            }
	                            columnitem.add(new Label("celldata", value));
	                        }else {
	                           
	                             String value = Utility.formatValue(f.getFormatter(), String.valueOf(map.get(f.getName())));
	                            final String filepath =Utility.formatValue(f.getFormatter(), String.valueOf(map.get(f.getName())));
	                            
	                            if(f.getDataType().equalsIgnoreCase("downloadlink")){
	                                columnitem.add(new DownloadLinkFragment("celldata", "downloadlinkFragment", this.getParent().getParent(), value));
	                                
	                            }else{
	                              if(value.equals("null")||value.isEmpty()){
	                                 value = "无";
	                               }
	                               columnitem.add(new Label("celldata", value));
	                            }
	                        }
	                    }
	                    columnRepeater.add(columnitem);
	                }
	                
	            }
	        };
	        
	        datacontainer.add(listview);
	        //PagingNavigator nav = new PagingNavigator("navigator", listview);
	        AjaxPagingNavigator nav =new AjaxPagingNavigator("navigator", listview);
	        nav.setOutputMarkupId(true); 

	        datacontainer.add(nav);
	        datacontainer.setVersioned(false);
	        ICRUDActionListener actionListener = new DefaultCRUDActionListener(){
	            @Override
	            public void create() { 
	            	if(entity.getName().equals("target_acquisition")){
	            		setResponsePage(new CreateDataPage(entity.getName(),params));         
	            	}else{
	            		setResponsePage(new CreateDataPage(entity.getName(),params));     
	            	}
	            }
	            @Override

	            public void downLoadBtn() throws Exception {     
	                String exported_filename = null;
	                try {
//	                    exported_filename = DataExporter.export(entity.getName(), mapList);
	                } catch (Exception e) {
	                    logger.debug("failed to export file for " + entity.getName(), e);
	                }
	                if (exported_filename != null) {

	                    final File file = new File(exported_filename);
	                    String filename="";
	                    if(entity.getName().equalsIgnoreCase("crmuser")){
	                        filename="岗位信息";
	                    }else if(entity.getName().equalsIgnoreCase("account")){
	                        filename="医院";
	                    }else if(entity.getName().equalsIgnoreCase("contact")){
	                        filename="医生";
	                    }else if(entity.getName().equalsIgnoreCase("userinfo")){
	                        filename="用户";
	                    }else if(entity.getName().equalsIgnoreCase("activity")){
	                        filename="活动";
	                    }
	                    
	                    String display_fileName = UrlEncoder.QUERY_INSTANCE.encode("导出_"+filename+".zip", Charset.forName("UTF-8"));

	                    IResourceStream resourceStream = new FileResourceStream(new org.apache.wicket.util.file.File(file));
	                    getRequestCycle().scheduleRequestHandlerAfterCurrent(new ResourceStreamRequestHandler(resourceStream) {
	                        @Override
	                        public void respond(IRequestCycle requestCycle) {
	                            super.respond(requestCycle);
	                            Files.remove(file);
	                        }
	                    }.setFileName(display_fileName).setContentDisposition(ContentDisposition.ATTACHMENT));

	                } else {
	                    logger.debug("Failed to export file for:" + entity.getName());

	                }
	            

	            }
				@Override
				public void merge() {
					// TODO Auto-generated method stub
					
				}
				@Override
				public void ineffective() {
					// TODO Auto-generated method stub
					
				}    
	        };
	        add(new CRUDPanel("operationBar",entity.getName(),null, CRMUtility.getPermissionOfEntityList(entity.getName()),actionListener));
	    }

	    public PageableTablePanel(String id, IModel<?> model) {
	        super(id, model);
	    }

	    
	    private class DownloadLinkFragment extends Fragment {
	        public DownloadLinkFragment( String id, String markupId, MarkupContainer markupProvider,final String filename) {
	            super(id, markupId, markupProvider);
	            final File tempFile = new File(filename);
	            DownloadLink downloadlink = new DownloadLink("downloadlink",
	                    new AbstractReadOnlyModel<File>(){

	                        @Override
	                        public File getObject() {
	                            

	                            return tempFile;
	                        }
	               
	            },tempFile.getName());
	            downloadlink.add(new Label("fn",tempFile.getName()));
	            
	            add(downloadlink);
	            
	        }
	        
	    }
	    
	    private class DetailLinkFragment extends Fragment {
	        /**
	         * Construct.
	         * 
	         * @param id
	         *            The component Id
	         * @param markupId
	         *            The id in the markup
	         * @param markupProvider
	         *            The markup provider
	         */
	        public DetailLinkFragment( String id, String markupId, MarkupContainer markupProvider, String caption, final Entity entity) {
	            super(id, markupId, markupProvider);
	            add(new Link("detailclick") {

	                @Override
	                public void onClick() {
	                    Param p = (Param) getParent().getParent().getDefaultModelObject();
	                    System.out.println(p + " id:" + p.getId() + " name:" + p.getEntityName());
	                    String entityName = p.getEntityName();
	                    setResponsePage(new EntityDetailPage(entityName, p.getId()));
	                }
	            }.add(new Label("caption", new Model<String>(caption))));
	        }
	        
	    }
}
