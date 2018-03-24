package com.csswust.patest2.service;

import com.csswust.patest2.service.result.UserProfileLoadRe;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by 972536780 on 2018/3/16.
 */
public interface UserProfileService {
    UserProfileLoadRe insertByExcel(MultipartFile file, boolean isIgnoreError);
}
