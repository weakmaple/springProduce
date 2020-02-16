package org.litespring.beans.factory.config;

import org.litespring.beans.BeansException;

/**
 * @objective :
 * @date :2019/12/22- 9:42
 */
public interface BeanPostProcessor {
    Object beforeInitialization(Object bean,String beanName) throws BeansException;
    Object afterInitialization(Object bean,String beanName) throws BeansException;
}
