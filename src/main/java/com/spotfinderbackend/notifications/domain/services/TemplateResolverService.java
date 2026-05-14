package com.spotfinderbackend.notifications.domain.services;

import com.spotfinderbackend.notifications.domain.model.valueobjects.NotificationType;
import com.spotfinderbackend.notifications.domain.model.valueobjects.ResolvedNotification;
import com.spotfinderbackend.notifications.infrastructure.persistence.jpa.repositories.NotificationTemplateRepository;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TemplateResolverService {

    private final NotificationTemplateRepository templateRepository;

    public TemplateResolverService(NotificationTemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    public ResolvedNotification resolveTemplate(NotificationType type, Map<String, String> data) {
        return templateRepository.findByType(type)
                .map(t -> new ResolvedNotification(t.resolveTitle(data), t.resolveBody(data)))
                .orElseGet(() -> defaultFallback(type));
    }

    private ResolvedNotification defaultFallback(NotificationType type) {
        return switch (type) {
            case ENTRY_CONFIRMED -> new ResolvedNotification("Ingreso confirmado", "Tu vehículo ingresó al estacionamiento.");
            case PAYMENT_REMINDER -> new ResolvedNotification("Pago pendiente", "Debes pagar antes de salir.");
            case PAYMENT_SUCCESS -> new ResolvedNotification("Pago confirmado", "Tu pago fue procesado.");
            case PAYMENT_FAILED -> new ResolvedNotification("Pago fallido", "Tu pago no pudo procesarse.");
            case EMERGENCY_ALERT -> new ResolvedNotification("Alerta de emergencia", "Evacúe inmediatamente.");
            case SESSION_END -> new ResolvedNotification("Salida confirmada", "Tu vehículo salió del estacionamiento.");
        };
    }
}
