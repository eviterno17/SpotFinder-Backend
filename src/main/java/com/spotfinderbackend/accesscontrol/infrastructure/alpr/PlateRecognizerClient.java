package com.spotfinderbackend.accesscontrol.infrastructure.alpr;

import com.spotfinderbackend.accesscontrol.domain.model.valueobjects.LicensePlate;
import com.spotfinderbackend.accesscontrol.domain.services.PlateRecognitionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Plate Recognizer API client. Production wiring should POST the image as
 * multipart/form-data to {@code apiUrl} with header
 * {@code Authorization: Token <apiToken>} and parse the JSON response.
 * <p>
 * Until credentials are provisioned the client returns a deterministic stub plate
 * so that the rest of the system can be exercised end-to-end.
 */
@Service
public class PlateRecognizerClient implements PlateRecognitionService {

    private static final Logger LOG = LoggerFactory.getLogger(PlateRecognizerClient.class);

    private final String apiUrl;
    private final String apiToken;

    public PlateRecognizerClient(
            @Value("${platerecognizer.api-url:https://api.platerecognizer.com/v1/plate-reader/}") String apiUrl,
            @Value("${platerecognizer.api-token:}") String apiToken
    ) {
        this.apiUrl = apiUrl;
        this.apiToken = apiToken;
    }

    @Override
    public Optional<LicensePlate> recognizePlate(byte[] imageData) {
        if (imageData == null || imageData.length == 0) return Optional.empty();
        if (apiToken == null || apiToken.isBlank()) {
            LOG.warn("Plate Recognizer token not configured; returning stub plate");
            return Optional.of(new LicensePlate("ABC-123", 0.9));
        }
        // TODO: real HTTP integration using RestTemplate / WebClient.
        // POST multipart/form-data to apiUrl with Authorization: Token <apiToken>.
        LOG.info("Plate Recognizer call placeholder (apiUrl={}); returning stub plate", apiUrl);
        return Optional.of(new LicensePlate("ABC-123", 0.92));
    }
}
