package com.zerotrust.server.events;

import com.zerotrust.server.model.Connection;
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
