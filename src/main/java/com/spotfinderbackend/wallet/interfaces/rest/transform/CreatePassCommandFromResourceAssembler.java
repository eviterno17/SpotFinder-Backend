package com.spotfinderbackend.wallet.interfaces.rest.transform;

import com.spotfinderbackend.wallet.domain.model.commands.CreatePassCommand;
import com.spotfinderbackend.wallet.interfaces.rest.resources.CreatePassResource;

public class CreatePassCommandFromResourceAssembler {

    public static CreatePassCommand toCommand(CreatePassResource resource) {
        return new CreatePassCommand(
                resource.sessionId()
        );
    }
}