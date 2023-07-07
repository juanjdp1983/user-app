package com.nisum.users.model.request;

import com.nisum.users.model.request.validator.CombinedConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
@CombinedConstraint(fields = {"email","password"})
public class SignupRequest {
    private String name;

    private String email;

    private String password;

    private List<PhoneRequest> phones;
}
