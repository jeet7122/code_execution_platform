package com.code_execution.code_executor.services;

import org.springframework.stereotype.Service;

public interface CodeExecutionService {
    String executePythonCode(String code);
}
