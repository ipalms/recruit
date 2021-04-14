package com.geek.geekstudio.util;

import com.geek.geekstudio.exception.RecruitFileException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
  *image位置   /all/image/a.jpg
 */
@Data
@Component
public class FileUtil {

    private Path fileStorageLocation; // 文件在本地存储的根地址

    private String announceFilePath;

    private String taskFilePath;

    private String workFilePath;

    private String imageFilePath;

    private String articleFilePath;

    //临时文件目录
    private String tmpFilePath;

    @Value("${files.path}")
    private String rootDir;

    @Value("${server.port}")
    private String port;

    @Value("${application.address}")
    private String address;

    String baseUrl;

    /**
     * FileUtil类的构造函数被执行完且依赖注入完成后才执行的方法
     */
    @PostConstruct
    public void init(){
        //String rootDir="/all";
        //获取绝对路径
        this.fileStorageLocation = Paths.get(rootDir).toAbsolutePath().normalize();
        //System.out.println("fileStorageLocation: "+fileStorageLocation);
        this.announceFilePath = fileStorageLocation.toString() + "/announce/";
        this.taskFilePath = fileStorageLocation.toString() + "/task/";
        this.workFilePath = fileStorageLocation.toString() + "/work/";
        this.imageFilePath = fileStorageLocation.toString() + "/image/";
        this.articleFilePath = fileStorageLocation.toString() + "/article/";
        this.tmpFilePath = fileStorageLocation.toString() + "/zip/";
        //构造baseUrl
        baseUrl="http://"+address+":"+port;
        System.out.println("baseUrl:"+baseUrl);
        try {
            if(!Files.exists(this.fileStorageLocation)) {
                Files.createDirectories(this.fileStorageLocation);
            }
        } catch (Exception ex) {
            throw new RuntimeException("创建上传目录失败");
        }
    }

    /**
     * 存储文件到系统
     * @param file 文件
     * @return 文件名
     */
    public String storeFile(String path, MultipartFile file, String fileName) throws RecruitFileException {
        String filePath;
        try {
            if (file.isEmpty()) {
                throw new RecruitFileException("文件为空");
            }
            // 设置文件存储路径
            filePath= path + fileName;
            File dest = new File(filePath);
            // 检测是否存在父目录
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();// 新建文件夹
            }
            /*// 检测是否存在该文件
            if (dest.exists()) {
                throw new Exception("覆盖同名文件！");
            }*/
            //覆盖同名文件
            file.transferTo(dest);// 文件写入
        } catch (IOException e) {
            throw new RecruitFileException("文件上传失败！");
        }
        //返回绝对路径去除fileStorageLocation的部分 eg:/image/a.jpg
        return path.substring(fileStorageLocation.toString().length())+fileName;
    }

    //删除某个文件
    public boolean deleteFile(String filePath) throws RecruitFileException {
        if(!StringUtils.isEmpty(filePath)){
            File file = new File(filePath);
            if(file.exists()){
                //考虑删除后判断当父目录为空将父目录也删除掉
                return file.delete();
            }else{
                throw new RecruitFileException("文件不存在！");
            }
        }
        return false;
    }

    //递归删除某个文件夹
    public boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            if(children!=null){
                for (String child : children) {
                    boolean success = deleteDir(new File(dir, child));
                    if (!success) {
                        return false;
                    }
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    //递归删除某个文件夹下的所有文件[最后会留存一个空文件夹]   NIO
    public boolean deleteDir2(String dir)  {
        Path directory=Paths.get(dir);
        try {
            if (Files.isDirectory(directory)) {
                try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
                    for (Path e : stream) {
                        if (Files.isDirectory(e)) {//是文件夹就递归删除
                            deleteDir2(e.toRealPath().toString());
                        }
                        //遍历删除文件(或空文件夹)
                        Files.delete(e);
                    }
                }
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }


    /**
     * 压缩文件
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
     */
    public void zipFiles(String filePath, ZipOutputStream out, String prefix)
            throws IOException {
        File file = new File(filePath);
        if (file.isDirectory()) {
            if (Objects.requireNonNull(file.listFiles()).length == 0) {
                ZipEntry zipEntry = new ZipEntry(prefix + file.getName() + "/");
                out.putNextEntry(zipEntry);
                out.closeEntry();
            } else {
                prefix += file.getName() + File.separator;
                for (File f : Objects.requireNonNull(file.listFiles()))
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
     * 解压文件
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
     * 压缩pathList[]路径下的文件到zipPath中
     */
    public void zipFileByPathList(String[] pathList,String zipPath) throws IOException {
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
            //String srcFile = prefix + path;
            File file = new File(path);
            if(!file.exists()){
                continue;
            }
            zipFiles(path,out,"");
        }
        //不关闭会出现错误
        out.close();
    }

    /**
     * 返回前端可以访问本地文件url（url中输入访问到实际资源的路径）
     */
    public String getFileUrl(String filePath){
        return this.baseUrl+"/source"+filePath;
    }

    /**
     * 得到某一任务所有作业的父目录
     */
    public String getAllWork(int courseId, int taskId){
        return this.workFilePath+courseId+"/"+taskId;
    }

    /**
     *获得服务器上资源完整的路径(实际的物理路径)
     */
    public String buildPath(String url){
        return this.fileStorageLocation.toString()+url;
    }

    /**
     *构建文章附件的存储位置（考虑加上adminId）
     */
    public String buildArticleFilePath(int articleId) {
        return this.articleFilePath+articleId+"/";
    }

    /**
     * 构建公告所涉及文件的存储位置
     */
    public String buildAnnounceFilePath(int courseId){
        return this.announceFilePath+courseId+"/";
    }

    public String buildAnnounceFullPath(String filePath, String fileName) {
        return filePath.substring(fileStorageLocation.toString().length())+fileName;
    }

    /**
     *构建task文件的存储位置
     */
    public String buildTaskFilePath(int courseId,int taskId) {
        return this.taskFilePath+courseId+"/"+taskId+"/";
    }

    /**
     *构建work文件的存储位置
     */
    public String buildWorkFilePath(int courseId,String userId, int taskId) {
        return this.workFilePath+courseId+"/"+taskId+"/"+userId+"/";
    }
}

