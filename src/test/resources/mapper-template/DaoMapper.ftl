package ${basePackage}.dao;

import ${basePackage}.dao.common.BaseQuery;
import ${model.type};

import java.util.List;

public interface ${typeName}Dao {
    int deleteByPrimaryKey(Integer id);

    int insert(${typeName} record);

    int insertSelective(${typeName} record);

    ${typeName} selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(${typeName} record);

    int updateByPrimaryKeyWithBLOBs(${typeName} record);

    int updateByPrimaryKey(${typeName} record);

    int deleteByIds(String ids);

    int deleteByIdsList(List<Integer> idsList);

    List<${typeName}> selectByCondition(${typeName} record, BaseQuery query);

    int selectByConditionGetCount(${typeName} record, BaseQuery query);

    List<${typeName}> selectByIds(String ids);

    List<${typeName}> selectByIdsList(List<Integer> idsList);

    int insertBatch(List<${typeName}> recordList);
}