package com.nisum.users.model.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PhoneRequest {
    private String number;
    private String citycode;
    private String contrycode;
}
