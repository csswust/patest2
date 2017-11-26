package com.csswust.patest2.common.dao;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class CommonMapper<T, Q extends BaseQuery> extends SqlSessionDaoSupport implements BaseDao<T, Q> {
    private static Logger log = LoggerFactory.getLogger(CommonMapper.class);

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    public abstract String getPackage();

    public abstract void insertInit(T record);

    public abstract void updatInit(T record);

    @Override
    public int deleteByPrimaryKey(Integer id) {
        if (id == null) {
            return 0;
        }
        try {
            return getSqlSession().delete(getPackage() + "deleteByPrimaryKey", id);
        } catch (Exception e) {
            log.error("CommonMapper.deleteByPrimaryKey({}) error: {}", id, e);
            return 0;
        }
    }

    @Override
    public int insert(T record) {
        if (record == null) {
            return 0;
        }
        insertInit(record);
        try {
            return getSqlSession().insert(getPackage() + "insert", record);
        } catch (Exception e) {
            log.error("CommonMapper.insert({}) error: {}", record, e);
            return 0;
        }
    }

    @Override
    public int insertSelective(T record) {
        if (record == null) {
            return 0;
        }
        insertInit(record);
        try {
            return getSqlSession().insert(getPackage() + "insertSelective", record);
        } catch (Exception e) {
            log.error("CommonMapper.insertSelective({}) error: {}", record, e);
            return 0;
        }
    }

    @Override
    public T selectByPrimaryKey(Integer id) {
        if (id == null) {
            return null;
        }
        try {
            return getSqlSession().selectOne(getPackage() + "selectByPrimaryKey", id);
        } catch (Exception e) {
            log.error("CommonMapper.selectByPrimaryKey({}) error: {}", id, e);
            return null;
        }
    }

    @Override
    public int updateByPrimaryKeySelective(T record) {
        if (record == null) {
            return 0;
        }
        updatInit(record);
        try {
            return getSqlSession().update(getPackage() + "updateByPrimaryKeySelective", record);
        } catch (Exception e) {
            log.error("CommonMapper.updateByPrimaryKeySelective({}) error: {}", record, e);
            return 0;
        }
    }

    @Override
    public int updateByPrimaryKeyWithBLOBs(T record) {
        if (record == null) {
            return 0;
        }
        updatInit(record);
        try {
            return getSqlSession().update(getPackage() + "updateByPrimaryKeyWithBLOBs", record);
        } catch (Exception e) {
            log.error("CommonMapper.updateByPrimaryKeyWithBLOBs({}) error: {}", record, e);
            return 0;
        }
    }

    @Override
    public int updateByPrimaryKey(T record) {
        if (record == null) {
            return 0;
        }
        updatInit(record);
        try {
            return getSqlSession().update(getPackage() + "updateByPrimaryKey", record);
        } catch (Exception e) {
            log.error("CommonMapper.updateByPrimaryKey({}) error: {}", record, e);
            return 0;
        }
    }

    @Override
    public List<T> selectByCondition(T record, Q query) {
        List<T> list = new ArrayList<T>();
        try {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("record", record);
            if (query.getPage() != null && query.getRows() != null) {
                param.put("start", (query.getPage() - 1) * query.getRows());
                param.put("rows", query.getRows());
            }
            return getSqlSession().selectList(getPackage() + "selectByCondition", param);
        } catch (Exception e) {
            log.error("CommonMapper.selectByCondition({}) error: {}", record, e);
            return list;
        }
    }

    @Override
    public int selectByConditionGetCount(T record, Q query) {
        try {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("record", record);
            if (query.getPage() != null && query.getRows() != null) {
                param.put("start", (query.getPage() - 1) * query.getRows());
                param.put("rows", query.getRows());
            }
            return getSqlSession().selectOne(getPackage() + "selectByConditionGetCount", param);
        } catch (Exception e) {
            log.error("CommonMapper.selectByConditionGetCount({}) error: {}", record, e);
            return 0;
        }
    }
}
