package com.example.anywrpfe.auth.response;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Data
@Builder
public class HttpResponse {

    protected String timestamp;
    protected int statusCode;

    protected HttpStatus status;

    protected String message;

    protected Map<?,?> data;
}
