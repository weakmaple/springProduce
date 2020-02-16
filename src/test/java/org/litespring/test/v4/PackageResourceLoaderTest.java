package org.litespring.test.v4;

import org.junit.Assert;
import org.junit.Test;
import org.litespring.core.io.Resource;
import org.litespring.core.io.support.PackageResourceLoader;

import java.io.IOException;

/**
 * @objective : 2
 * @date :2019/11/22- 7:47
 */
public class PackageResourceLoaderTest {

    // 把指定目录下的类转换成resources类
    @Test
    public void testGetResources() throws IOException{
        PackageResourceLoader loader = new PackageResourceLoader();
        Resource[] resources = loader.getResources("org.litespring.dao.v4");
        Assert.assertEquals(2,resources.length);
    }
}
