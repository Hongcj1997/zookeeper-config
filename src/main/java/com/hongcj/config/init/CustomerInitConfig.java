package com.hongcj.config.init;

import com.hongcj.config.common.CustomerLocator;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;

/**
 * @author: Hongcj
 * @Date: 2022/05/31 9:56 上午
 */
public class CustomerInitConfig implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    /**
     * 所有CustomerLocator的扩展类
     */
    private final List<CustomerLocator> customeLocators;

    public CustomerInitConfig() {
        ClassLoader defaultClassLoader = ClassUtils.getDefaultClassLoader();
        customeLocators = SpringFactoriesLoader.loadFactories(CustomerLocator.class, defaultClassLoader);
    }

    @SneakyThrows
    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        ConfigurableEnvironment environment = configurableApplicationContext.getEnvironment();
        MutablePropertySources propertySources = environment.getPropertySources();
        for (CustomerLocator customerLocator : customeLocators) {
            Collection<PropertySource> propertySourceCollection = customerLocator.locationList(environment, configurableApplicationContext);
            if(CollectionUtils.isEmpty(propertySourceCollection)){
                continue;
            }
            for (PropertySource propertySource : propertySourceCollection) {
                propertySources.addLast(propertySource);
            }
        }


    }
}
