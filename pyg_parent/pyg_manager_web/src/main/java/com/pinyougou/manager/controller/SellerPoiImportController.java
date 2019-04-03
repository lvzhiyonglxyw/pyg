package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pyg.pyg_sellergoods_interface.SellerService;
import com.pyg_pojo.entity.Result;
import com.pyg_pojo.pojo.TbSeller;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("all")
@RestController
@RequestMapping("/sellerPoiImportController")
public class SellerPoiImportController {

    @Reference
    private SellerService sellerService;

    /**
     * 读取出filePath中的所有数据信息
     *
     * @param filePath excel文件的绝对路径
     */
    @RequestMapping("/dataFromExcel")
    public List<Result> getDataFromExcel(HttpServletRequest request, MultipartFile uploadFile) {
        List<Result> resultList = new ArrayList<>();
        Result result = null;
        try {
            //////////////////////上传文件///////////////////////////////
            //得到项目的绝对路径，也就是tomcat所在磁盘路径
            String realPath = request.getSession().getServletContext().getRealPath("/upload");
            //得到要上传的路径文件
            File sourceFile = new File(realPath);
            if (!sourceFile.exists()) {
                //如果upload文件夹不存在，用代码创建一个。
                sourceFile.mkdirs();
            }
            //得到当前文件的名称
            String fileName = uploadFile.getOriginalFilename();
            //确保文件名唯一
            //fileName = UUID.randomUUID() + fileName;
            //上传文件
            //把uploadFile里面的文件上传到服务器sourceFile文件中，名字叫fileName
            uploadFile.transferTo(new File(sourceFile, fileName));
            //////////////////////上传文件///////////////////////////////

            //////////////////////导入文件内容///////////////////////////////
            String filePath = realPath + "\\" + fileName;
            System.out.println("上传文件路径：" + filePath);
            //判断是否为excel类型文件
            if (!filePath.endsWith(".xls") && !filePath.endsWith(".xlsx")) {
                System.out.println("文件不是excel类型");
            }
            FileInputStream fis = null;
            Workbook wookbook = null;
            try {
                //获取一个绝对地址的流
                fis = new FileInputStream(filePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                //2003版本的excel，用.xls结尾
                wookbook = new HSSFWorkbook(fis);//得到工作簿
            } catch (Exception ex) {
                //ex.printStackTrace();
                try {
                    //2007版本的excel，用.xlsx结尾
                    wookbook = new XSSFWorkbook(fis);//得到工作簿
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            //得到一个工作表
            Sheet sheet = wookbook.getSheetAt(0);
            //获得表头
            Row rowHead = sheet.getRow(0);
            //判断表头是否正确(表格中的列不得超过指定的数字)
            if (rowHead.getPhysicalNumberOfCells() != 22) {
                System.out.println("表头的数量不对!");
            }
            //获得数据的总行数
            int totalRowNum = sheet.getLastRowNum();
            //要获得属性
            String sellerId = "";//用户ID
            String name = "";//公司名
            String nickName = "";//店铺名称
            String password = "";//密码
            String email = "";//EMAIL
            String mobile = "";//公司手机
            String telephone = "";//公司电话
            String status = "";//状态
            String addressDetail = "";//详细地址
            String linkmanName = "";//联系人姓名
            String linkmanQq = "";//联系人QQ
            String linkmanMobile = "";//联系人电话
            String lLinkmanEmail = "";//联系人EMAIL
            String licenseNumber = "";//营业执照号
            String taxNumber = "";//税务登记证号
            String orgNumber = "";//组织机构代码
            // Long address = null;//地址
            String logoPic = "";//公司LOGO图
            String brief = "";//简介
            Date createTime = null;//创建时间
            String legalPerson = "";//法定代表人
            String legalPersonCardId = "";//法定代表人身份证
            String bankUser = "";//开户行账号名称
            String bankName = "";//开户行

            //获得所有数据,减去一行，第一行是标题
            System.out.println("行数：" + totalRowNum);
            // if (totalRowNum <= 1) {
            for (int i = 1; i <= totalRowNum; i++) {
                //获得第i行对象
                Row row = sheet.getRow(i);

                //获得获得第i行第0列的 String类型对象
                Cell cell = row.getCell((short) 0);
                //判断从表格中获取的列数据是否为空
                if (cell != null) {
                    sellerId = cell.getStringCellValue().toString();//用户id
                } else {
                    sellerId = "";
                }
                //获得一个数字类型的数据
                cell = row.getCell((short) 1);
                if (cell != null) {
                    name = cell.getStringCellValue();//公司名称
                } else {
                    name = "";
                }
                cell = row.getCell((short) 2);
                if (cell != null) {
                    nickName = cell.getStringCellValue();//店铺名称
                } else {
                    nickName = "";
                }
                cell = row.getCell((short) 3);
                if (cell != null) {
                    password = cell.getStringCellValue();//密码
                } else {
                    password = "";
                }

                cell = row.getCell((short) 4);
                if (cell != null) {
                    email = cell.getStringCellValue();//email
                } else {
                    email = "";
                }
                cell = row.getCell((short) 5);
                if (cell != null) {
                    //转换字符转，手机号获取到后int类型需要在取数据的时候进行转换
                    DecimalFormat df = new DecimalFormat("0");
                    mobile = df.format(cell.getNumericCellValue());
                    //mobile = cell.get();//公司手机
                } else {
                    mobile = "";
                }
                cell = row.getCell((short) 6);
                if (cell != null) {
                    DecimalFormat df = new DecimalFormat("0");
                    telephone = df.format(cell.getNumericCellValue());
                    //telephone = cell.getStringCellValue();//公司电话
                } else {
                    telephone = "";
                }

                cell = row.getCell((short) 7);
                if (cell != null) {
                    status = cell.getStringCellValue();//状态
                    if (status != null & status.length() > 0) {
                        if ("未审核".equals(status)) {
                            status = "0";
                        } else if ("审核通过".equals(status) || "通过".equals(status)) {
                            status = "1";
                        } else if ("未通过审核".equals(status) || "审核未通过".equals(status)) {
                            status = "2";
                        } else if ("关闭".equals(status) || "商家关闭".equals(status)) {
                            status = "3";
                        }
                    } else {
                        status = "3";
                    }
                    cell = row.getCell((short) 8);
                    if (cell != null) {
                        addressDetail = cell.getStringCellValue();//详细地址
                    } else {
                        addressDetail = "";
                    }
                    cell = row.getCell((short) 9);
                    if (cell != null) {
                        linkmanName = cell.getStringCellValue();//联系人姓名
                    } else {
                        linkmanName = "";
                    }
                    cell = row.getCell((short) 10);
                    if (cell != null) {
                        linkmanQq = cell.getStringCellValue();//联系人QQ
                    } else {
                        linkmanQq = "";
                    }
                }
                cell = row.getCell((short) 11);
                if (cell != null) {
                    linkmanMobile = cell.getStringCellValue();//联系人电话
                } else {
                    linkmanMobile = "";
                }
                cell = row.getCell((short) 12);
                if (cell != null) {
                    lLinkmanEmail = cell.getStringCellValue();//联系人Email
                } else {
                    lLinkmanEmail = "";
                }
                cell = row.getCell((short) 13);
                if (cell != null) {
                    licenseNumber = cell.getStringCellValue();//营业执照号
                } else {
                    licenseNumber = "";
                }
                cell = row.getCell((short) 14);
                if (cell != null) {
                    taxNumber = cell.getStringCellValue();//税务登记证号
                } else {
                    taxNumber = "";
                }
                cell = row.getCell((short) 15);
                if (cell != null) {
                    orgNumber = cell.getStringCellValue();//组织机构代码
                } else {
                    orgNumber = "";
                }
                cell = row.getCell((short) 16);
                if (cell != null) {
                    logoPic = cell.getStringCellValue();//公司LOGO图
                } else {
                    logoPic = "";
                }
                cell = row.getCell((short) 17);
                if (cell != null) {
                    brief = cell.getStringCellValue();//简介
                } else {
                    brief = "";
                }
                /*cell = row.getCell((short) 18);
                if (cell != null) {
                    createTime = cell.getDateCellValue();//创建日期
                } else {
                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String format = sdf.format(date);
                    Date parse = sdf.parse(format);
                    createTime = parse;
                }*/
                cell = row.getCell((short) 18);
                if (cell != null) {
                    legalPerson = cell.getStringCellValue();//法定代表人
                } else {
                    legalPerson = "";
                }
                cell = row.getCell((short) 19);
                if (cell != null) {
                    legalPersonCardId = cell.getStringCellValue();//法定代表人身份证
                } else {
                    legalPersonCardId = "";
                }
                cell = row.getCell((short) 20);
                if (cell != null) {
                    bankUser = cell.getStringCellValue();//开户行账号名称
                } else {
                    bankUser = "";
                }

                cell = row.getCell((short) 21);
                if (cell != null) {
                    bankName = cell.getStringCellValue();//开户行
                } else {
                    bankName = "";
                }
                System.out.println("用户ID:" + sellerId + "公司名:" + name + "店铺名称:" + nickName + "密码:" +
                        password + "EMAIL:" + email + "公司手机:" + mobile + "公司电话:" + telephone + "状态:" +
                        status + "详细地址:" + addressDetail + "联系人姓名:" + linkmanName + "联系人QQ:" + linkmanQq +
                        "联系人电话:" + linkmanMobile + "联系人EMAIL" + lLinkmanEmail + "营业执照号:" + licenseNumber +
                        "税务登记证号:" + taxNumber + "组织机构代码:" + orgNumber + "公司LOGO图:" + logoPic +
                        "简介:" + brief + "创建时间:" + createTime + "法定代表人:" + legalPerson + "法定代表人身份证:" + legalPersonCardId +
                        "开户行账号名称:" + bankUser + "开户行:" + bankName);
                //判断表格中的用户id是否存在
                TbSeller seller = sellerService.findOne(sellerId);

                if (seller != null) {
                    resultList.add(new Result(false, sellerId + "用户名已存在！"));
                    continue;
                }

                TbSeller tbSeller = new TbSeller();
                tbSeller.setSellerId(sellerId);
                tbSeller.setStatus(status);
                tbSeller.setPassword(password);
                tbSeller.setCreateTime(new Date());
                //tbSeller.setAddress(new Long(address));
                tbSeller.setAddressDetail(addressDetail);
                tbSeller.setBankName(bankName);
                tbSeller.setBankUser(bankUser);
                tbSeller.setBrief(brief);
                tbSeller.setEmail(email);
                tbSeller.setLegalPerson(legalPerson);
                tbSeller.setLegalPersonCardId(legalPersonCardId);
                tbSeller.setLicenseNumber(licenseNumber);
                tbSeller.setLinkmanEmail(lLinkmanEmail);
                tbSeller.setLinkmanMobile(linkmanMobile);
                tbSeller.setLinkmanName(linkmanName);
                tbSeller.setLinkmanQq(linkmanQq);
                tbSeller.setLogoPic(logoPic);
                tbSeller.setMobile(mobile);
                tbSeller.setName(name);
                tbSeller.setNickName(nickName);
                tbSeller.setOrgNumber(orgNumber);
                tbSeller.setTaxNumber(taxNumber);
                tbSeller.setTelephone(telephone);
                sellerService.add(tbSeller);
                resultList.add(new Result(true, sellerId + "用户名添加成功！"));
            }

            return resultList;
        } catch (Exception e) {
            e.printStackTrace();
            resultList.add(new Result(false,  "导入错误!"));
            return resultList;
        }
    }
}
