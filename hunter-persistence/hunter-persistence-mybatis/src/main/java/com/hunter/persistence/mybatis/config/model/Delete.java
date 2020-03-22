package com.hunter.persistence.mybatis.config.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

public class Delete {
    private String id;
    private String resultType;
    private String parameterType;
    private String text;

    @XmlValue
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    @XmlAttribute(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    @XmlAttribute(name = "resultType")
    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }
    @XmlAttribute(name = "parameterType")
    public String getParameterType() {
        return parameterType;
    }

    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }
}
