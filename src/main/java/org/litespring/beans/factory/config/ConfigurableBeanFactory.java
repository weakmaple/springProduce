package org.litespring.beans.factory.config;

import org.litespring.beans.factory.BeanFactory;

import java.util.List;

/**
 * @objective : bean配置接口,主要用于设置classLoader
 * @date :2019/11/14- 12:52
 */
public interface ConfigurableBeanFactory extends AutowireCapableBeanFactory {
    void setBeanClassLoader(ClassLoader classLoader);
    ClassLoader getClassLoader();
    void addBeanPostProcessor(BeanPostProcessor postProcessor);
    List<BeanPostProcessor> getBeanPostProcessors();
}
