package com.hieunguyen.identityservice.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IntrospectResponse {

    boolean valid;
}
