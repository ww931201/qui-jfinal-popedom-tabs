package com.sinorail.cwzywst.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 *
 * @author sohnny
 */
public class FileUtils {
   
    /**
     * 根据路径创建一个默认权限的空文件
     *
     * @param file 要创建的文件路径
     */
    public static void createFile(Path file) {
        if (Files.notExists(file)) {
            try {
                Files.createDirectories(file.getParent());
                //System.out.println(file.getParent().toString());
                Files.createFile(file);
            } catch (FileAlreadyExistsException x) {
                System.err.format("file named %s"
                        + " already exists%n", file);
            } catch (IOException x) {
                // Some other sort of failure, such as permissions.
                System.err.format("createFile error: %s%n", x);
            }
        }
    }
    
    public static boolean createDir(String destDirName) {  
        File dir = new File(destDirName);  
        if (dir.exists()) {  
            System.out.println("创建目录" + destDirName + "失败，目标目录已经存在");  
            return false;  
        }  
        if (!destDirName.endsWith(File.separator)) {  
            destDirName = destDirName + File.separator;  
        }  
        //创建目录  
        if (dir.mkdirs()) {  
            System.out.println("创建目录" + destDirName + "成功！");  
            return true;  
        } else {  
            System.out.println("创建目录" + destDirName + "失败！");  
            return false;  
        }  
    } 

}