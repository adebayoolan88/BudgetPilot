package dev.adebayoolan.budgetpilot.security;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import dev.adebayoolan.budgetpilot.model.User;
import dev.adebayoolan.budgetpilot.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticatedUserService {

    private final UserRepository userRepository;

    public User requireCurrentUser(Jwt jwt) {
        return userRepository.findByClerkId(jwt.getSubject())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No account found for this session yet"));
    }

    public void requireOwnership(Jwt jwt, UUID ownerId) {
        User current = requireCurrentUser(jwt);
        if (!current.getId().equals(ownerId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not authorized for this resource");
        }
    }
}
