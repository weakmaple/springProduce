package org.litespring.test.v4;

import java.lang.reflect.Field;

import org.junit.Assert;
import org.junit.Test;
import org.litespring.beans.factory.config.DependencyDescriptor;
import org.litespring.beans.factory.support.DefaultBeanFactory;
import org.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import org.litespring.core.io.ClassPathResource;
import org.litespring.core.io.Resource;
import org.litespring.dao.v4.AccountDao;
import org.litespring.service.v4.PetStoreService;

/**
 * @objective : 7
 * @date :2019/11/27- 10:33
 * 测试从BeanFactory中寻找类型为accountDao的对象
 */
public class DependencyDescriptorTest {

    // 测试将获取到的BeanDefinition转化为Bean之后
	@Test
	public void testResolveDependency() throws Exception{
		DefaultBeanFactory factory = new DefaultBeanFactory();
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
		Resource resource = new ClassPathResource("petstore-v4.xml");
		reader.loadBeanDefinitions(resource);
		
		Field f = PetStoreService.class.getDeclaredField("accountDao");
		DependencyDescriptor descriptor = new DependencyDescriptor(f,true);
		//从BeanFactory中寻找类型为accountDao的对象
		Object o = factory.resolveDependency(descriptor);
		Assert.assertTrue(o instanceof AccountDao);
	}
}
