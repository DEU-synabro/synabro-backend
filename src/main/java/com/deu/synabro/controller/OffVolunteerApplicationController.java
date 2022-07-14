package com.deu.synabro.controller;

import com.deu.synabro.entity.enums.ApplyOption;
import com.deu.synabro.http.request.offVolunteer.ApplyBeneficiaryRequest;
import com.deu.synabro.http.request.offVolunteer.BeneficiaryRequest;
import com.deu.synabro.http.response.GeneralResponse;
import com.deu.synabro.service.OffVolunteerApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name="offVolunteerApplication", description = "오프라인 봉사 신청 API")
@RestController
@RequestMapping("/api/offVolunteerApplication")
@AllArgsConstructor
public class OffVolunteerApplicationController {
    @Autowired
    OffVolunteerApplicationService offVolunteerApplicationService;

    private static final String CREATE_OFF_WORKS = "{\n" +
            "    \"code\" : 200\n" +
            "    \"message\" : \"오프라인 봉사 신청이 되었습니다.\"\n" +
            "}";

    private static final String DELETE_WORKS = "{\n" +
            "    \"code\" : 204\n" +
            "    \"message\" : \"오프라인 봉사 신청이 취소되었습니다.\"\n" +
            "}";

    private static final String NOT_WORKS = "{\n" +
            "    \"code\" : 404\n" +
            "    \"message\" : \"취소할 봉사 신청이 없습니다.\"\n" +
            "}";

    @Operation(tags = "offVolunteerApplication", summary = "오프라인 봉사를 신청합니다.",
            responses={
                    @ApiResponse(responseCode = "200", description = "오프라인 봉사 모집글 생성 성공",
                            content = @Content(schema = @Schema(implementation = GeneralResponse.class),
                                    examples = @ExampleObject(value = CREATE_OFF_WORKS))),
            })
    @PostMapping("{off_volunteer_id}")
    public ResponseEntity<GeneralResponse> applyOffVolunteer(@Parameter(description = "고유 아이디") @PathVariable(name = "off_volunteer_id") UUID uuid,
                                                             @RequestParam(name="applyOption") ApplyOption applyOption,
                                                             @Parameter(name = "contentsRequest", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
                                                             @RequestPart(name="beneficiaryRequest", required = false) ApplyBeneficiaryRequest applyBeneficiaryRequest){
        try{
            String option = applyOption.getApplyOption();
            if (option=="회원"){
                offVolunteerApplicationService.applyOffVolunteer(uuid);
            }
            if (option=="수혜자"){
                offVolunteerApplicationService.applyOffVolunteerBeneficiary(applyBeneficiaryRequest);
            }

            return new ResponseEntity<>(GeneralResponse.of(HttpStatus.OK, "오프라인 봉사 신청이 되었습니다."), HttpStatus.OK);
        }catch (RuntimeException exception){
            return new ResponseEntity<>(GeneralResponse.of(HttpStatus.BAD_REQUEST, exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(tags = "offVolunteerApplication", summary = "봉사 요청글을 삭제 합니다.",
            responses={
                    @ApiResponse(responseCode = "204", description = "봉사 요청글 삭제 성공",
                            content = @Content(schema = @Schema(implementation = GeneralResponse.class),
                                    examples = @ExampleObject(value = DELETE_WORKS))),
                    @ApiResponse(responseCode = "404", description = "삭제할 봉사 요청글이 없음",
                            content = @Content(schema = @Schema(implementation = GeneralResponse.class),
                                    examples = @ExampleObject(value = NOT_WORKS)))
            })
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping("/{off_volunteer_application_id}")
    public ResponseEntity<GeneralResponse> cancelOffVolunteer(@Parameter(description = "고유 아이디")
                                                                  @PathVariable(name = "off_volunteer_application_id") UUID uuid){
        try{
            offVolunteerApplicationService.cancelOffVolunteer(uuid);
            return new ResponseEntity<>(GeneralResponse.of(HttpStatus.OK,"오프라인 봉사 신청이 취소되었습니다."), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(GeneralResponse.of(HttpStatus.NOT_FOUND,"취소할 봉사 신청이 없습니다."), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(tags = "offVolunteerApplication", summary = "봉사 요청글을 삭제 합니다.",
            responses={
                    @ApiResponse(responseCode = "204", description = "봉사 요청글 삭제 성공",
                            content = @Content(schema = @Schema(implementation = GeneralResponse.class),
                                    examples = @ExampleObject(value = DELETE_WORKS))),
                    @ApiResponse(responseCode = "404", description = "삭제할 봉사 요청글이 없음",
                            content = @Content(schema = @Schema(implementation = GeneralResponse.class),
                                    examples = @ExampleObject(value = NOT_WORKS)))
            })
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @DeleteMapping("")
    public ResponseEntity<GeneralResponse> cancelOffVolunteerBeneficiary(@Parameter @RequestBody BeneficiaryRequest beneficiaryRequest){
        try{
            offVolunteerApplicationService.cancelOffVolunteerBeneficiary(beneficiaryRequest);
            return new ResponseEntity<>(GeneralResponse.of(HttpStatus.OK,"오프라인 봉사 신청이 취소되었습니다."), HttpStatus.OK);
        }  catch (Exception e){
            return new ResponseEntity<>(GeneralResponse.of(HttpStatus.NOT_FOUND,"취소할 봉사 신청이 없습니다."), HttpStatus.NOT_FOUND);
        }
    }
}
