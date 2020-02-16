package org.litespring.beans.factory;

import org.litespring.beans.BeansException;

/**
 * @objective :
 * @date :2020/1/2- 18:27
 */
public interface BeanFactoryAware {
    void setBeanFactory(BeanFactory beanFactory) throws BeansException;
}
