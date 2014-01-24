package com.bxy.salestrategies.common;

import java.io.Serializable;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

public class Field implements Serializable
{

  @Expose
  private boolean isPrimaryKey;
  @Expose
  private String id;
  @Expose
  private boolean isRequired;
  @Expose
  private String display;
  private String dataType;
  @Expose
  private String name;
  @Expose
  private boolean isDetailLink;
  @Expose
  private boolean isVisible;
  @Expose
  private String picklist = null;
  @Expose
  private boolean isEditable;
  @Expose
  private boolean isParam;
  @Expose
  private boolean isSearchable;
  @Expose
  private boolean isShow;
  @Expose
  private String formatter;
  @Expose
  private int priority;
  @Expose
  private String relationTable = null;
  @Expose
  private String fieldGroup = "基本信息";
  @Expose
  private String alias = null;
  @Expose
  private boolean isBaseInfo = false;
  @Expose
  private String default_value = null;
  @Expose
  private String default_value_type = null;
  @Expose
  private String childNode;
  @Expose
  private String parentNode;
  @Expose
  private String pattern;
  @Expose
  private int tooltip;
  @Expose
  private boolean isExistsDefaultValue;
  @Expose
  private String fieldType;

  public String getPattern() {
	return pattern;
}

public void setPattern(String pattern) {
	this.pattern = pattern;
}

public boolean isRequired()
  {
    return isRequired;
  }

  public void setRequired(boolean isRequired)
  {
    this.isRequired = isRequired;
  }

  public boolean isPrimaryKey()
  {
    return isPrimaryKey;
  }

  public void setPrimaryKey(boolean isPrimaryKey)
  {
    this.isPrimaryKey = isPrimaryKey;
  }

  public String getId()
  {
    return id;
  }

  public void setId(String id)
  {
    this.id = id;
  }

  public String getDisplay()
  {
    return display;
  }

  public void setDisplay(String display)
  {
    this.display = display;
  }

  public String getDataType()
  {
    return dataType;
  }

  public void setDataType(String dataType)
  {
    this.dataType = dataType;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }
  
  public boolean isShow() {
	return isShow;
  }

  public void setShow(boolean isShow) {
	this.isShow = isShow;
  }

  @Override
  public String toString()
  {
    GsonBuilder gb = new GsonBuilder();

    return gb.excludeFieldsWithoutExposeAnnotation().create().toJson(this);
  }

  public boolean isDetailLink()
  {
    return isDetailLink;
  }

  public void setDetailLink(boolean isDetailLink)
  {
    this.isDetailLink = isDetailLink;
  }

  public boolean isVisible()
  {
    return isVisible;
  }

  public void setVisible(boolean isVisible)
  {
    this.isVisible = isVisible;
  }

  public String getPicklist()
  {
    return picklist;
  }

  public void setPicklist(String picklist)
  {
    this.picklist = picklist;
  }

  public boolean isEditable()
  {
    return isEditable;
  }

  public void setEditable(boolean isEditable)
  {
    this.isEditable = isEditable;
  }

  public String getFormatter()
  {
    return formatter;
  }

  public void setFormatter(String formatter)
  {
    this.formatter = formatter;
  }

  public int getPriority()
  {
    return priority;
  }

  public void setPriority(int priority)
  {
    this.priority = priority;
  }

  public String getRelationTable()
  {
    return relationTable;
  }

  public void setRelationTable(String relationTable)
  {
    this.relationTable = relationTable;
  }

  public String getAlias()
  {
    return alias;
  }

  public void setAlias(String alias)
  {
    this.alias = alias;
  }

  public String getFieldGroup()
  {
    return fieldGroup;
  }

  public void setFieldGroup(String fieldGroup)
  {
    this.fieldGroup = fieldGroup;
  }

  public boolean isBaseInfo()
  {
    return isBaseInfo;
  }

  public void setBaseInfo(boolean isBaseInfo)
  {
    this.isBaseInfo = isBaseInfo;
  }

  public boolean isSearchable()
  {
    return isSearchable;
  }

  public void setSearchable(boolean isSearchable)
  {
    this.isSearchable = isSearchable;
  }

  public String getDefault_value()
  {
    return default_value;
  }

  public void setDefault_value(String default_value)
  {
    this.default_value = default_value;
  }

  public String getDefault_value_type()
  {
    return default_value_type;
  }

  public void setDefault_value_type(String default_value_type)
  {
    this.default_value_type = default_value_type;
  }

public boolean isParam()
  {
    return isParam;
  }

  public void setParam(boolean isParam)
  {
    this.isParam = isParam;
  }

  public String getChildNode()
  {
    return childNode;
  }

  public void setChildNode(String childNode)
  {
    this.childNode = childNode;
  }

  public String getParentNode()
  {
    return parentNode;
  }

  public void setParentNode(String parentNode)
  {
    this.parentNode = parentNode;
  }

  public String getFieldType()
  {
	  
    return fieldType;
  }

  public void setFieldType(String fieldType)
  {
    this.fieldType = fieldType;
  }

  public int getTooltip()
  {
    return tooltip;
  }

  public void setTooltip(int tooltip)
  {
    this.tooltip = tooltip;
  }

  public boolean isExistsDefaultValue()
  {
    return isExistsDefaultValue;
  }

  public void setExistsDefaultValue(boolean isExistsDefaultValue)
  {
    this.isExistsDefaultValue = isExistsDefaultValue;
  }

}
