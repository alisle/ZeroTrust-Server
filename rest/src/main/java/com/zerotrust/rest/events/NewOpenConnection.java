package com.zerotrust.rest.events;

import com.zerotrust.rest.model.Connection;
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