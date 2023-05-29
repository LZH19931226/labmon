package com.hc.my.common.core.util;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by 16956 on 2018-06-14.
 */
public class FileUtil {

    public static void downLoadExcel(String fileName, HttpServletResponse response, Workbook workbook) {
        try {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));

            workbook.write(response.getOutputStream());
        } catch (IOException e) {

        }
    }
    public static void exportExcel(String fileName, List<ExcelExportEntity> beanList, List<Map<String,Object>> mapList,HttpServletResponse response){
        Workbook workbook =  ExcelExportUtil.exportExcel(new ExportParams(fileName,"sheet1"),beanList,mapList);
        downLoadExcel(fileName+".xls",response,workbook);
    }
}
