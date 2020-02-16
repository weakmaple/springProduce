package org.litespring.beans.factory.support;

import org.litespring.beans.BeanDefinition;

/**
 * @objective : bean定义接口
 * @date :2019/11/13- 12:59
 */
public interface BeanDefinitionRegistry {
    BeanDefinition getBeanDefinition(String beanID);
    void registerBeanDefinition(String beanID, BeanDefinition bd);
}
