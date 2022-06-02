package com.hongcj.config.common;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * @author: Hongcj
 * @Date: 2022/05/31 9:57 上午
 * @Description：自定义加载器接口
 *                  其他自定义加载器只需要实现该接口的locate方法，并声明在spring.factories里
 */
public interface CustomerLocator {

    PropertySource<?> locate(Environment environment, ConfigurableApplicationContext configurableApplicationContext) throws Exception;

    default Collection<PropertySource> locationList(Environment environment, ConfigurableApplicationContext configurableApplicationContext){
        try {
            return locationLists(this, environment, configurableApplicationContext);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    static Collection<PropertySource> locationLists(CustomerLocator customeLocator, Environment environment, ConfigurableApplicationContext configurableApplicationContext) throws Exception {
        PropertySource<?> propertySource = customeLocator.locate(environment, configurableApplicationContext);
        if(propertySource==null){
            return Collections.EMPTY_LIST;
        }
        return Arrays.asList(propertySource);
    }
}
