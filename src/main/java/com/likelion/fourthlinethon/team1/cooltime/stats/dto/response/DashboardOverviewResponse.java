package com.likelion.fourthlinethon.team1.cooltime.stats.dto.response;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;


@Getter
@Builder
@AllArgsConstructor
@Schema(title = "DashboardOverviewResponse", description = "대시보드 개요 응답 DTO")
public class DashboardOverviewResponse {
    @JsonUnwrapped
    private TotalRecordSummaryResponse totalRecordSummary;
    @JsonUnwrapped
    private TopCategoryResponse topCategory;
    private int postponedPercent;
    private boolean aiReportAvailable;

    public static DashboardOverviewResponse from(
            TotalRecordSummaryResponse totalRecordSummary,
            TopCategoryResponse topCategory,
            int postponedPercent,
            boolean aiReportAvailable
    ) {
        return DashboardOverviewResponse.builder()
                .totalRecordSummary(totalRecordSummary)
                .topCategory(topCategory)
                .postponedPercent(postponedPercent)
                .aiReportAvailable(aiReportAvailable)
                .build();
    }

}
