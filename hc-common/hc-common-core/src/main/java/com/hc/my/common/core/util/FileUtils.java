package com.hc.my.common.core.util;

import com.hc.my.common.core.exception.IedsException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

public class FileUtils {


    /**
     * 默认格式
     */
    public static final String DEFAULT_ALLOWED_EXTENSION = "apk";

    /**
     * 默认大小 50M
     */
    public static final long DEFAULT_MAX_SIZE = 50 * 1024 * 1024;

    /**
     * 默认的文件名最大长度100
     */
    public static final int DEFAULT_FILE_NAME_LENGTH = 100;


    /**
     * 根据文件路径上传
     *
     * @param baseDir 相对应用的基目录
     * @param file 上传的文件
     * @return 文件名称
     * @throws IOException
     */
    public static final String upload(String baseDir, MultipartFile file) throws IOException
    {
        try
        {
            return upload(baseDir, file,DEFAULT_ALLOWED_EXTENSION);
        }
        catch (Exception e)
        {
            throw new IOException(e.getMessage(), e);
        }
    }


    /**
     * 文件上传
     *
     * @param baseDir 相对应用的基目录
     * @param file 上传的文件
     * @return 返回上传成功的文件名
     * @throws IOException 比如读写文件出错时
     */
    public static final String upload(String baseDir, MultipartFile file,String sb) throws IOException {
        //验证文件名长度
        int fileNameLength = Objects.requireNonNull(file.getOriginalFilename()).length();
        if (fileNameLength > FileUtils.DEFAULT_FILE_NAME_LENGTH){
            throw new IedsException("文件名称太长");
        }
        //验证文件大小
        long size = file.getSize();
        if(size>DEFAULT_MAX_SIZE){
            throw new IedsException("文件不得超过30M");
        }
        //检测格式石否正确
        String extension = getExtension(file);
        if(!DEFAULT_ALLOWED_EXTENSION.equals(extension)){
            throw new IedsException("文件格式错误");
        }
        //构建文件名称
        String fileName = extractFilename(file);
        //创建文件
        File desc = getAbsoluteFile(baseDir, fileName);
        file.transferTo(desc);
        return baseDir+"/"+fileName;
    }


    public static final File getAbsoluteFile(String uploadDir, String fileName) {
        File desc = new File(uploadDir + File.separator + fileName);

        if (!desc.exists())
        {
            if (!desc.getParentFile().exists())
            {
                desc.getParentFile().mkdirs();
            }
        }
        return desc;
    }

    /**
     * 编码文件名
     */
    public static final String extractFilename(MultipartFile file){
        String fileName = file.getOriginalFilename();
        String extension = getExtension(file);
        fileName = UUID.randomUUID().toString() + "." + extension;
        return fileName;
    }

    /**
     * 获取文件名的后缀
     *
     * @param file 表单文件
     * @return 后缀名
     */
    public static final String getExtension(MultipartFile file){
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (StringUtils.isEmpty(extension))
        {
            extension = DEFAULT_ALLOWED_EXTENSION;
        }
        return extension;
    }

    /**
     *
     * @param filename 文件名
     * @param baseDir 文件地址
     * @param response
     */
    public static void download(String filename, String baseDir, HttpServletResponse response) {
        try {
            //关键点，需要获取的文件所在文件系统的目录，定位准确才可以顺利下载文件
            String filePath = baseDir+filename;
            //创建一个输入流，将读取到的文件保存到输入流
            InputStream fis = new BufferedInputStream(Files.newInputStream(Paths.get(filePath)));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 重要，设置response的Header指定一个名称
            response.setHeader("Content-Disposition", "attachment;filename=" + filename);
            //创建一个输出流，用于输出文件
            OutputStream oStream = new BufferedOutputStream(response.getOutputStream());
            //写入输出文件
            oStream.write(buffer);
            oStream.flush();
            oStream.close();
            System.out.println("下载日志文件" + filename +"成功");
        } catch (Exception e) {
            System.out.println("下载日志文件出错,错误原因:" + e);
        }
    }

}
