package com.hongcj.config.common;

import org.springframework.core.env.Environment;
import org.springframework.util.PropertyPlaceholderHelper;

import java.lang.reflect.Field;

/**
 * @author: Hongcj
 * @Date: 2022/06/01 4:25 下午
 * @Description：用来实现属性值的替换
 */
public class FieldPair {

    private PropertyPlaceholderHelper propertyPlaceholderHelper=
            new PropertyPlaceholderHelper("${","}",":",true);

    private Object bean;
    private Field field;
    private String value;

    public FieldPair(Object bean, Field field, String value) {
        this.bean = bean;
        this.field = field;
        this.value = value;
    }

    public void resetValue(Environment environment){
        boolean access=field.isAccessible();
        if(!access){
            field.setAccessible(true);
        }
        String resetValue=propertyPlaceholderHelper.replacePlaceholders(value,environment::getProperty);
        try {
            //反射修改bean的属性值
            field.set(bean,resetValue);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
