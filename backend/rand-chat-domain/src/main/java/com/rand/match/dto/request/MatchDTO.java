package com.rand.match.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchDTO {
    @NotNull(message = "MatchDTO.거리는 Null일수 없습니다.")
    @DecimalMin(value = "0.1", inclusive = true, message = "MatchDTO.거리는 0.1 이상이여야합니다.")
    private double distance;
}
