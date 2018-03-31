package com.csswust.patest2.dao.impl;

import com.csswust.patest2.dao.SiteInfoDao;
import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.dao.common.CommonMapper;
import com.csswust.patest2.entity.SiteInfo;
import com.csswust.patest2.entity.UserInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class SiteInfoDaoImpl extends CommonMapper<SiteInfo, BaseQuery> implements SiteInfoDao {
    @Override
    public String getPackage() {
        return "com.csswust.patest2.dao.SiteInfoDao.";
    }

    @Override
    public void insertInit(SiteInfo record) {
        record.setSiteId(null);
        record.setCreateTime(new Date());
        record.setModifyTime(new Date());
    }

    @Override
    public void updatInit(SiteInfo record) {
        record.setModifyTime(new Date());
    }

    @Override
    public SiteInfo selectByName(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        SiteInfo record = new SiteInfo();
        record.setName(name);
        List<SiteInfo> result = this.selectByCondition(record, new BaseQuery(1, 1));
        return (result != null && result.size() != 0) ? result.get(0) : null;
    }
}
