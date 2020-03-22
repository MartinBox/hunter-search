package com.hunter.persistence.mybatis.config.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name="mapper")
public class Mapper {
    private String namespace;
    private List<Select> select;
    private List<Insert> insert;
    private List<Update> update;
    private List<Delete> delete;
    @XmlElement(name = "select")
    public List<Select> getSelect() {
        return select;
    }

    public void setSelect(List<Select> select) {
        this.select = select;
    }
    @XmlElement(name = "insert")
    public List<Insert> getInsert() {
        return insert;
    }

    public void setInsert(List<Insert> insert) {
        this.insert = insert;
    }
    @XmlElement(name = "update")
    public List<Update> getUpdate() {
        return update;
    }

    public void setUpdate(List<Update> update) {
        this.update = update;
    }
    @XmlElement(name = "delete")
    public List<Delete> getDelete() {
        return delete;
    }

    public void setDelete(List<Delete> delete) {
        this.delete = delete;
    }

    @XmlAttribute(name = "namespace")
    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
}
