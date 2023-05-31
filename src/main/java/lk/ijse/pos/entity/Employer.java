package lk.ijse.pos.entity;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Employer {
    private String id;
    private String title;
    private String name;
    private String nic;
    private String dob;
    private String address;
    private String bankAccountNo;
    private String bankBranch;
    private String contactNo;
}
