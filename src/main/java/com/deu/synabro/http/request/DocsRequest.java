package com.deu.synabro.http.request;

import com.deu.synabro.http.response.VolunteerWorkResponse;
import lombok.*;

@Data
@Builder
public class DocsRequest {
    private VolunteerWorkResponse workId;
    private String fileName;
}
