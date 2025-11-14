package com.likelion.fourthlinethon.team1.cooltime.test.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import java.util.List;

@Getter
public class TestRequest {

    @NotEmpty(message = "answers는 비어 있을 수 없습니다.")
    @Size(min = 7, max = 7, message = "answers는 정확히 7개의 값을 가져야 합니다.")
    private List<@NotNull @Min(1) @Max(3) Integer> answers;
}
