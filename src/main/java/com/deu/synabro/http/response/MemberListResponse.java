package com.deu.synabro.http.response;

import com.deu.synabro.entity.Member;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.PagedModel;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
public class MemberListResponse extends PagedModel {
    private PagedModel<Member> resources;
}
