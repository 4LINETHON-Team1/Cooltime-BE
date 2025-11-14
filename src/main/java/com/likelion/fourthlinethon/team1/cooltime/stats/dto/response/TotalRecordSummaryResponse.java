package com.likelion.fourthlinethon.team1.cooltime.stats.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Getter
@Builder
@Schema(title = "TotalRecordSummaryResponse", description = "미룸 기록 총 일수 요약 응답")
public class TotalRecordSummaryResponse {

    /** 가입일 포함 오늘까지의 총 일수 */
    @Schema(description = "가입일 포함 오늘까지의 총 일수", example = "30")
    private int totalDays;

    /** 실제 기록이 존재한 일수 */
    @Schema(description = "실제 기록이 존재한 일수", example = "25")
    private int recordedDays;

    public static TotalRecordSummaryResponse of(LocalDate signupDate, LocalDate today, int recordedDays) {
        int totalDays = (int) ChronoUnit.DAYS.between(signupDate, today) + 1;
        return TotalRecordSummaryResponse.builder()
                .totalDays(totalDays)
                .recordedDays(recordedDays)
                .build();
    }
}