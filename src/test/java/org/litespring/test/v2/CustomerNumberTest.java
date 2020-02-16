package org.litespring.test.v2;

import org.junit.Assert;
import org.junit.Test;
import org.litespring.beans.propertyeditors.CustomerNumberEditor;

/**
 * @objective : 4
 * @date :2019/11/18- 7:59
 */
public class CustomerNumberTest {

    // 测试数据的格式转换（string转int）
    @Test
    public void testConverString(){
        CustomerNumberEditor editor = new CustomerNumberEditor(Integer.class,true);
        editor.setAsText("3");
        Object value = editor.getValue();
        Assert.assertTrue(value instanceof Integer);
        Assert.assertEquals(3,((Integer)editor.getValue()).intValue());

        editor.setAsText("");
        Assert.assertTrue(editor.getValue() == null);

        try{
            editor.setAsText("3.1");
        }catch (IllegalArgumentException e){
            return;
        }
        Assert.fail();
    }
}
