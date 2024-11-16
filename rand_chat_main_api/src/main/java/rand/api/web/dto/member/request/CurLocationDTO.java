package rand.api.web.dto.member.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CurLocationDTO {
    @NotNull(message = "CurLocationDTO.위도는 Null일 수 없음")
    @DecimalMin(value = "-90.0", inclusive = true, message = "CurLocationDTO.위도는 -90.0 ~90.0")
    @DecimalMax(value = "90.0", inclusive = true, message = "CurLocationDTO.위도는 -90.0 ~90.0")
    private double localeLat;

    @NotNull(message = "CurLocationDTO.경도는 Null일 수 없음")
    @DecimalMin(value = "-180.0", inclusive = true, message = "CurLocationDTO.경도는 -180.0 ~180.0")
    @DecimalMax(value = "180.0", inclusive = true, message = "CurLocationDTO.경도는 -180.0 ~180.0")
    private double localeLon;

}
