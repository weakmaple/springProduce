package org.litespring.core.io;

import java.io.InputStream;

/**
 * @objective : 加载配置文件接口
 * @date :2019/11/14- 7:52
 */
public interface Resource {
    public InputStream getInputStream() throws Exception;
    public String getDescription();
}
