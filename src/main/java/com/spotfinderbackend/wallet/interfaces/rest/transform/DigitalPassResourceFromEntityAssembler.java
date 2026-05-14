package com.spotfinderbackend.wallet.interfaces.rest.transform;

import com.spotfinderbackend.wallet.domain.model.aggregates.DigitalPass;
import com.spotfinderbackend.wallet.interfaces.rest.resources.DigitalPassResource;

public class DigitalPassResourceFromEntityAssembler {

    public static DigitalPassResource toResource(DigitalPass pass) {
        return new DigitalPassResource(
                pass.getId(),
                pass.getSessionId(),
                pass.getQrCode(),
                pass.getApplePayload(),
                pass.getGooglePayload(),
                pass.getCreatedAt()
        );
    }
}