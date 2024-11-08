package com.farmin.farminserver.common.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.farmin.farminserver.common.error.ErrorCode;
import com.farmin.farminserver.common.error.ErrorCodeIfs;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private Integer resultCode;
    private String resultMessage;
    private String resultDescription;
    // 성공
    public static Result OK(){
        return Result.builder()
                .resultCode(ErrorCode.OK.getErrorCode())
                .resultMessage(ErrorCode.OK.getDescription())
                .resultDescription("SUCCESS")
                .build();
    }

    public static Result CREATE(){
        return Result.builder()
                .resultCode(ErrorCode.CREATE.getErrorCode())
                .resultMessage(ErrorCode.CREATE.getDescription())
                .resultDescription("CREATE")
                .build();
    }
    //에러 응답
    public static Result ERROR(ErrorCodeIfs errorCodeIfs){
        return Result.builder()
                .resultCode(errorCodeIfs.getErrorCode())
                .resultMessage(errorCodeIfs.getDescription())
                .resultDescription("ERROR")
                .build();
    }
    //에러 및 설명 응답
    public static Result ERROR(ErrorCodeIfs errorCodeIfs,String description){
        return Result.builder()
                .resultCode(errorCodeIfs.getErrorCode())
                .resultMessage(errorCodeIfs.getDescription())
                .resultDescription(description)
                .build();
    }
}