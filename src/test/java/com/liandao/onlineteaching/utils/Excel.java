package com.liandao.onlineteaching.utils;

import org.apache.poi.hpsf.DocumentSummaryInformation;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Excel {
    @Test
    public void test() {
        HSSFWorkbook sheets = new HSSFWorkbook();
        //获取文档信息，并配置
        DocumentSummaryInformation dsi = sheets.getDocumentSummaryInformation();
//        //文档类别
//        dsi.setCategory("员工信息");
//        //设置文档管理员
//        dsi.setManager("江南一点雨");
//        //设置组织机构
//        dsi.setCompany("XXX集团");
//        //获取摘要信息并配置
//        SummaryInformation si = sheets.getSummaryInformation();
//        //设置文档主题
//        si.setSubject("员工信息表");
//        //设置文档标题
//        si.setTitle("员工信息");
//        //设置文档作者
//        si.setAuthor("XXX集团");
//        //设置文档备注
//        si.setComments("备注信息暂无");

        HSSFSheet sheet = sheets.createSheet();

        HSSFRow row = sheet.createRow(0);
        HSSFCell cell1 = row.createCell(0);
        HSSFCell cell2 = row.createCell(1);
        HSSFCell cell3 = row.createCell(2);
        cell1.setCellValue("用户账号");
        cell2.setCellValue("用户名");
        cell3.setCellValue("用户角色");

        String path = "./test.xls";
        try {
            FileOutputStream fileOut = new FileOutputStream(path);
            try {
                sheets.write(fileOut);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
