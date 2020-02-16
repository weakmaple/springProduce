package org.litespring.test.v1;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.litespring.beans.BeanDefinition;
import org.litespring.beans.factory.BeanCreationException;
import org.litespring.beans.factory.BeanDefinitionStoreException;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import org.litespring.core.io.ClassPathResource;
import org.litespring.core.io.Resource;
import org.litespring.service.v1.PetStoreService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @objective : 1
 * @date :2019/11/12- 9:23
 */
public class BeanFactoryTest {

    DefaultBeanFactory factory;
    XmlBeanDefinitionReader reader;
    @Before
    public void setUp(){
        factory = new DefaultBeanFactory();
        reader = new XmlBeanDefinitionReader(factory);
    }


    // 测试获取到BeanDefinition
    @Test
    public void testGetBean(){
        // 获取配置文件
        Resource resource =  new ClassPathResource("petstore-v1.xml");
        reader.loadBeanDefinitions(resource);
        // 获取BeanDefinition
        BeanDefinition bd = factory.getBeanDefinition("perStore");

        // 判断是否为单例模式
        assertTrue(bd.isSingleton());
//        assertTrue(bd.isPrototype());
        assertEquals(BeanDefinition.SCOPE_DEFAULT,bd.getScope());
        // 断言判断是否获得正确
        assertEquals("org.litespring.service.v1.PetStoreService",bd.getBeanClassName());
        // 获取bean实例对象
        PetStoreService petStore = (PetStoreService)factory.getBean("perStore");
        // 断言判断是否为null
        assertNotNull(petStore);
    }

    // 测试无效的bean的name
    @Test
    public void testInvalidBean(){
        // 获取配置文件
        Resource resource =  new ClassPathResource("petstore-v1.xml");
        reader.loadBeanDefinitions(resource);
        try{
            factory.getBean("invalidBean");
        }catch (BeanCreationException e){
            return;
        }
        Assert.fail("exception BeanCreationException");
    }

    // 测试无效的配置文件路径（XML路径）
    @Test
    public void testInvalidXML(){
        try {
            // 获取配置文件
            DefaultBeanFactory factory = new DefaultBeanFactory();
            XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
            Resource resource =  new ClassPathResource("xxxxxx.xml");
            reader.loadBeanDefinitions(resource);
        }catch (BeanDefinitionStoreException e){
            return;
        }
        Assert.fail("exception DefaultBeanFactory");
    }

}











