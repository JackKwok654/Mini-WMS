package com.jack.location.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BaseResponse {
    private String status;

    private String message;
}
