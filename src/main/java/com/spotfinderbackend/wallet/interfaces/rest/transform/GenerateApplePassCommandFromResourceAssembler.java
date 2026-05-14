package com.spotfinderbackend.wallet.interfaces.rest.transform;

import com.spotfinderbackend.wallet.domain.model.commands.GenerateApplePassCommand;
import com.spotfinderbackend.wallet.interfaces.rest.resources.GenerateApplePassResource;

public class GenerateApplePassCommandFromResourceAssembler {

    public static GenerateApplePassCommand toCommand(GenerateApplePassResource resource) {
        return new GenerateApplePassCommand(
                resource.passId()
        );
    }
}