package lk.ijse.pos.model;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SalesReturnDto {
    private String returnId;
    private String orderId;
    private String date;
    private double total;
    private List<SalesReturnDetailsDto> dto;
}
