package com.nisum.users.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nisum.users.model.request.LoginRequest;
import com.nisum.users.model.request.PhoneRequest;
import com.nisum.users.model.request.SignupRequest;
import com.nisum.users.model.response.UserCreatedResponse;
import com.nisum.users.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.naming.ServiceUnavailableException;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private UserServiceImpl userService;

    private AuthController authController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        authController = new AuthController(userService);
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(new ServiceUnavailableException())
                .build();
    }

    /**
     * product list test OK
     *
     * @throws Exception
     */
    @Test
    public void registerUserOk() throws Exception {
        Mockito.when(userService.creteUser(buildSignupRequest())).thenReturn(buildUserCreatedResponse());

        mockMvc.perform( MockMvcRequestBuilders
                .post("/api/auth/signup")
                .content(asJsonString(buildSignupRequest()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }

    @Test
    public void signinUserOk() throws Exception {
        Mockito.when(userService.authenticateUser(buildLoginRequest())).thenReturn(buildUserCreatedResponse());

        mockMvc.perform( MockMvcRequestBuilders
                .post("/api/auth/signin")
                .content(asJsonString(buildSignupRequest()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private SignupRequest buildSignupRequest(){
        List<PhoneRequest> list = new ArrayList<>();
        list.add(new PhoneRequest("099234123","1","57"));
        return new SignupRequest("name","mail@dominio.com","Hyb4@198", list);
    }

    private LoginRequest buildLoginRequest(){
        return new LoginRequest("name","Hyb4@198");
    }
    private UserCreatedResponse buildUserCreatedResponse(){
        return new UserCreatedResponse(UUID.fromString("b309ae32-e366-4a95-a5e4-893a47a3be13"),
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqdWFuY2hvdjIiLCJpYXQiOjE2ODcxODQ2MjUsImV4cCI6MTY4NzI3MTAyNX0.fuGCRqhMc6TMN4g3laJ5vz_mfVDmaY-aJ-Uv2Wc5-yM",
                true);
    }
}
