package org.litespring.core.type.classreading;

import org.litespring.core.io.ClassPathResource;
import org.litespring.core.io.Resource;
import org.litespring.core.type.AnnotationMetadata;
import org.litespring.core.type.ClassMetadata;
import org.springframework.asm.ClassReader;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @objective : 用于包装ClassMetadataReadingVisitor和AnnotationMetadataReadingVisitor
 * ClassMetadataReadingVisitor和AnnotationMetadataReadingVisitor分别实现了ClassMetadata和AnnotationMetadata接口
 *
 * 封装了将resource类转化为InputStream类的功能，并调用ASM框架的accept方法
 * @date :2019/11/24- 10:39
 */
public class SimpleMetadataReader implements MetadataReader {

    private final Resource resource;
    private final ClassMetadata classMetadata;
    private final AnnotationMetadata annotationMetadata;


    public SimpleMetadataReader(Resource resource) throws Exception {
        InputStream inputStream = new BufferedInputStream(resource.getInputStream());
        ClassReader classReader;
        try {
            classReader = new ClassReader(inputStream);
        }finally {
            inputStream.close();
        }
        AnnotationMetadataReadingVisitor visitor = new AnnotationMetadataReadingVisitor();
        classReader.accept(visitor,ClassReader.SKIP_DEBUG);
        /*
           使用accept之后的调用路径
           ClassMetadataReadingVisitor 下的 visit 函数 判断类对象的属性（是否为接口，是否为抽象类，是否有继承父类，是否有实现接口）
           AnnotationMetadataReadingVisitor 下的 visitAnnotation 函数 获取到注解的类路径（key），注解的所有属性（value），保存在map中
           其中要完成上一步的内容 需要调用 AnnotationAttributesReadingVisitor 下的 visit 函数 和 visitEnd 函数来完成
        */
        this.annotationMetadata = visitor;
        this.classMetadata = visitor;
        this.resource = resource;
    }

    @Override
    public Resource getResource() {
        return this.resource;
    }

    @Override
    public ClassMetadata getClassMetadata() {
        return this.classMetadata;
    }

    @Override
    public AnnotationMetadata getAnnotationMetadata() {
        return this.annotationMetadata;
    }
}
