package com.hongcj.config.bean;

import com.hongcj.config.event.ChangeEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author: Hongcj
 * @Date: 2022/06/01 4:30 下午
 * @Description：事件监听器
 */
@Component
public class ConfigurationPropertiesRebinder implements ApplicationListener<ChangeEvent> {

    private ConfigurationPropertiesBeans beans;

    private Environment environment;

    public ConfigurationPropertiesRebinder(ConfigurationPropertiesBeans beans, Environment environment) {
        this.beans=beans;
        this.environment=environment;
    }

    @Override
    public void onApplicationEvent(ChangeEvent event) {
        System.out.println("收到environment变更事件");
        rebind();
    }
    public void rebind(){
        this.beans.getFieldMapper().forEach((k,v)->{
            v.forEach(f->f.resetValue(environment));
        });
    }
}
