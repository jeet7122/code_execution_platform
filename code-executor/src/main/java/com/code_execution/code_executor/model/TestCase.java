package com.code_execution.code_executor.model;

import lombok.Data;

@Data
public class TestCase {
    private String input;
    private String expectedOutput;
    private boolean isHidden;
}
