package org.litespring.context.support;

import org.litespring.beans.BeanDefinition;
import org.litespring.beans.factory.NoSuchBeanDefinitionException;
import org.litespring.beans.factory.annotation.AutowiredAnnotationProcessor;
import org.litespring.beans.factory.config.ConfigurableBeanFactory;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import org.litespring.context.ApplicationContext;
import org.litespring.core.io.Resource;
import org.litespring.util.ClassUtils;

/**
 * @objective : 封装了factory和xml文件解析的流程 也就是loadBeanDefinitions方法的执行
 * @date :2019/11/14- 12:37
 */
public abstract class AbstractApplicationContext implements ApplicationContext {
    private DefaultBeanFactory factory = null;
    private ClassLoader classLoader;

    // 使用默认classLoader的构造器
    public AbstractApplicationContext(String configFile){
        this(configFile,ClassUtils.getDefaultClassLoader());
    }
    // 自定义classLoader的构造器
    public AbstractApplicationContext(String configFile, ClassLoader cl) {
        factory = new DefaultBeanFactory();
        // 用于解析xml文件的类
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        // 找到配置文件
        Resource resource = this.getResourceByPath(configFile);
        // 真正解析配置文件
        reader.loadBeanDefinitions(resource);
        // 设置classLoader
        factory.setBeanClassLoader(cl);
        // 注册BeanPostProcessors
        registerBeanPostProcessors(factory);
    }

    protected abstract Resource getResourceByPath(String path);

    @Override
    public Object getBean(String beanId) {
        return factory.getBean(beanId);
    }


    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }


    public ClassLoader getClassLoader() {
        return (this.classLoader != null?this.classLoader: ClassUtils.getDefaultClassLoader());
    }
    // 注册BeanPostProcessors
    protected void registerBeanPostProcessors(ConfigurableBeanFactory beanFactory) {
        AutowiredAnnotationProcessor postProcessor = new AutowiredAnnotationProcessor();
        postProcessor.setBeanFactory(beanFactory);
        beanFactory.addBeanPostProcessor(postProcessor);

    }

    @Override
    public Class<?> getType(String name) throws NoSuchBeanDefinitionException {
        return this.factory.getType(name);
    }
}
