package by.itacademy.userservice.endponts.web;

import by.itacademy.sharedresource.core.dto.CoordinatesDTO;
import by.itacademy.sharedresource.core.dto.PageDTO;
import by.itacademy.userservice.core.dto.*;
import by.itacademy.userservice.dao.entity.UserEntity;
import by.itacademy.userservice.service.api.IUserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@Validated
@RestController
@RequestMapping("/users")
public class UserController {
    private final IUserService userService;
    private final ConversionService conversionService;
    public UserController(
            IUserService userService,
            ConversionService conversionService
    ) {
        this.userService = userService;
        this.conversionService = conversionService;
    }

    @PostMapping
    public ResponseEntity<?> save(
            @RequestBody @Valid UserCreateDTO userCreateDTO
    ) {
        userService.createByUser(userCreateDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getPages(
            @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer page,
            @RequestParam(required = false, defaultValue = "20") @PositiveOrZero Integer size
    ) {
        Page<UserEntity> pageOfUsers =  userService.get(PageRequest.of(page, size));
        return new ResponseEntity<>(
                conversionService.convert(pageOfUsers, PageDTO.class),
                HttpStatus.OK
        );
    }
 
      @GetMapping("/{uuid}")
    public ResponseEntity<UserDTO> getCard(
            @PathVariable UUID uuid
    ) {
        UserDTO userDTO = conversionService.convert(userService.get(uuid), UserDTO.class);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PutMapping("/{uuid}/dt_update/{dt_update}")
    public ResponseEntity<?> update(
            @PathVariable UUID uuid,
            @PathVariable("dt_update") LocalDateTime dtUpdate,
            @RequestBody @Valid UserCreateDTO userCreateDTO
    ) {
        CoordinatesDTO coordinatesDTO = new CoordinatesDTO();
        coordinatesDTO.setUuid(uuid);
        coordinatesDTO.setDtUpdate(dtUpdate);
        userService.update(userCreateDTO, coordinatesDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/validation")
    public ResponseEntity<?> validate(
            @RequestBody List<UUID> uuids
            ) {
        userService.validate(uuids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
