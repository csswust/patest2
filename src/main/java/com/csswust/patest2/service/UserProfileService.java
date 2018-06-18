package com.csswust.patest2.service;

import com.csswust.patest2.common.APIResult;
import com.csswust.patest2.service.result.UserProfileLoadRe;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by 972536780 on 2018/3/16.
 */
public interface UserProfileService {
    APIResult insertByExcel(MultipartFile file, boolean isIgnoreError);
}
