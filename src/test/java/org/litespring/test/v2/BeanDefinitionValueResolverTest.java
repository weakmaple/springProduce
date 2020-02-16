package org.litespring.test.v2;

import org.junit.Assert;
import org.junit.Test;
import org.litespring.beans.factory.config.RuntimeBeanReference;
import org.litespring.beans.factory.config.TypeStringValue;
import org.litespring.beans.factory.support.BeanDefinitionValueResolver;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import org.litespring.core.io.ClassPathResource;
import org.litespring.dao.v2.AccountDao;
import org.litespring.service.v2.PetStoreService;

/**
 * @objective : 3
 *     // 将获取到的属性（保存的形式是TypeStringValue和RuntimeBeanReference）
 *     // ref属性的使用getbean获取，value直接强制类型转换
 *     // 然后转化为Object类型
 * @date :2019/11/17- 10:44
 */
public class BeanDefinitionValueResolverTest {

   // 测试RuntimeBeanReference类型的转换
    @Test
    public void test1(){
        DefaultBeanFactory factory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        reader.loadBeanDefinitions(new ClassPathResource("petstore-v2.xml"));

        BeanDefinitionValueResolver resolver = new BeanDefinitionValueResolver(factory);

        RuntimeBeanReference reference = new RuntimeBeanReference("accountDao");
        Object value = resolver.resolverValueIfNecessary(reference);

        Assert.assertNotNull(value);
        Assert.assertTrue(value instanceof AccountDao);
    }

    // 测试TypeStringValue类型的转换
    @Test
    public void test2(){
        DefaultBeanFactory factory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        reader.loadBeanDefinitions(new ClassPathResource("petstore-v2.xml"));

        BeanDefinitionValueResolver resolver = new BeanDefinitionValueResolver(factory);


        TypeStringValue stringValue = new TypeStringValue("test");
        Object value = resolver.resolverValueIfNecessary(stringValue);

        Assert.assertNotNull(value);
        Assert.assertEquals("test",value);
    }
}
