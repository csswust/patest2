package com.csswust.patest2.dao.impl;

import com.csswust.patest2.common.dao.BaseQuery;
import com.csswust.patest2.common.dao.CommonMapper;
import com.csswust.patest2.dao.AcademyInfoDao;
import com.csswust.patest2.entity.AcademyInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class AcademyInfoDaoImpl extends CommonMapper<AcademyInfo, BaseQuery> implements AcademyInfoDao {
    @Override
    public String getPackage() {
        return "com.csswust.patest2.dao.AcademyInfoDao.";
    }

    @Override
    public void insertInit(AcademyInfo record) {
        record.setAcaId(null);
        record.setCreateTime(new Date());
        record.setModifyTime(new Date());
    }

    @Override
    public void updatInit(AcademyInfo record) {
        record.setModifyTime(new Date());
    }

    @Override
    public AcademyInfo selectByAcademyName(String academyName) {
        if (StringUtils.isBlank(academyName)) {
            return null;
        }
        AcademyInfo record = new AcademyInfo();
        record.setAcademyName(academyName);
        List<AcademyInfo> result = this.selectByCondition(record, new BaseQuery(1, 1));
        return (result != null && result.size() != 0) ? result.get(0) : null;
    }
}
