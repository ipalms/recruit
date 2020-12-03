package com.geek.geekstudio.util;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @className: FileUtil
 * @description: 文件工具类
 *
 *
 * root_dir: 数据库配置目录
 * announce：root_dir/上传人id/
 * material:
 * software:
 * task:     root_dir/教师用户名/教师课程id/任务id/
 * work:     root_dir/教师用户名/教师课程id/学生学号-姓名/
 *
 * @author: ZSZ
 * @date: 2019/10/30 18:54
 */
@Data
@Component
public class FileUtil {


    private Path fileStorageLocation; // 文件在本地存储的地址

    private String announceFilePath;

    private String materialFilePath;

    private String softwareFilePath;

    private String taskFilePath;

    private String workFilePath;

    //临时文件目录
    private String tmpFilePath;

    /*@PostConstruct
    public void init(){
        String rootDir = labSystemMapper.queryByKeyName("root_dir");
        this.fileStorageLocation = Paths.get(rootDir).toAbsolutePath().normalize();
        this.announceFilePath = fileStorageLocation.toString() + "/announce/";
        this.materialFilePath = fileStorageLocation.toString() + "/material/";
        this.softwareFilePath = fileStorageLocation.toString() + "/software/";
        this.taskFilePath = fileStorageLocation.toString() + "/task/";
        this.workFilePath = fileStorageLocation.toString() + "/work/";
        this.tmpFilePath = fileStorageLocation.toString() + "/zip/";

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("创建上传目录失败");
        }
    }*/

    /**
     * 重新构建作业文件名
     * @param userId
     * @param userName
     * @param task
     * @param fileName
     * @return
     */
    public String getWorkName(String userId,String userName,String task,String fileName){
        int index = fileName.lastIndexOf('.');
        String suffix = fileName.substring(index);
        return userId+"-"+userName+"-"+task+suffix;
    }

    /**
     * 存储文件到系统
     *
     * @param file 文件
     * @return 文件名
     */
    public String storeFile(String path, MultipartFile file, String fileName) throws Exception {
        String filePath;
        try {
            if (file.isEmpty()) {
                throw new Exception("文件为空");
            }

            // 设置文件存储路径
            filePath= path + fileName;
            File dest = new File(filePath);
            // 检测是否存在目录
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();// 新建文件夹
            }
            // 检测是否存在该文件
//            if (dest.exists()) {
//                throw new Exception("覆盖同名文件！");
//            }
            //覆盖同名文件
            file.transferTo(dest);// 文件写入
        } catch (IOException e) {
            throw new Exception("文件上传失败！");
        }
        //返回绝对路径去除fileStorageLocation的部分
        return path.substring(fileStorageLocation.toString().length())+fileName;
    }

    public boolean deleteFile(String filePath) throws Exception {
        if(!StringUtils.isEmpty(filePath)){
            File file = new File(filePath);
            if(file.exists()){
                if(file.delete()){
                    return true;
                }else{
                    return false;
                }
            }else{
                throw new Exception("文件不存在！");
            }
        }
        return false;
    }

    public boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                 boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    /**
     * 返回前端可以访问本地文件url
     * @param baseUrl
     * @param filePath
     * @return
     */
    public String getFileUrl(String baseUrl,String filePath){
                return baseUrl+"/lab"+filePath;
    }


    /**
     * 压缩文件
     * @param srcFiles
     * @param zipPath
     */
    public void zipFileWithTier(String srcFiles, String zipPath) {
        try {
            File file = new File(zipPath);
            if(file.exists()){
                file.delete();
            }
            file.createNewFile();
            FileOutputStream zipFile = new FileOutputStream(file);
            BufferedOutputStream buffer = new BufferedOutputStream(zipFile);
            ZipOutputStream out = new ZipOutputStream(buffer);
            zipFiles(srcFiles, out, "");
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 压缩文件
     * @param filePath
     * @param out
     * @param prefix
     * @throws IOException
     */
    public void zipFiles(String filePath, ZipOutputStream out, String prefix)
            throws IOException {
        File file = new File(filePath);
        if (file.isDirectory()) {
            if (file.listFiles().length == 0) {
                ZipEntry zipEntry = new ZipEntry(prefix + file.getName() + "/");
                out.putNextEntry(zipEntry);
                out.closeEntry();
            } else {
                prefix += file.getName() + File.separator;
                for (File f : file.listFiles())
                    zipFiles(f.getAbsolutePath(), out, prefix);
            }
        } else {
            FileInputStream in = new FileInputStream(file);
            ZipEntry zipEntry = new ZipEntry(prefix + file.getName());
            out.putNextEntry(zipEntry);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.closeEntry();
            in.close();
        }

    }

    /**
     * 解压问价
     * @param bytes
     * @param prefix
     * @throws IOException
     */
    public void unzipFilesWithTier(byte[] bytes, String prefix) throws IOException {

        InputStream bais = new ByteArrayInputStream(bytes);
        ZipInputStream zin = new ZipInputStream(bais);
        ZipEntry ze;
        while ((ze = zin.getNextEntry()) != null) {
            if (ze.isDirectory()) {
                File file = new File(prefix + ze.getName());
                if (!file.exists())
                    file.mkdirs();
                continue;
            }
            File file = new File(prefix + ze.getName());
            if (!file.getParentFile().exists())
                file.getParentFile().mkdirs();
            ByteArrayOutputStream toScan = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int len;
            while ((len = zin.read(buf)) > 0) {
                toScan.write(buf, 0, len);
            }
            byte[] fileOut = toScan.toByteArray();
            toScan.close();
            writeByteFile(fileOut, new File(prefix + ze.getName()));
        }
        zin.close();
        bais.close();
    }

    public byte[] readFileByte(String filename) throws IOException {

        if (filename == null || filename.equals("")) {
            throw new NullPointerException("File is not exist!");
        }
        File file = new File(filename);
        long len = file.length();
        byte[] bytes = new byte[(int) len];

        BufferedInputStream bufferedInputStream = new BufferedInputStream(
                new FileInputStream(file));
        int r = bufferedInputStream.read(bytes);
        if (r != len)
            throw new IOException("Read file failure!");
        bufferedInputStream.close();

        return bytes;

    }

    public String writeByteFile(byte[] bytes, File file) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "success";
    }

    /**
     * 压缩以";"分隔的文件
     * @param pathList
     * @param zipPath
     */
    public void zipFileByPathList(String[] pathList,String zipPath,String prefix) throws IOException {
        File zipFile = new File(zipPath);
        if(zipFile.exists()) {
            zipFile.delete();
        }
        File dirPath = zipFile.getParentFile();
        if(!dirPath.exists()){
            dirPath.mkdirs();
            zipFile.createNewFile();
        }
        ZipOutputStream out = new ZipOutputStream(
                new BufferedOutputStream( new FileOutputStream(zipFile)));
        for (String path:pathList) {
            String srcFile = prefix + path;
            File file = new File(srcFile);
            if(!file.exists()){
                continue;
            }
            zipFiles(srcFile,out,"");
        }
        //不关闭会出现错误
        out.close();
    }


}
