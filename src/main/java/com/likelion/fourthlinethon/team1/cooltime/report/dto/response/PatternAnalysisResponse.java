package com.likelion.fourthlinethon.team1.cooltime.report.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PatternAnalysisResponse {

    @JsonProperty("pattern_analysis")
    private String patternAnalysis;

    @JsonProperty("solution")
    private String solution;

    @JsonProperty("weekly_comparison")
    private String weeklyComparison;
}

