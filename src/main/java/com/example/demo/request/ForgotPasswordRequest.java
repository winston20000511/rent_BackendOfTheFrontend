package com.example.demo.request;

import lombok.Data;
import lombok.NonNull;
@Data
public class ForgotPasswordRequest {
    @NonNull
    private String email;


}
