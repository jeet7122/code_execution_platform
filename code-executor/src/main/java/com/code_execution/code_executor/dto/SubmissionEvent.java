package com.code_execution.code_executor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubmissionEvent {
    private String submissionId;
    private String userId;
    private String problemId;
    private String code;
    private String language;
}
