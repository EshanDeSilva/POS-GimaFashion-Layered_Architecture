package lk.ijse.pos.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class EmployerDto {
    private String id;
    private String title;
    private String name;
    private String nic;
    private String dob;
    private String address;
    private String bankAcc;
    private String bankBranch;
    private String contact;
}
