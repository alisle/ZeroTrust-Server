package com.zerotrust.links.model;

import lombok.Data;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Data
@Entity
@Table(name = "ip_address")
public class IPAddress {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "address", nullable = false)
    private int address;

    @Formula("address")
    private int addressInt;

    @Column(name = "address_string", nullable = false)
    private String addressString;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IPAddress address1 = (IPAddress) o;
        return address == address1.address &&
                version == address1.version;
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, version);
    }

    public IPAddress() {
    }

    public IPAddress(int address, String addressString, @NotNull Version version) {
        this.address = address;
        this.addressString = addressString;
        this.version = version;
    }
}
