package com.spotfinderbackend.accesscontrol.interfaces.rest.transform;

import com.spotfinderbackend.accesscontrol.domain.model.commands.RecognizePlateCommand;
import com.spotfinderbackend.accesscontrol.domain.model.commands.RegisterBarrierCommand;
import com.spotfinderbackend.accesscontrol.domain.model.commands.RegisterEntryCommand;
import com.spotfinderbackend.accesscontrol.domain.model.commands.RegisterExitCommand;
import com.spotfinderbackend.accesscontrol.domain.model.valueobjects.BarrierPosition;
import com.spotfinderbackend.accesscontrol.interfaces.rest.resources.EntryRequestResource;
import com.spotfinderbackend.accesscontrol.interfaces.rest.resources.ExitRequestResource;
import com.spotfinderbackend.accesscontrol.interfaces.rest.resources.PlateRecognitionResource;
import com.spotfinderbackend.accesscontrol.interfaces.rest.resources.RegisterBarrierResource;

import java.util.Base64;

public class AccessCommandFromResourceAssembler {

    public static RegisterEntryCommand toCommand(EntryRequestResource r) {
        return new RegisterEntryCommand(decode(r.imageData()), r.barrierCode());
    }

    public static RegisterExitCommand toCommand(ExitRequestResource r) {
        return new RegisterExitCommand(decode(r.imageData()), r.barrierCode());
    }

    public static RecognizePlateCommand toCommand(PlateRecognitionResource r) {
        return new RecognizePlateCommand(decode(r.imageData()), r.cameraPosition());
    }

    public static RegisterBarrierCommand toCommand(RegisterBarrierResource r) {
        return new RegisterBarrierCommand(r.barrierCode(), BarrierPosition.valueOf(r.position().toUpperCase()), r.facilityId());
    }

    private static byte[] decode(String base64) {
        if (base64 == null || base64.isBlank()) return new byte[]{0};
        try { return Base64.getDecoder().decode(base64); }
        catch (IllegalArgumentException e) { return base64.getBytes(); }
    }
}
