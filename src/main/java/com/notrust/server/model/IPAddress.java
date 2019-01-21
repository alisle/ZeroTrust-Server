package com.notrust.server.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ip_address")
public class IPAddress {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "address", nullable = false)
    private String address;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "version", nullable = false)
    private Version version;

    public enum Version {
        V4,
        V6,
        Invalid;

        static public Version fromString(String version) {
            switch(version.toLowerCase()) {
                case "v6":
                    return V6;
                case "v4":
                    return V4;
               default:
                   return Invalid;

            }
        }
    }


}
