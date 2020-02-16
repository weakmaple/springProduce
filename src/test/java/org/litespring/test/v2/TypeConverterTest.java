package org.litespring.test.v2;

import org.junit.Assert;
import org.junit.Test;
import org.litespring.beans.TypeMismatchException;
import org.litespring.beans.SimpleTypeConverter;
import org.litespring.beans.TypeConverter;

/**
 * @objective : 6
 * @date :2019/11/19- 7:29
 */
public class TypeConverterTest {

    // 测试int类型的转换
    @Test
    public void testConvertStringToInt(){
        TypeConverter converter = new SimpleTypeConverter();
        Integer i = converter.convertIfNecessary("3",Integer.class);
        Assert.assertEquals(3,i.intValue());
        try{
            converter.convertIfNecessary("3.1",Integer.class);
        }catch (TypeMismatchException e){
            return;
        }
        Assert.fail();
    }

    // 测试boolean类型的转换
    @Test
    public void testConvertStringToBoolean(){
        TypeConverter converter = new SimpleTypeConverter();
        Boolean b = converter.convertIfNecessary("true",Boolean.class);
        Assert.assertEquals(true,b.booleanValue());
        try{
            converter.convertIfNecessary("xcadsdsd",Boolean.class);
        }catch (TypeMismatchException e){
            return;
        }
        Assert.fail();
    }
}
