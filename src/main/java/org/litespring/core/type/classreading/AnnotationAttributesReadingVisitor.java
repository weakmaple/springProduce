package org.litespring.core.type.classreading;

import org.litespring.core.annotation.AnnotationAttributes;
import org.springframework.asm.AnnotationVisitor;
import org.springframework.asm.SpringAsmInfo;

import java.util.Map;

/**
 * @objective : 将注解的类名，注解的属性（key=value）的内容保存在map中
 * @date :2019/11/23- 10:15
 */
public class AnnotationAttributesReadingVisitor extends AnnotationVisitor {
    // 注解类路径
    private final String annotationType;

    // 用于保存注解中的也就是()中的值的key和value
    AnnotationAttributes attributes = new AnnotationAttributes();

    // key为注解类路径，value为注解()中的内容,也就是上方两个属性的综合
    private final Map<String,AnnotationAttributes> attributeMap;

    public AnnotationAttributesReadingVisitor(String annotationType, Map<String, AnnotationAttributes> attributeMap) {
        super(SpringAsmInfo.ASM_VERSION);
        this.annotationType = annotationType;
        this.attributeMap = attributeMap;
    }
    // 由asm调用，key为注解类路径，value为注解()中的内容,也就是上方两个属性的综合
    @Override
    public final void visitEnd() {
        this.attributeMap.put(this.annotationType,this.attributes);
    }
    // 由asm调用，将注解的key和value添加到保存的map中
    public void visit(String attributeName, Object attributeValue){
        // 将注解的key和value添加到保存的map中
        this.attributes.put(attributeName,attributeValue);
    }
}
