package org.litespring.core.type.classreading;

import org.litespring.core.annotation.AnnotationAttributes;
import org.litespring.core.type.AnnotationMetadata;
import org.springframework.asm.AnnotationVisitor;
import org.springframework.asm.Type;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * @objective : 获取到注解的类路径（key），注解的所有属性（value），保存在map中
 * @date :2019/11/23- 10:02
 */
public class AnnotationMetadataReadingVisitor extends ClassMetadataReadingVisitor implements AnnotationMetadata {
    // 用于保存注解的set
    private final Set<String> annotationSet = new LinkedHashSet<String>(4);
    // key为注解类路径，value为注解()中的内容,
    private final Map<String, AnnotationAttributes> attributeMap = new LinkedHashMap<String, AnnotationAttributes>(4);

    public AnnotationMetadataReadingVisitor() {

    }
    // 由asm调用，逐个扫描类上的注解
    @Override
    public AnnotationVisitor visitAnnotation(final String desc, boolean visible) {
        // 获得要扫描的类的注解
        String className = Type.getType(desc).getClassName();
        // 添加到保存注解的set中
        this.annotationSet.add(className);
        return new AnnotationAttributesReadingVisitor(className,this.attributeMap);
    }

    public Set<String> getAnnotationTypes(){
        return this.annotationSet;
    }

    public boolean hasAnnotation(String annotationType){
        return this.annotationSet.contains(annotationType);
    }

    public AnnotationAttributes getAnnotationAttributes(String annotationType){
        return this.attributeMap.get(annotationType);
    }
}
