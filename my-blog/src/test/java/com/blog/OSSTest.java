package com.blog;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@SpringBootTest
//@ConfigurationProperties(prefix = "oss")
public class OSSTest {

    private String accessKey;
    private String secretKey;
    private String bucket;

    @Test
    public void test(){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.autoRegion());
        //...其他参数参考类注释

        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
//        String accessKey = "MRdXRobG0tlKAJWPZSERRZ6R2Rye1JxT5wX9e1r4";
//        String secretKey = "miffUm-5zurjo-hugvac";
//        String bucket = "my-learning";

        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = null;

        try {
//            byte[] uploadBytes = "hello qiniu cloud".getBytes("utf-8");
//            ByteArrayInputStream byteInputStream=new ByteArrayInputStream(uploadBytes);

            File file = new File("C:\\Users\\42563\\Pictures\\Attention.jpg");
            FileInputStream fileInputStream = new FileInputStream(file);

            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);

            try {
                Response response = uploadManager.put(fileInputStream,key,upToken,null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (Exception ex) {
            //ignore
        }

    }

    @Test
    public void test1(){
        String originalFilename = "5.考勤报工-普通员工使用手册v1.0.docx";
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filePath = "C:\\Users\\42563\\Desktop\\新员工入职学习并掌握的制度汇总2022.6.22\\新员工入职学习并掌握的制度汇总\\5.考勤报工-普通员工使用手册v1.0.docx";

        try {
            String dirName = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String fileSaveName = LocalTime.now().format(DateTimeFormatter.ofPattern("HHmmss"));
            if (!new File("D:\\" + dirName).exists()){
                new File("D:\\" + dirName).mkdir();
            }
            OutputStream outputStream = new FileOutputStream(
                    "D:\\" + dirName
                            +"\\" + fileSaveName
                            + suffix);
            FileInputStream fileInputStream = new FileInputStream(new File(filePath));
            int available = fileInputStream.available();
            byte[] bytes = new byte[available];
            if (fileInputStream.read(bytes) != -1){
                outputStream.write(bytes);
            }

            outputStream.flush();
            outputStream.close();
            fileInputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

}
