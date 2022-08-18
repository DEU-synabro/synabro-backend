package com.deu.synabro.http.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OffVolunteerApplicationResponse {
    private String name;
    private String phone;
    private String teamGroup;
    private String address;
}
