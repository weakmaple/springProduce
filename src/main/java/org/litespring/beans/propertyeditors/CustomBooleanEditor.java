package org.litespring.beans.propertyeditors;

import org.litespring.util.StringUtils;

import java.beans.PropertyEditorSupport;

/**
 * @objective : 创建默认的可转化类型（boolean类型）
 * @date :2019/11/18- 13:16
 */
public class CustomBooleanEditor extends PropertyEditorSupport {
    private static final String VALUE_TRUE = "true";
    private static final String VALUE_FALSE = "false";

    private static final String VALUE_ON = "on";
    private static final String VALUE_OFF = "off";

    private static final String VALUE_YES = "yes";
    private static final String VALUE_NO = "no";

    private static final String VALUE_1 = "1";
    private static final String VALUE_0 = "0";

    private final boolean allowEmpty;

    public CustomBooleanEditor(boolean allowEmpty){
        this.allowEmpty = allowEmpty;
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        // 去空格
        String input = (text != null ? text.trim() : null);
        // 如果允许为空并且传送过来的数据长度为零
        // 将value值设置为null
        if (this.allowEmpty && !StringUtils.hasLength(input)) {
            // Treat empty String as null value.
            setValue(null);
        // 设置值为true的情况
        }else if((VALUE_TRUE.equalsIgnoreCase(input) || VALUE_ON.equalsIgnoreCase(input) ||
                VALUE_YES.equalsIgnoreCase(input) || VALUE_1.equalsIgnoreCase(input))){
            setValue(Boolean.TRUE);
        // 设置值为true的情况
        }else if((VALUE_FALSE.equalsIgnoreCase(input) || VALUE_OFF.equalsIgnoreCase(input) ||
                VALUE_NO.equalsIgnoreCase(input) || VALUE_0.equalsIgnoreCase(input))){
            setValue(Boolean.FALSE);
        // 其他情况报错
        }else{
            throw new IllegalArgumentException("Invalid boolean value ["+text+"]");
        }
    }

    @Override
    public String getAsText() {
        if(Boolean.TRUE.equals(getValue())){
            return VALUE_TRUE;
        }else if(Boolean.FALSE.equals(getValue())){
            return VALUE_FALSE;
        }else{
            return "";
        }
    }
}