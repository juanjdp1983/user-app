package com.nisum.users.model.request;

import com.nisum.users.model.request.PhoneRequest;
import lombok.*;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class SignupRequest {
    private String name;
    private String email;
    private String password;
    private List<PhoneRequest> phones;
}
