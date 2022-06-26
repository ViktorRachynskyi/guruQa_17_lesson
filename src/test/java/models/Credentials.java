package models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Credentials {

    private String email;
    private String password;
}
