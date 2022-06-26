package models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseWithLombok {

    private String token;
}