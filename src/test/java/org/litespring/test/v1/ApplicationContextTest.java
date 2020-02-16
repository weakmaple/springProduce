package org.litespring.test.v1;

import org.junit.Test;
import org.litespring.context.ApplicationContext;
import org.litespring.context.support.ClassPathXmlApplicationContext;
import org.litespring.context.support.FileSystemApplicationContext;
import org.litespring.service.v1.PetStoreService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @objective : 2
 * @date :2019/11/13- 13:24
 */
public class ApplicationContextTest {

    // 测试ClassPathXmlApplicationContext（相对路径）
    @Test
    public void test1(){
        ApplicationContext ctx = new ClassPathXmlApplicationContext("petstore-v1.xml");
        PetStoreService petStore = (PetStoreService)ctx.getBean("perStore");
        assertNotNull(petStore);
    }

    // 测试FileSystemApplicationContext（绝对路径）
    @Test
    public void test2(){
        // F:\study_again\java\springProduce\src\main\resources\petstore-v1.xml
        // F:\study_again\java\springProduce\src\main\resources\petstore-v1.xml
        ApplicationContext ctx = new FileSystemApplicationContext("src\\main\\resources\\petstore-v1.xml");
        PetStoreService petStore = (PetStoreService)ctx.getBean("perStore");
        assertNotNull(petStore);
    }
}
