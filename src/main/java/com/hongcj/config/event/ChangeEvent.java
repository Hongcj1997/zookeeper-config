package com.hongcj.config.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author: Hongcj
 * @Date: 2022/06/01 4:21 下午
 */
public class ChangeEvent extends ApplicationEvent {
    public ChangeEvent(Object source) {
        super(source);
    }
}
