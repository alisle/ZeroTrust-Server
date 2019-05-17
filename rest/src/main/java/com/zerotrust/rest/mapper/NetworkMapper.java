package com.zerotrust.rest.mapper;

import com.google.common.net.InetAddresses;
import com.zerotrust.model.entity.Network;
import com.zerotrust.rest.dto.NetworkDTO;
import com.zerotrust.rest.exception.InvalidNetworkException;
import com.zerotrust.rest.exception.InvalidNetworkMaskException;
import org.springframework.stereotype.Service;

@Service
public class NetworkMapper {

    public Network convert(NetworkDTO dto) throws InvalidNetworkException, InvalidNetworkMaskException {
        if(dto.getName() == null ||
           dto.getNetwork() == null ||
           dto.getMask() == null) {
            throw new InvalidNetworkException();
        }

        if(!InetAddresses.isInetAddress(dto.getNetwork()) || !InetAddresses.isInetAddress(dto.getMask())) {
            throw new InvalidNetworkMaskException();
        }

        String name = dto.getName();
        String description = dto.getDescription();
        String addressString = dto.getNetwork();
        String maskString = dto.getMask();

        int address = InetAddresses.coerceToInteger(InetAddresses.forString(addressString));
        int mask = InetAddresses.coerceToInteger(InetAddresses.forString(maskString));

        Network network = new Network();
        network.setName(name);
        network.setDescription(description);
        network.setAddress(address);
        network.setAddressString(addressString);
        network.setMask(mask);
        network.setMaskString(maskString);

        return network;
    }
}
