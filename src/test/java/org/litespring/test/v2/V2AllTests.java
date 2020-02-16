package org.litespring.test.v2;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.litespring.test.v1.ApplicationContextTest;
import org.litespring.test.v1.BeanFactoryTest;
import org.litespring.test.v1.ResourceTest;

/**
 * @objective : 7
 * @date :2019/11/14- 8:06
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        ApplicationContextTestV2.class,
        BeanDefinitionTestV2.class,
        BeanDefinitionValueResolverTest.class,
        CustomBooleanEditorTest.class,
        CustomerNumberTest.class,
        TypeConverterTest.class,
})
public class V2AllTests {
}
