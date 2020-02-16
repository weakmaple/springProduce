package org.litespring.test.v4;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.litespring.test.v3.ApplicationContextTestV3;
import org.litespring.test.v3.BeanDefinitionTestV3;
import org.litespring.test.v3.ConstructorResolverTest;

/**
 * @objective : 10
 * @date :2019年12月22日 13:11:11
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        ApplicationContextTestV4.class,
        AutowiredAnnotationProcessorTest.class,
        ClassPathBeanDefinitionScannerTest.class,
        ClassReaderTest.class,
        DependencyDescriptorTest.class,
        InjectionMetadataTest.class,
        MetadataReadTest.class,
        PackageResourceLoaderTest.class,
        XmlBeanDefinitionReaderTest.class,
})
public class V4AllTests {
}
