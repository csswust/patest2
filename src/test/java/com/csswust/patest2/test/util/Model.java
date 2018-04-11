package com.csswust.patest2.test.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 972536780 on 2018/4/9.
 */
public class Model {
    private String namespace;
    private String tableName;
    private String type;
    private Class<?> typeClass;
    private String idColumn;
    private String idProperty;
    private String idJdbcType;
    private List<Field> fieldList = new ArrayList<>();
    private boolean isBlob = false;
    private List<Field> blobFieldList = new ArrayList<>();

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Class<?> getTypeClass() {
        return typeClass;
    }

    public void setTypeClass(Class<?> typeClass) {
        this.typeClass = typeClass;
    }

    public String getIdColumn() {
        return idColumn;
    }

    public void setIdColumn(String idColumn) {
        this.idColumn = idColumn;
    }

    public String getIdProperty() {
        return idProperty;
    }

    public void setIdProperty(String idProperty) {
        this.idProperty = idProperty;
    }

    public String getIdJdbcType() {
        return idJdbcType;
    }

    public void setIdJdbcType(String idJdbcType) {
        this.idJdbcType = idJdbcType;
    }

    public List<Field> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<Field> fieldList) {
        this.fieldList = fieldList;
    }

    public boolean isBlob() {
        return isBlob;
    }

    public void setBlob(boolean blob) {
        isBlob = blob;
    }

    public List<Field> getBlobFieldList() {
        return blobFieldList;
    }

    public void setBlobFieldList(List<Field> blobFieldList) {
        this.blobFieldList = blobFieldList;
    }
}
