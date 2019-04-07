package com.zerotrust.rest.events;

import com.zerotrust.rest.model.Connection;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class NewCloseConnection extends ApplicationEvent {
    private final Connection connection;

    public NewCloseConnection(Object source, Connection connection) {
        super(source);
        this.connection = connection;
    }
}
