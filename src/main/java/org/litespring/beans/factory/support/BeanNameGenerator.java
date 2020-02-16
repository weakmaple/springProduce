package org.litespring.beans.factory.support;

import org.litespring.beans.BeanDefinition;

/**
 * @objective : 设置bean的id
 * @date :2019/11/27- 11:37
 */
public interface BeanNameGenerator {
    String generateBeanName(BeanDefinition definition,BeanDefinitionRegistry registry);
}
