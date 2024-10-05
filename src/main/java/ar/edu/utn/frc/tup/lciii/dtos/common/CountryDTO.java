package ar.edu.utn.frc.tup.lciii.dtos.common;

import lombok.*;

import java.util.List;
import java.util.Map;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
public class CountryDTO {
    private String name;
    private String code;
}
