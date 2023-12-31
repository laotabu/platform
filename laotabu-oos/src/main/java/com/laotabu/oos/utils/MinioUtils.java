package com.laotabu.oos.utils;

import com.laotabu.oos.domain.OOSFileInfo;
import io.minio.BucketExistsArgs;
import io.minio.CopyObjectArgs;
import io.minio.CopySource;
import io.minio.GetObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.ListObjectsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveBucketArgs;
import io.minio.RemoveObjectArgs;
import io.minio.Result;
import io.minio.StatObjectArgs;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class MinioUtils {
    @Autowired
    private MinioClient minioClient;

    /**
     * 创建一个桶
     */
    public void createBucket(String bucket) throws Exception {
        boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
        if (!found) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
        }
    }

    /**
     * 上传一个文件
     */
    public void uploadFile(InputStream stream, String bucket, String objectName) throws Exception {
        minioClient.putObject(PutObjectArgs.builder().bucket(bucket).object(objectName)
                .stream(stream, -1, 10485760).build());
    }

    /**
     * 列出所有的桶
     */
    public List<String> listBuckets() throws Exception {
        List<Bucket> list = minioClient.listBuckets();
        List<String> names = new ArrayList<>();
        list.forEach(b -> {
            names.add(b.name());
        });
        return names;
    }

    /**
     * 列出一个桶中的所有文件和目录
     */
    public List<OOSFileInfo> listFiles(String bucket) throws Exception {
        Iterable<Result<Item>> results = minioClient.listObjects(
                ListObjectsArgs.builder().bucket(bucket).recursive(true).build());

        List<OOSFileInfo> infos = new ArrayList<>();
        results.forEach(r->{
            OOSFileInfo info = new OOSFileInfo();
            try {
                Item item = r.get();
                info.setFilename(item.objectName());
                info.setDirectory(item.isDir());
                infos.add(info);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return infos;
    }

    /**
     * 下载一个文件
     */
    public InputStream download(String bucket, String objectName) throws Exception {
        InputStream stream = minioClient.getObject(
                GetObjectArgs.builder().bucket(bucket).object(objectName).build());
        return stream;
    }

    /**
     * 删除一个桶
     */
    public void deleteBucket(String bucket) throws Exception {
        minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucket).build());
    }

    /**
     * 删除一个对象
     */
    public void deleteObject(String bucket, String objectName) throws Exception {
        minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucket).object(objectName).build());
    }


    /**
     * 复制文件
     *
     * @Param: [sourceBucket, sourceObject, targetBucket, targetObject]
     * @return: void
     * @Author: hjh
     * @Date: 2023/11/21
     */
    public void copyObject(String sourceBucket, String sourceObject, String targetBucket, String targetObject) throws Exception {
        this.createBucket(targetBucket);
        minioClient.copyObject(CopyObjectArgs.builder().bucket(targetBucket).object(targetObject)
                .source(CopySource.builder().bucket(sourceBucket).object(sourceObject).build()).build());
    }

    /**
     * 获取文件信息
     *
     * @Param: [bucket, objectName]
     * @return: java.lang.String
     * @Author: hjh
     * @Date: 2023/11/21
     */
    public String getObjectInfo(String bucket, String objectName) throws Exception {

        return minioClient.statObject(StatObjectArgs.builder().bucket(bucket).object(objectName).build()).toString();

    }

    /**
     * 生成一个给HTTP GET请求用的presigned URL。浏览器/移动端的客户端可以用这个URL进行下载，即使其所在的存储桶是私有的。
     *
     * @Param: [bucketName, objectName, expires]
     * @return: java.lang.String
     * @Author: hjh
     * @Date: 2023/11/21
     */
    public String getPresignedObjectUrl(String bucketName, String objectName, Integer expires, TimeUnit timeUnit) throws Exception {
        GetPresignedObjectUrlArgs build = GetPresignedObjectUrlArgs
                .builder().bucket(bucketName).object(objectName).expiry(expires,timeUnit).method(Method.GET).build();
        return minioClient.getPresignedObjectUrl(build);
    }

    /**
     * 获取minio中所有的文件
     *
     * @Param: []
     * @return: java.util.List<boot.spring.domain.Fileinfo>
     * @Author: hjh
     * @Date: 2023/11/21
     */
    public List<OOSFileInfo> listAllFile() throws Exception {
        List<String> list = this.listBuckets();
        List<OOSFileInfo> OOSFileInfos = new ArrayList<>();
        for (String bucketName : list) {
            OOSFileInfos.addAll(this.listFiles(bucketName));
        }
        
        return OOSFileInfos;
    }

}