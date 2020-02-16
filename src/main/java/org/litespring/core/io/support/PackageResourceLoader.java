package org.litespring.core.io.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.litespring.core.io.FileSystemResource;
import org.litespring.util.Assert;
import org.litespring.util.ClassUtils;

import org.litespring.core.io.Resource;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @objective : 扫描包内的bean（dao，service，controller）
 * @date :2019/11/22- 7:51
 */
public class PackageResourceLoader {
    private static final Log logger = LogFactory.getLog(PackageResourceLoader.class);

    private final ClassLoader classLoader;

    public PackageResourceLoader() {
        this.classLoader = ClassUtils.getDefaultClassLoader();
    }

    public PackageResourceLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public ClassLoader getClassLoader(){
        return this.classLoader;
    }


    public Resource[] getResources(String basePackage) throws IOException {
        Assert.notNull(basePackage,"basePackage must not be null");
        // 将.转化为/
        // 例如 org.litespring.dao.v4 转化为 org/litespring/dao/v4
        String location = ClassUtils.convertClassNameToResourcePath(basePackage);
        // 找到location所指向的文件夹，并返回他的绝对路径
        ClassLoader cl = getClassLoader();
        URL url = cl.getResource(location);
        File rootDir = new File(url.getFile());
        // 递归查找该文件夹下面所有文件，装载进Set中
        Set<File> matchingFiles = retrieveMatchingFiles(rootDir);
        Resource[] result = new Resource[matchingFiles.size()];
        int i = 0;
        for (File file:matchingFiles){
            result[i++] = new FileSystemResource(file);
        }
        return result;
    }
    // 找到所有可读文件
    protected Set<File> retrieveMatchingFiles(File rootDir) throws IOException {
        // 判断该文件夹是否存在
        if (!rootDir.exists()){
            if(logger.isDebugEnabled()){
                logger.debug("Skipping ["+rootDir.getAbsolutePath()+"] because it does not exit");
            }
            return Collections.emptySet();
        }
        // 判断是否是文件夹
        if(!rootDir.isDirectory()){
            if(logger.isDebugEnabled()){
                logger.debug("Skipping ["+rootDir.getAbsolutePath()+"] because it does not a directory");
            }
            return Collections.emptySet();
        }
        // 判断该文件夹是否可读
        if (!rootDir.canRead()) {
            if (logger.isWarnEnabled()) {
                logger.warn("Cannot search for matching files underneath directory [" + rootDir.getAbsolutePath() +
                        "] because the application is not allowed to read the directory");
            }
            return Collections.emptySet();
        }
        // 获取该文件夹下所有文件
        Set<File> result = new LinkedHashSet<>(8);
        doRetrieveMatchingFiles(rootDir,result);
        return result;
    }
    // 递归查找所有可读文件
    protected void doRetrieveMatchingFiles(File dir, Set<File> result) throws IOException {
        // 获取该文件夹下所有文件
        File[] dirContents = dir.listFiles();
        if (dirContents == null){
            if (logger.isWarnEnabled()) {
                logger.warn("Could not retrieve contents of directory [" + dir.getAbsolutePath() + "]");
            }
            return;
        }
        for (File content : dirContents) {
            if(content.isDirectory()){
                // 是文件夹，进行递归查找文件
                if (!content.canRead()) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Skipping subdirectory [" + dir.getAbsolutePath() +
                                "] because the application is not allowed to read the directory");
                    }
                } else {
                    //在content目录下继续 todo 递归
                    doRetrieveMatchingFiles(content, result);
                }
            } else {
                //content是一个文件，直接将文件放入result中
                result.add(content);
            }
        }
    }
}
