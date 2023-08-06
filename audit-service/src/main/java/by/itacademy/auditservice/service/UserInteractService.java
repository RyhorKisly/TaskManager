package by.itacademy.auditservice.service;

import by.itacademy.auditservice.core.dto.UserShortDTO;
import by.itacademy.auditservice.service.api.IUserInteractService;
import by.itacademy.auditservice.service.feign.UserServiceClient;
import org.springframework.stereotype.Service;

@Service
public class UserInteractService implements IUserInteractService {
    private final UserServiceClient userServiceClient;

    public UserInteractService(UserServiceClient userServiceClient) {
        this.userServiceClient = userServiceClient;
    }

    @Override
    public UserShortDTO sendAndGet(String bearerToken) {
        UserShortDTO userShortDTO = userServiceClient.send("Bearer " + bearerToken).getBody();
        return userShortDTO;
    }
}