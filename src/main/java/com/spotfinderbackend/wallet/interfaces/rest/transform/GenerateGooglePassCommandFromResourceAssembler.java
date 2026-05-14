package com.spotfinderbackend.wallet.interfaces.rest.transform;

import com.spotfinderbackend.wallet.domain.model.commands.GenerateGooglePassCommand;
import com.spotfinderbackend.wallet.interfaces.rest.resources.GenerateGooglePassResource;

public class GenerateGooglePassCommandFromResourceAssembler {

    public static GenerateGooglePassCommand toCommand(GenerateGooglePassResource resource) {
        return new GenerateGooglePassCommand(
                resource.passId()
        );
    }
}