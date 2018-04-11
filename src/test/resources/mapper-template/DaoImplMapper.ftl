package ${basePackage}.dao.impl;

import ${basePackage}.dao.common.BaseQuery;
import ${basePackage}.dao.common.CommonMapper;
import ${basePackage}.dao.${typeName}Dao;
import ${model.type};
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class ${typeName}DaoImpl extends CommonMapper<${typeName}, BaseQuery> implements ${typeName}Dao {
    @Override
    public String getPackage() {
        return "${model.namespace}.";
    }

    @Override
    public void insertInit(${typeName} record, Date date) {
        record.set${dIdProperty}(null);
        record.setCreateTime(date);
        record.setModifyTime(date);
    }

    @Override
    public void updateInit(${typeName} record, Date date) {
        record.setModifyTime(date);
    }
}