package com.liandao.onlineteaching.utils;

import com.alibaba.fastjson.JSONObject;
import com.liandao.onlineteaching.config.CustomException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Data
@Component
public class QiniuyunUtil {
    @Value("${qiniu.accessKey}")
    private String accessKey;

    @Value("${qiniu.secretKey}")
    private String secretKey;

    @Value("${qiniu.bucket}")
    private String bucket;

    @Value("${qiniu.path}")
    private String path;

    // 创建配置对象
    private Configuration configuration = new Configuration(Zone.huadong());

    // 创建上传对象
    private UploadManager uploadManager = new UploadManager(this.configuration);

    // 获取上传凭证
    private String createUploadToken() {
        Auth auth = Auth.create(this.accessKey, this.secretKey);
        return auth.uploadToken(this.bucket);
    }

    public String getUploadToken() {
        return this.createUploadToken();
    }

    // 上传单张图片
    public String uploadImage(MultipartFile file) {
        if (file == null) throw new CustomException("上传文件不能为空");

        String[] fileExtNames = {"jpg", "jpeg", "png"};
        long maxFileSize = 1024 * 1024;

        String fileName = file.getOriginalFilename() == null ? "" : file.getOriginalFilename();
        String fileExtName = this.getFileExtName(fileName);
        boolean isImg = Arrays.stream(fileExtNames).anyMatch(i -> i.equals(fileExtName));

        if (!isImg) throw new CustomException("上传文件不是图片格式");
        if (file.getSize() > maxFileSize) throw new CustomException("上传文件不能大于1M");

        String tempFileName = UUID.randomUUID()
                .toString().replace("-", "") + "." + fileExtName;
        String retImagePath = this.uploadFile(file, tempFileName);

        if(retImagePath.isEmpty()) throw new CustomException("上传图片失败");

        return retImagePath;
    }

    private String uploadFile(MultipartFile file, String fileName) {
        String retPath = "";

        try {
            Response response = this.uploadManager.put(file.getBytes(), fileName, this.createUploadToken());

            if (response.isOK() && response.isJson())
                retPath = this.path + JSONObject.parseObject(response.bodyString()).get("key");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return retPath;
    }

    private String getFileExtName(String fileName) {
        return fileName.lastIndexOf(".") != -1
                ? fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase()
                : "";
    }
}
