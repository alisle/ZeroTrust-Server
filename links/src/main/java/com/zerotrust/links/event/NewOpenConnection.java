package com.zerotrust.links.event;

import com.zerotrust.model.entity.Connection;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class NewOpenConnection extends ApplicationEvent {
    private final Connection connection;

    public NewOpenConnection(Object source, Connection connection) {
        super(source);
        this.connection = connection;
    }

}
