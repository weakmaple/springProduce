package org.litespring.test.v4;


import org.junit.Assert;
import org.junit.Test;
import org.litespring.core.annotation.AnnotationAttributes;
import org.litespring.core.io.ClassPathResource;
import org.litespring.core.type.AnnotationMetadata;
import org.litespring.core.type.classreading.MetadataReader;
import org.litespring.core.type.classreading.SimpleMetadataReader;
import org.litespring.stereotype.Component;

import java.io.IOException;

/**
 * @objective : 4
 * @date :2019/11/24- 10:26
 */
public class MetadataReadTest {

    /*
    * 测试MetadataReader和SimpleMetadataReader，
    * 其中SimpleMetadataReader封装了Resource，ClassMetadata和AnnotationMetadata
    * */
    @Test
    public void testGetMetadata() throws Exception {
        ClassPathResource resource = new ClassPathResource("org/litespring/service/v4/PetStoreService.class");
        MetadataReader reader = new SimpleMetadataReader(resource);

        AnnotationMetadata amd = reader.getAnnotationMetadata();

        String annotation = Component.class.getName();

        Assert.assertTrue(amd.hasAnnotation(annotation));
        AnnotationAttributes attributes = amd.getAnnotationAttributes(annotation);
        Assert.assertEquals("petStore", attributes.get("value"));

        Assert.assertFalse(amd.isAbstract());
        Assert.assertFalse(amd.isFinal());
        Assert.assertEquals("org.litespring.service.v4.PetStoreService", amd.getClassName());
    }
}
