package lk.ijse.pos.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserDto {
    private String username;
    private String password;
    private String userEmail;
    private String userType = "Default";
}
