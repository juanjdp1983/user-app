package com.nisum.users.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Data
public class UserCreatedResponse {

    private UUID id;
    private LocalDateTime created;
    private LocalDateTime modified;
    private LocalDateTime last_login;
    private String token;
    private Boolean isActive;

}
