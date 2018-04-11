package ${basePackage}.controller;

import ${basePackage}.controller.common.BaseAction;
import ${basePackage}.dao.${typeName}Dao;
import ${basePackage}.dao.common.BaseQuery;
import ${basePackage}.entity.${typeName};
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 972536780 on 2018/4/1.
 */
@RestController
@RequestMapping("/${lTypeName}")
public class ${typeName}Action extends BaseAction {
    @Autowired
    private ${typeName}Dao ${lTypeName}Dao;

    @RequestMapping(value = "/selectByCondition", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> selectByCondition(
            ${typeName} ${lTypeName},
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer rows) {
        if (${lTypeName} == null) return null;
        Map<String, Object> res = new HashMap<>();
        List<${typeName}> ${lTypeName}List = ${lTypeName}Dao.selectByCondition(${lTypeName},
                new BaseQuery(page, rows));
        Integer total = ${lTypeName}Dao.selectByConditionGetCount(${lTypeName}, new BaseQuery());
        res.put("total", total);
        res.put("list", ${lTypeName}List);
        return res;
    }

    @RequestMapping(value = "/selectById", method = {RequestMethod.GET, RequestMethod.POST})
    public ${typeName} selectById(@RequestParam Integer id) {
        return ${lTypeName}Dao.selectByPrimaryKey(id);
    }

    @RequestMapping(value = "/insertOne", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> insertOne(${typeName} ${lTypeName}) {
        Map<String, Object> res = new HashMap<>();
        int result = ${lTypeName}Dao.insertSelective(${lTypeName});
        res.put("status", result);
        return res;
    }

    @RequestMapping(value = "/updateById", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> updateById(${typeName} ${lTypeName}) {
        Map<String, Object> res = new HashMap<>();
        int result = ${lTypeName}Dao.updateByPrimaryKeySelective(${lTypeName});
        res.put("status", result);
        return res;
    }

    @RequestMapping(value = "/deleteByIds", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> deleteByIds(@RequestParam String ids) {
        if (ids == null || StringUtils.isBlank(ids)) return null;
        Map<String, Object> res = new HashMap<>();
        int result = ${lTypeName}Dao.deleteByIds(ids);
        res.put("status", result);
        return res;
    }

    @RequestMapping(value = "/insertBatch", method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> insertBatch(@RequestParam ${typeName}[] ${lTypeName}List) {
        if (${lTypeName}List == null) return null;
        Map<String, Object> res = new HashMap<>();
        int status = ${lTypeName}Dao.insertBatch(Arrays.asList(${lTypeName}List));
        if (status == ${lTypeName}List.length) res.put("status", status);
        else res.put("status", 0);
        return res;
    }
}
