package org.litespring.beans.factory.config;

import org.litespring.beans.factory.BeanFactory;

/**
 * @objective : 对比factory中的bean的classloader，确定两者的类路径是否相同
 * @date :2019/11/29- 9:47
 */
public interface AutowireCapableBeanFactory extends BeanFactory {
    public Object resolveDependency(DependencyDescriptor dependencyDescriptor);
}
