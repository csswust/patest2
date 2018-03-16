package com.csswust.patest2.dao.impl;

import com.csswust.patest2.dao.common.BaseQuery;
import com.csswust.patest2.dao.common.CommonMapper;
import com.csswust.patest2.dao.MajorInfoDao;
import com.csswust.patest2.entity.MajorInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class MajorInfoDaoImpl extends CommonMapper<MajorInfo, BaseQuery> implements MajorInfoDao {
    @Override
    public String getPackage() {
        return "com.csswust.patest2.dao.MajorInfoDao.";
    }

    @Override
    public void insertInit(MajorInfo record) {
        record.setMajId(null);
        record.setCreateTime(new Date());
        record.setModifyTime(new Date());
    }

    @Override
    public void updatInit(MajorInfo record) {
        record.setModifyTime(new Date());
    }

    @Override
    public MajorInfo selectByMajorName(String majorName) {
        if (StringUtils.isBlank(majorName)) {
            return null;
        }
        MajorInfo record = new MajorInfo();
        record.setMajorName(majorName);
        List<MajorInfo> result = this.selectByCondition(record, new BaseQuery(1, 1));
        return (result != null && result.size() != 0) ? result.get(0) : null;
    }
}
