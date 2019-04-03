package com.pinyougou.manager.controller;

import com.pinyougou.utils.FastDFSClient;
import com.pyg_pojo.entity.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/uploadController")
public class UploadController {

    //获取服务器地址
    private String server_url = "http://192.168.25.133/";

    @RequestMapping("/uplad")
    public Result uplad(MultipartFile file1) {
        //1、取文件的扩展名
        String filename = file1.getOriginalFilename();//文件名称
        //截取最后一个"."之后的内容（后缀名）
        String extName = filename.substring(filename.lastIndexOf("."));
        try {
            //2、创建FastDFS的客户端
            FastDFSClient client = new FastDFSClient("classpath:config/fdfs_client.conf");
            //3、执行上传文件
            String file_id = client.uploadFile(file1.getBytes(), extName);
            //4、拼接返回的url和ip地址，拼装成完整的url
            String url = server_url + file_id;
            System.out.println(url);
            return new Result(true, url);
        } catch (Exception e) {
            return new Result(false, "上传失败");
        }
    }
}
