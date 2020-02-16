package org.litespring.context.annotation;

import org.litespring.beans.factory.annotation.AnnotatedBeanDefinition;
import org.litespring.beans.factory.support.GenericBeanDefinition;
import org.litespring.core.type.AnnotationMetadata;

/**
 * @objective : 如果使用context:component-scan，扫描出来的bean是缺少id属性的
 *               为了不污染GenericBeanDefinition，使用ScannedGenericBeanDefinition来完成bean设置id的任务
 *               即为加上了注解的BeanDefinition
 * @date :2019/11/25- 7:49
 */
public class ScannedGenericBeanDefinition extends GenericBeanDefinition implements AnnotatedBeanDefinition {
    private final AnnotationMetadata metadata;

    public ScannedGenericBeanDefinition(AnnotationMetadata metadata) {
        super();
        this.metadata = metadata;
        // 这里的setBeanClassName设置的是全类名
        setBeanClassName(this.metadata.getClassName());
    }

    public final AnnotationMetadata getMetadata(){
        return this.metadata;
    }
}
