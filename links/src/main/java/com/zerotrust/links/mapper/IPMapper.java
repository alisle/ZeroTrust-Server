package com.zerotrust.links.mapper;

import com.google.common.net.InetAddresses;
import com.zerotrust.links.exception.InvalidIPAddress;
import com.zerotrust.model.entity.IPAddress;
import org.springframework.stereotype.Service;

import java.net.InetAddress;

@Service
public class IPMapper {
    public IPAddress convertString(String string) throws InvalidIPAddress {
        if(!InetAddresses.isInetAddress(string)) {
            throw new InvalidIPAddress();
        }

        InetAddress address = InetAddresses.forString(string);
        int bytes = InetAddresses.coerceToInteger(address);
        return new IPAddress(bytes, string, IPAddress.Version.V4);
    }

    public IPAddress[] convertStrings(String[] strings) throws InvalidIPAddress {
        if(strings == null) {
            return new IPAddress[] {};
        }

        IPAddress[] addresses = new IPAddress[strings.length];
        for(int x = 0; x < strings.length; x++) {
            addresses[x] = convertString(strings[x]);
        }

        return addresses;
    }
}
