package com.hc.utils;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
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
    public static void exportExcel(List<?> list, String title, String sheetName, Class<?> pojoClass,String fileName,boolean isCreateHeader, HttpServletResponse response){
        ExportParams exportParams = new ExportParams(title, sheetName);
        exportParams.setCreateHeadRows(isCreateHeader);
        defaultExport(list, pojoClass, fileName, response, exportParams);

    }
    public static void exportExcel(List<?> list, String title, String sheetName, Class<?> pojoClass,String fileName, HttpServletResponse response){
        defaultExport(list, pojoClass, fileName, response, new ExportParams(title, sheetName));
    }

    /**
     * 导出多sheet   excle表格方法重写
     */
    public static void exportExcleSheets(List<List<?>> list,List<String> Sheettitle,List<String> sheetNameList,Class<?> pojoClass,String fileName,HttpServletResponse response){
        List<Map<String,Object>> mapList = new ArrayList<Map<String,Object>>();
        for (int i = 0;i<list.size();i++) {
            ExportParams exportParams = new ExportParams(Sheettitle.get(i), sheetNameList.get(i));
            Map<String, Object> titleMap = new HashMap<String, Object>();
            titleMap.put("title", exportParams);
            titleMap.put("data", list.get(i));
            titleMap.put("entity", pojoClass);
            mapList.add(titleMap);
        }
        ExcelType type  = ExcelType.HSSF;
        Workbook workbook = ExcelExportUtil.exportExcel(mapList,type);
        if (workbook != null ) {
            downLoadExcel(fileName,response,workbook);
        }
    }
    /**
     * 导出多sheet  不同sheet模型表格方法重写
     */
    public static void exportExcleUnSheets(List<List<?>> list,List<String> Sheettitle,List<String> sheetNameList,List<Class<?>> pojoClass,String fileName,HttpServletResponse response){
        List<Map<String,Object>> mapList = new ArrayList<Map<String,Object>>();
        for (int i = 0;i<list.size();i++) {
            ExportParams exportParams = new ExportParams(Sheettitle.get(i), sheetNameList.get(i));
            Map<String, Object> titleMap = new HashMap<String, Object>();
            titleMap.put("title", exportParams);
            titleMap.put("data", list.get(i));
            titleMap.put("entity", pojoClass.get(i));
            mapList.add(titleMap);
        }
        ExcelType type  = ExcelType.HSSF;
        Workbook workbook = ExcelExportUtil.exportExcel(mapList,type);
        if (workbook != null ) {
            downLoadExcel(fileName,response,workbook);
        }
    }

    /**
     *
     * @param list
     * @param fileName
     * @param response
     */

    public static void exportExcel(List<Map<String, Object>> list, String fileName, HttpServletResponse response){
        defaultExport(list, fileName, response);
    }

    private static void defaultExport(List<?> list, Class<?> pojoClass, String fileName, HttpServletResponse response, ExportParams exportParams) {
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams,pojoClass,list);
        if (workbook != null);
        downLoadExcel(fileName, response, workbook);
    }

    private static void downLoadExcel(String fileName, HttpServletResponse response, Workbook workbook) {
        try {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));

            workbook.write(response.getOutputStream());
        } catch (IOException e) {

        }
    }
    private static void defaultExport(List<Map<String, Object>> list, String fileName, HttpServletResponse response) {
        Workbook workbook = ExcelExportUtil.exportExcel(list, ExcelType.HSSF);
        if (workbook != null);
        downLoadExcel(fileName, response, workbook);
    }

    public static <T> List<T> importExcel(String filePath,Integer titleRows,Integer headerRows, Class<T> pojoClass){
        if (StringUtils.isBlank(filePath)){
            return null;
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        List<T> list = null;
        try {
            list = ExcelImportUtil.importExcel(new File(filePath), pojoClass, params);
        }catch (NoSuchElementException e){

        } catch (Exception e) {
            e.printStackTrace();

        }
        return list;
    }
    public static <T> List<T> importExcel(MultipartFile file, Integer titleRows, Integer headerRows, Class<T> pojoClass){
        if (file == null){
            return null;
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        List<T> list = null;
        try {
            list = ExcelImportUtil.importExcel(file.getInputStream(), pojoClass, params);
        }catch (NoSuchElementException e){

        } catch (Exception e) {

        }
        return list;
    }

}
