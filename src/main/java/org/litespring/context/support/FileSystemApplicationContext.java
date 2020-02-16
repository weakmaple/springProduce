package org.litespring.context.support;

import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import org.litespring.context.ApplicationContext;
import org.litespring.core.io.FileSystemResource;
import org.litespring.core.io.Resource;

/**
 * @objective :
 * @date :2019/11/14- 8:09
 */
public class FileSystemApplicationContext extends AbstractApplicationContext {

    public FileSystemApplicationContext(String path) {
        super(path);
    }

    @Override
    protected Resource getResourceByPath(String path) {
        return new FileSystemResource(path);
    }
}
