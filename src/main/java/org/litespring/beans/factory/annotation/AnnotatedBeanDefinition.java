package org.litespring.beans.factory.annotation;

import org.litespring.core.type.AnnotationMetadata;

/**
 * @objective : 与注解相关的bean的设计
 * @date :2019/11/25- 7:52
 */
public interface AnnotatedBeanDefinition {
    AnnotationMetadata getMetadata();
}
