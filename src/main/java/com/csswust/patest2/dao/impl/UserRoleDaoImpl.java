package com.csswust.patest2.dao.impl;

import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.dao.common.CommonMapper;
import com.csswust.patest2.dao.UserRoleDao;
import com.csswust.patest2.entity.UserRole;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class UserRoleDaoImpl extends CommonMapper<UserRole, BaseQuery> implements UserRoleDao {
    @Override
    public String getPackage() {
        return "com.csswust.patest2.dao.UserRoleDao.";
    }

    @Override
    public void insertInit(UserRole record) {
        record.setUseRolId(null);
        record.setCreateTime(new Date());
        record.setModifyTime(new Date());
    }

    @Override
    public void updatInit(UserRole record) {
        record.setModifyTime(new Date());
    }
}
