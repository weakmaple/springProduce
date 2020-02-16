package org.litespring.test.v1;

import org.junit.Assert;
import org.junit.Test;
import org.litespring.core.io.ClassPathResource;
import org.litespring.core.io.FileSystemResource;
import org.litespring.core.io.Resource;

import java.io.InputStream;

/**
 * @objective : 3
 * @date :2019/11/14- 7:44
 */
public class ResourceTest {

    // 配置相对路径读取配置文件
    @Test
    public void test1() throws Exception{
        Resource r = new ClassPathResource("petstore-v1.xml");

        InputStream is = null;
        try {
            is = r.getInputStream();
            Assert.assertNotNull(is);
        }finally {
            if(is != null){
                is.close();
            }
        }
    }

    // 测试绝对路径读取配置文件
    @Test
    public void test2() throws Exception{
        // F:\study_again\java\springProduce\src\main\resources\petstore-v1.xml
        // H:\study_again\java\springProduce\src\main\resources\petstore-v1.xml
        Resource r = new FileSystemResource("src\\main\\resources\\petstore-v1.xml");

        InputStream is = null;
        try {
            is = r.getInputStream();
            Assert.assertNotNull(is);
        }finally {
            if(is != null){
                is.close();
            }
        }
    }
}
