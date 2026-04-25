package com.code_execution.code_executor.model;

import com.code_execution.code_executor.model.enums.Difficulty;
import com.code_execution.code_executor.model.enums.Tag;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "problems")
@Data
public class Problem {
    @Id
    private String id;
    private String title;
    private String description;
    private Difficulty difficulty;
    private List<TestCase> testCases;
    private List<Tag> tags;

}
