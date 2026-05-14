package com.spotfinderbackend.iam.application.acl;

import com.spotfinderbackend.iam.domain.model.queries.GetUserByEmailQuery;
import com.spotfinderbackend.iam.domain.services.UserQueryService;
import com.spotfinderbackend.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import com.spotfinderbackend.iam.interfaces.acl.IamContextFacade;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IamContextFacadeImpl implements IamContextFacade {

    private final UserRepository userRepository;
    private final UserQueryService userQueryService;

    public IamContextFacadeImpl(UserRepository userRepository, UserQueryService userQueryService) {
        this.userRepository = userRepository;
        this.userQueryService = userQueryService;
    }

    @Override
    public Optional<Long> findUserIdByEmail(String email) {
        if (email == null || email.isBlank()) return Optional.empty();
        return userQueryService.handle(new GetUserByEmailQuery(email))
                .map(u -> u.getId());
    }

    @Override
    public Optional<Long> findActiveUserId(Long userId) {
        if (userId == null) return Optional.empty();
        return userRepository.findById(userId)
                .filter(u -> Boolean.TRUE.equals(u.getActive()))
                .map(u -> u.getId());
    }

    @Override
    public Optional<String> findEmailByUserId(Long userId) {
        if (userId == null) return Optional.empty();
        return userRepository.findById(userId).map(u -> u.getEmail());
    }

    @Override
    public List<String> findRolesByUserId(Long userId) {
        if (userId == null) return List.of();
        return userRepository.findById(userId)
                .map(u -> u.getRoles().stream()
                        .map(r -> r.getName().name())
                        .toList())
                .orElse(List.of());
    }

    @Override
    public Optional<String> findFcmTokenByUserId(Long userId) {
        // FCM tokens are not yet stored in the User aggregate.
        // This will be wired up once the FCM token endpoint is added to IAM.
        return Optional.empty();
    }
}
