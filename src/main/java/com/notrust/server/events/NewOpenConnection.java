package com.notrust.server.events;

import com.notrust.server.model.Connection;
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
