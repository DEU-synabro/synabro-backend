package com.deu.synabro.http.response.member;

public class ResponseExample {

    protected static final String SET_MEMBER_OK = "{\n" +
            "    \"code\" : 200\n" +
            "    \"message\" : \"사용자 등록에 성공하였습니다.\"\n" +
            "}";

    protected static final String SET_MEMBER_BAD_REQUEST = "{\n" +
            "    \"code\" : 400\n" +
            "    \"message\" : \"잘못된 접근입니다. email, password, username을 확인해주세요.\"\n" +
            "}";

    protected static final String SET_UPDATE_OK = "{\n" +
            "    \"code\" : 200\n" +
            "    \"message\" : \"사용자 정보 수정에 성공하였습니다.\"\n" +
            "}";

    protected static final String SET_BAD_REQUEST = "{\n" +
            "    \"code\" : 400\n" +
            "    \"message\" : \"잘못된 접근입니다. Request Body를 확인해주세요.\"\n" +
            "}";

}
