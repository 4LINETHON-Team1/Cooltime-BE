package com.likelion.fourthlinethon.team1.cooltime.report.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.likelion.fourthlinethon.team1.cooltime.user.entity.MyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PatternAnalysisRequest {

    @JsonProperty("pattern_type")
    private MyType patternType;

    @JsonProperty("current_week")
    private WeekData currentWeek;

    @JsonProperty("last_week")
    private WeekData lastWeek;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WeekData {
        @JsonProperty("total_log_count")
        private Integer totalLogCount;

        @JsonProperty("postponed_log_count")
        private Integer postponedLogCount;

        @JsonProperty("category_stats")
        private List<CategoryStat> categoryStats;

        @JsonProperty("reason_stats")
        private List<ReasonStat> reasonStats;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryStat {
        @JsonProperty("category")
        private String category;

        @JsonProperty("count")
        private Integer count;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReasonStat {
        @JsonProperty("reason")
        private String reason;

        @JsonProperty("count")
        private Integer count;
    }
}

