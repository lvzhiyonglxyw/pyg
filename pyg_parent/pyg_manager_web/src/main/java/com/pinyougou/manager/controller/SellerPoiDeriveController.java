package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.utils.DownloadUtil;
import com.pyg.pyg_sellergoods_interface.SellerService;
import com.pyg_pojo.entity.Result;
import com.pyg_pojo.pojo.TbSeller;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.apache.struts2.ServletActionContext.getServletContext;

@RestController
@RequestMapping("/sellerPoiDeriveController")
public class SellerPoiDeriveController {

    @Reference
    private SellerService sellerService;

    @RequestMapping("/sellerPoiDerive")
    public void sellerPoiDerive(HttpServletResponse response, HttpServletRequest request, String name, String nickName) {
        try {
            response.setContentType("text/html;charset=utf-8");
            request.setCharacterEncoding("utf-8");
            response.setCharacterEncoding("utf-8");
            name=new String(name.getBytes("iso-8859-1"),"utf-8");
            nickName=new String(nickName.getBytes("iso-8859-1"),"utf-8");
            //创建excel工作簿
            Workbook wb = new HSSFWorkbook();
            // 解释：该方法相当于新建了一个excel文件，HSSFWorkbook ：excel的文档对象。
            //创建第一个sheet（页），命名为 page1
            Sheet sheet = wb.createSheet("page1");
            //  解释：该方法相当于在excel文件里新建了一个工作页，HSSFSheet   ：excel文件的表单页
            Row row1 = sheet.createRow((short) 0);
            // 在row行上创建一个方格
            Cell cell1 = row1.createCell(0);
            //在放格上面显示
            cell1.setCellValue("用户ID");
            //设置方格的显示
            row1.createCell(1).setCellValue("公司名");
            row1.createCell(2).setCellValue("店铺名称");
            row1.createCell(3).setCellValue("密码");
            row1.createCell(4).setCellValue("EMAIL");
            row1.createCell(5).setCellValue("公司手机");
            row1.createCell(6).setCellValue("公司电话");
            row1.createCell(7).setCellValue("状态");
            row1.createCell(8).setCellValue("详细地址");
            row1.createCell(9).setCellValue("联系人姓名");
            row1.createCell(10).setCellValue("联系人QQ");
            row1.createCell(11).setCellValue("联系人电话");
            row1.createCell(12).setCellValue("联系人EMAIL");
            row1.createCell(13).setCellValue("营业执照号");
            row1.createCell(14).setCellValue("税务登记证号");
            row1.createCell(15).setCellValue("组织机构代码");
            // row1.createCell(16).setCellValue("公司地址");
            row1.createCell(16).setCellValue("公司LOGO图");
            row1.createCell(17).setCellValue("简介");
            //row1.createCell(18).setCellValue("创建日期");
            row1.createCell(18).setCellValue("法定代表人");
            row1.createCell(19).setCellValue("法定代表人身份证");
            row1.createCell(20).setCellValue("开户行账号名称");
            row1.createCell(21).setCellValue("开户行");

            //  05
            //设置方格的显示
            List<TbSeller> sellerList = sellerService.findAll(name, nickName);
            for (int i = 0; i < sellerList.size(); i++) {
                // 在页sheet上，创建一行
                Row row = sheet.createRow((short) 1 + i);
                // 在row行上创建一个方格
                Cell cell = row.createCell(0);
                //在放格上面显示
                //cell.setCellValue(sellerList.get(i).getSellerId());
                //  05
                //设置方格的显示
                cell.setCellValue(sellerList.get(i).getSellerId());//用户ID
                row.createCell(1).setCellValue(sellerList.get(i).getName());//公司名
                row.createCell(2).setCellValue(sellerList.get(i).getNickName());//店铺名称
                row.createCell(3).setCellValue(sellerList.get(i).getPassword());//密码
                row.createCell(4).setCellValue(sellerList.get(i).getEmail());//EMAIL
                row.createCell(5).setCellValue(sellerList.get(i).getMobile());//公司手机
                row.createCell(6).setCellValue(sellerList.get(i).getTelephone());//公司电话
                if ("0".equals(sellerList.get(i).getStatus())) {
                    row.createCell(7).setCellValue("未审核");//状态
                } else if ("1".equals(sellerList.get(i).getStatus())) {
                    row.createCell(7).setCellValue("审核通过");//状态
                } else if("2".equals(sellerList.get(i).getStatus())){
                    row.createCell(7).setCellValue("未通过审核");//状态
                }else if ("3".equals(sellerList.get(i).getStatus())){
                    row.createCell(7).setCellValue("商家关闭");//状态
                }
                row.createCell(8).setCellValue(sellerList.get(i).getAddressDetail());//详细地址
                row.createCell(9).setCellValue(sellerList.get(i).getLinkmanName());//联系人姓名
                row.createCell(10).setCellValue(sellerList.get(i).getLinkmanQq());//联系人QQ
                row.createCell(11).setCellValue(sellerList.get(i).getLinkmanMobile());//联系人电话
                row.createCell(12).setCellValue(sellerList.get(i).getLinkmanEmail());//联系人EMAIL
                row.createCell(13).setCellValue(sellerList.get(i).getLicenseNumber());//营业执照号
                row.createCell(14).setCellValue(sellerList.get(i).getTaxNumber());//税务登记证号
                row.createCell(15).setCellValue(sellerList.get(i).getOrgNumber());//组织机构代码
               /* if (sellerList.get(i).getAddress() != null) {
                    row.createCell(16).setCellValue(sellerList.get(i).getAddress());//公司地址
                } else {
                    row.createCell(16).setCellValue("");//公司地址
                }*/
                row.createCell(16).setCellValue(sellerList.get(i).getLogoPic());//公司LOGO图
                row.createCell(17).setCellValue(sellerList.get(i).getBrief());//简介

               /* if (sellerList.get(i).getCreateTime() != null) {
                    Date date = sellerList.get(i).getCreateTime();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String createTime = format.format(date);
                    row.createCell(18).setCellValue(createTime);//创建日期
                } else {
                    row.createCell(18).setCellValue("");//创建日期
                }*/
                row.createCell(18).setCellValue(sellerList.get(i).getLegalPerson());//法定代表人
                row.createCell(19).setCellValue(sellerList.get(i).getLegalPersonCardId());//法定代表人身份证
                row.createCell(20).setCellValue(sellerList.get(i).getBankUser());//开户行账号名称
                row.createCell(21).setCellValue(sellerList.get(i).getBankName());//开户行
            }
            //下载文件
            //创建一个文件 命名为workbook.xls
            //得到项目的绝对路径，也就是tomcat所在磁盘路径
            String realPath = request.getSession().getServletContext().getRealPath("/upload");
            //得到要上传的路径文件
            File sourceFile = new File(realPath);
            if (!sourceFile.exists()) {
                //如果upload文件夹不存在，用代码创建一个。
                sourceFile.mkdirs();
            }
            FileOutputStream fileOut = new FileOutputStream(realPath + "\\seller.xls");// 把上面创建的工作簿输出到文件中
            wb.write(fileOut);//关闭输出流
            fileOut.close();

//////////////////////////////////文件下载////////////////////////////////////////////////

            //1.1获取下载资源文件的名称
            String filename = "seller.xls";

            //1.2 解决中文乱码问题
            filename = new String(filename.getBytes("iso8859-1"), "UTF-8");

            // 2.获取下载文件的路径
            String realPath1 = request.getSession().getServletContext().getRealPath("upload/" + filename);
            //3.设置两个头(ContentType和Content-Disposition)
            //3.1 先得获取ContentType的内容(MIME类型)
            String mimeType = request.getSession().getServletContext().getMimeType(filename);
            //System.out.println(mimeType);
            //3.2设置contentType
            response.setHeader("ContentType", mimeType);

            //3.3解决下载框中文(根据不同的浏览器设置不同的解决方式)
            String user_Agent = request.getHeader("User-Agent");
            if (user_Agent.contains("Firefox")) { //解决火狐的
                filename = base64EncodeFileName(filename);
            } else {
                filename = URLEncoder.encode(filename, "UTF-8");
            }

            //3.4 设置Content-Disposition内容  告诉浏览器请使用下载框的方式打开该文件
            response.addHeader("Content-Disposition", "attachment;filename=" + filename);

            // 4.获得输入一个流
            FileInputStream is = new FileInputStream(realPath1);
            // 5.获得一个输出流
            ServletOutputStream os = response.getOutputStream();
            // 6.输入流输出流对拷
            int len = 0;
            byte[] b = new byte[1024];
            while ((len = is.read(b)) != -1) {
                os.write(b, 0, len);
            }
            os.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    /**
     * 解决火狐浏览器乱码问题
     *
     * @param fileName
     * @return
     */
    public static String base64EncodeFileName(String fileName) {
        BASE64Encoder base64Encoder = new BASE64Encoder();
        try {
            return "=?UTF-8?B?"
                    + new String(base64Encoder.encode(fileName
                    .getBytes("UTF-8"))) + "?=";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}