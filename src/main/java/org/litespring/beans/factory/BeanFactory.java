package org.litespring.beans.factory;

import org.litespring.beans.BeanDefinition;
/**
 * @objective :
 * @date :2019/11/12- 9:40
 */
public interface BeanFactory {

    //public BeanDefinition getBeanDefinition(String beanId);
    // 获得bean实例
    Object getBean(String beanId);

    Class<?> getType(String targetBeanName) throws NoSuchBeanDefinitionException;
}
