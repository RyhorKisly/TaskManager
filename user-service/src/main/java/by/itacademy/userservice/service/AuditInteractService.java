package by.itacademy.userservice.service;

import by.itacademy.sharedresource.core.dto.AuditCreateDTO;
import by.itacademy.sharedresource.core.dto.UserShortDTO;
import by.itacademy.sharedresource.core.enums.EssenceType;
import by.itacademy.userservice.config.properites.JWTProperty;
import by.itacademy.userservice.core.dto.UserDTO;
import by.itacademy.userservice.dao.entity.UserEntity;
import by.itacademy.userservice.endponts.utils.JwtTokenHandler;
import by.itacademy.userservice.service.api.IAuditInteractService;
import by.itacademy.userservice.service.api.IUserHolder;
import by.itacademy.userservice.service.feign.AuditServiceClient;
import org.springframework.stereotype.Service;

@Service
public class AuditInteractService implements IAuditInteractService {
    private final AuditServiceClient auditServiceClient;
    private final JwtTokenHandler jwtHandler;
    private final JWTProperty property;
    private final IUserHolder holder;
    public AuditInteractService(
            AuditServiceClient auditServiceClient,
            JwtTokenHandler jwtHandler,
            JWTProperty property,
            IUserHolder holder
    ) {
        this.auditServiceClient = auditServiceClient;
        this.jwtHandler = jwtHandler;
        this.property = property;
        this.holder = holder;
    }

    @Override
    public void send(UserEntity newEntity, String text) {
        UserShortDTO userShortDTO = fillUserShortDTO(holder.getUser());
        AuditCreateDTO auditCreateDTO = fillUserSendDTO(userShortDTO, newEntity, text);
        String bearerToken = "Bearer " + jwtHandler.generateSystemAccessToken(property.getSystem());
        auditServiceClient.send(bearerToken, auditCreateDTO);
    }

    @Override
    public void send(UserEntity entity, UserShortDTO dto, String text) {
        AuditCreateDTO auditCreateDTO = fillUserSendDTO(dto, entity, text);
        String bearerToken = "Bearer " + jwtHandler.generateSystemAccessToken(property.getSystem());
        auditServiceClient.send(bearerToken, auditCreateDTO);
    }

    private AuditCreateDTO fillUserSendDTO(UserShortDTO userShortDTO, UserEntity newEntity, String text) {
        AuditCreateDTO auditCreateDTO = new AuditCreateDTO();

        //user, который произвёл операцию
        auditCreateDTO.setUserShortDTO(userShortDTO);

        //EssenceType для аудита
        auditCreateDTO.setType(EssenceType.USER);
        auditCreateDTO.setText(text);
        //id кого создали
        auditCreateDTO.setId(newEntity.getUuid().toString());

        return auditCreateDTO;
    }

    private UserShortDTO fillUserShortDTO(UserDTO userDTO) {
        UserShortDTO userShortDTO = new UserShortDTO();
        userShortDTO.setUuid(userDTO.getUuid());
        userShortDTO.setMail(userDTO.getMail());
        userShortDTO.setFio(userDTO.getFio());
        userShortDTO.setRole(userDTO.getRole());
        return userShortDTO;
    }
}
