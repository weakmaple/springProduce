package org.litespring.core.io;

import org.litespring.util.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @objective : 使用文件读取的方式获取配置文件
 * @date :2019/11/14- 7:57
 */
public class FileSystemResource implements Resource {

    private final String path;
    private final File file;

    public FileSystemResource(File file) {
        this.path = file.getPath();
        this.file = file;
    }

    public FileSystemResource(String path) {
        Assert.notNull(path,"path must not be null");
        this.file = new File(path);
        this.path = path;
    }

    @Override
    public InputStream getInputStream() throws Exception {
        return new FileInputStream(this.file);
    }

    @Override
    public String getDescription() {
        return "file {"+this.file.getAbsolutePath()+"}";
    }


}
