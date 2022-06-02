package com.hongcj.config.change;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hongcj.config.event.ChangeEvent;
import lombok.SneakyThrows;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.CuratorCacheListenerBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;

import java.util.Map;

/**
 * @author: Hongcj
 * @Date: 2022/06/01 3:26 下午
 */
public class NodeChangeListener implements CuratorCacheListenerBuilder.ChangeListener {

    private Environment environment;

    private ConfigurableApplicationContext configurableApplicationContext;

    public NodeChangeListener(Environment environment, ConfigurableApplicationContext configurableApplicationContext) {
        this.environment = environment;
        this.configurableApplicationContext = configurableApplicationContext;
    }

    @SneakyThrows
    @Override
    public void event(ChildData oldNode, ChildData node) {
        System.out.println("收到数据变更事件");
        String data = new String(node.getData());

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String,Object> map = objectMapper.readValue(data, Map.class);
        //替换掉原来的PropertySource
        MapPropertySource configService = new MapPropertySource("configService", map);
        ConfigurableEnvironment cfe = (ConfigurableEnvironment)environment;
        cfe.getPropertySources().replace("configService",configService);
        // 发送事件 反射修改旧值
        configurableApplicationContext.publishEvent(new ChangeEvent(this));
        System.out.println("修改完成");
    }
}
