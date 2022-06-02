package com.hongcj.config.bean;

import com.hongcj.config.common.FieldPair;
import com.hongcj.config.common.RefreshScope;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Hongcj
 * @Date: 2022/06/01 4:25 下午
 * @Description：初始bean时，扫描出所有需要动态修改值的属性，并保存为FieldPair类
 */
@Component
public class ConfigurationPropertiesBeans implements BeanPostProcessor {

    // 保存的是需要动态修改值的参数

    private Map<String, List<FieldPair>> fieldMapper=new HashMap<>();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
//         bean初始化之前执行
//         将需要动态修改值的属性保存起来
        Class beanClass = bean.getClass();
        if(beanClass.isAnnotationPresent(RefreshScope.class)){
            Field[] fields = beanClass.getDeclaredFields();
            for (Field field : fields) {
                Value value = field.getAnnotation(Value.class);
                if(value==null){
                    continue;
                }
                List<String> propertyKey = getPropertyKey(value.value(), 0);
                for (String key : propertyKey) {
                    fieldMapper.computeIfAbsent(key,(k)->new ArrayList<>())
                            .add(new FieldPair(bean,field, value.value()));
                }
            }
        }

        return bean;
    }

    private List<String> getPropertyKey(String value,int begin){
        int start=value.indexOf("${",begin)+2;
        if(start<2){
            return new ArrayList<>();
        }
        int middle=value.indexOf(":",start);
        int end=value.indexOf("}",start);
        String key;
        if(middle>0&&middle<end){
            key=value.substring(start,middle);
        }else{
            key=value.substring(start,end);
        }
        List<String> keys=getPropertyKey(value,end);
        keys.add(key);
        return keys;
    }

    public Map<String, List<FieldPair>> getFieldMapper() {
        return fieldMapper;
    }
}
