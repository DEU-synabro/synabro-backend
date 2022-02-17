package com.deu.synabro.http.request;

import com.deu.synabro.entity.Docs;
import com.deu.synabro.http.response.VolunteerResponse;
import lombok.*;

@Data
@Builder
public class DocsRequest {
    private VolunteerResponse workId;
    private String fileName;
}
