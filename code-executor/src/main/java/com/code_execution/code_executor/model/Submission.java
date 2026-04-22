package com.code_execution.code_executor.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "submissions")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Submission {
    @Id
    private ObjectId id;
    private String userId;
    private String problemId;
    private String code;
    private String language;
    private String status; // PENDING, RUNNING, SUCCESS, FAILED
}
