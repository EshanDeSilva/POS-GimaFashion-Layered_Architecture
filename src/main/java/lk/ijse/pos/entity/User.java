package lk.ijse.pos.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class User {
    private String username;
    private String password;
    private String userEmail;
    private String userType = "Default";
}
