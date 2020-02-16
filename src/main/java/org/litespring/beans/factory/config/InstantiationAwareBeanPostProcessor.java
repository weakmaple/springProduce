package org.litespring.beans.factory.config;

import org.litespring.beans.BeansException;

/**
 * @objective :
 * @date :2019/12/22- 9:45
 */
public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor {
    Object beforeInstantiation(Class<?> beanClass,String beanName) throws BeansException;
    Object afterInstantiation(Object bean,String beanName) throws BeansException;
    void postProcessPropertyValues(Object bean,String beanName) throws BeansException;
}
