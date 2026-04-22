package com.code_execution.code_executor.model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "executionresults")
public class ExecutionResult {
    @Id
    private ObjectId id;
    private String submissionId;
    private String status; // SUCCESS, FAILED
    private Long runtime;
    private Long memory;
    private Integer testCasesPassed;
    private Integer totalTestCases;
    private String error;
}
