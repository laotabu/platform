package com.laotabu.oos.controller;

import com.laotabu.common.annotation.Log;
import com.laotabu.common.core.domain.AjaxResult;
import com.laotabu.common.enums.BusinessType;
import com.laotabu.common.utils.StringUtils;
import com.laotabu.oos.config.MinioConfig;
import com.laotabu.oos.service.OosAssetsService;
import com.laotabu.oos.utils.MinioUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author: lsd
 * @Date: 2023/11/17 - 16:44
 * @Desc:
 */
@RestController
@RequestMapping("/oos/workflow")
public class OosWorkflowController {
    @Autowired
    OosAssetsService oosAssetsService;

    @Autowired
    private MinioUtils minioUtils;

    @Autowired
    private MinioConfig minioConfig;

    @Log(title = "固资申请附件上传", businessType = BusinessType.IMPORT)
    @PostMapping(value = "/upAssetsFile")
    @PreAuthorize("@ss.hasPermi('workflow:assets:add')")
    public AjaxResult upAssetsFile(@RequestParam MultipartFile[] files) throws IOException {
//        processDefinitionService.uploadStreamAndDeployment(file);
        //TODO 对象存储服务
        InputStream inputStream = null;
        List<String> fileUrlList = new ArrayList<>();
        try {
            for (MultipartFile file : files) {
                //文件名
                String fileName = file.getOriginalFilename();
                inputStream = file.getInputStream();
                minioUtils.uploadFile(inputStream,minioConfig.getBucketName(), fileName);
                //生成一个url给前端
                String fileUrl = minioUtils.getPresignedObjectUrl(minioConfig.getBucketName(), fileName, 1, TimeUnit.DAYS);
                fileUrlList.add(fileUrl);
                if (inputStream != null){
                    inputStream.close();
                }
            }
            return AjaxResult.success("上传成功",fileUrlList);
        } catch (Exception e) {
            return AjaxResult.error("上传失败");
        } finally {
            if (inputStream != null){
                inputStream.close();
            }
        }
    }


}
