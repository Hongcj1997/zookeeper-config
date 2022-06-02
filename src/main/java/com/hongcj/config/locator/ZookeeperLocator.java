package com.hongcj.config.locator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hongcj.config.common.CustomerLocator;
import com.hongcj.config.change.NodeChangeListener;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;

import java.util.Map;

/**
 * @author: Hongcj
 * @Date: 2022/05/31 9:59 上午
 */
public class ZookeeperLocator implements CustomerLocator {

    private final CuratorFramework curatorFramework;

    private final String node = "/data";

    public ZookeeperLocator() {
        curatorFramework = CuratorFrameworkFactory.builder()
                .connectString("10.211.55.3:2181")
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .sessionTimeoutMs(60000)
                .connectionTimeoutMs(20000)
                .namespace("config")
                .build();
        curatorFramework.start();
    }

    @Override
    public PropertySource<?> locate(Environment environment, ConfigurableApplicationContext configurableApplicationContext) throws Exception {
        System.out.println("开始加载配置文件");
        byte[] bytes = curatorFramework.getData().forPath(node);
        String data = new String(bytes);

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String,Object> map = objectMapper.readValue(data, Map.class);


        CompositePropertySource composite = new CompositePropertySource("configService");
        MapPropertySource propertySource = new MapPropertySource("configService",map);

        composite.addPropertySource(propertySource);

        // 增加监听器
        addListener(environment,configurableApplicationContext);
        return composite;
    }

    private void addListener(Environment environment, ConfigurableApplicationContext configurableApplicationContext){
        NodeChangeListener changeListener = new NodeChangeListener(environment, configurableApplicationContext);
        CuratorCache curatorCache = CuratorCache.build(curatorFramework,node,CuratorCache.Options.SINGLE_NODE_CACHE);

        CuratorCacheListener listener = CuratorCacheListener.builder()
                .forChanges(changeListener).build();

        curatorCache.listenable().addListener(listener);
        curatorCache.start();
    }
}
