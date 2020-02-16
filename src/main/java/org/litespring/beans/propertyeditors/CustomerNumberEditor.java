package org.litespring.beans.propertyeditors;

import org.litespring.util.NumberUtils;
import org.litespring.util.StringUtils;

import java.beans.PropertyEditorSupport;
import java.text.NumberFormat;

/**
 * @objective : 完成数据的格式转换（int类型）
 * @date :2019/11/18- 12:46
 */
public class CustomerNumberEditor extends PropertyEditorSupport {
    // 要转化的数据类型
    private final Class<? extends Number> numberClass;
    // 转化的数据格式
    private final NumberFormat numberFormat;
    // 是否允许为空
    private final boolean allowEmpty;

    public CustomerNumberEditor(Class<? extends Number> numberClass, boolean allowEmpty) {
        this(numberClass,null,allowEmpty);
    }

    public CustomerNumberEditor(Class<? extends Number> numberClass, NumberFormat numberFormat, boolean allowEmpty)
    throws IllegalArgumentException{
        // 判断要转化的类型是否是数字类型
        if (numberClass == null || !Number.class.isAssignableFrom(numberClass)){
            throw new IllegalArgumentException("Property class must be a subclass of Number");
        }
        this.numberClass = numberClass;
        this.numberFormat = numberFormat;
        this.allowEmpty = allowEmpty;
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        // 如果允许为空并且传送过来的数据长度为零
        // 将value值设置为null
        if (this.allowEmpty && !StringUtils.hasText(text)) {
            // Treat empty String as null value.
            setValue(null);
        }
        // 有格式要求，按照格式转换
        else if (this.numberFormat != null) {
            // Use given NumberFormat for parsing text.
            setValue(NumberUtils.parseNumber(text, this.numberClass, this.numberFormat));
        }
        // 进行类型转换
        else {
            // Use default valueOf methods for parsing text.
            setValue(NumberUtils.parseNumber(text, this.numberClass));
        }
    }

    @Override
    public void setValue(Object value) {
        if(value instanceof Number){
            super.setValue(NumberUtils.convertNumberToTargetClass((Number) value,this.numberClass));
        }else{
            super.setValue(value);
        }
    }

    @Override
    public String getAsText() {
        Object value = getValue();
        if (value == null){
            return "";
        }
        if(this.numberFormat != null){
            return this.numberFormat.format(value);
        }else{
            return value.toString();
        }
    }
}
