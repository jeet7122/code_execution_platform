package com.code_execution.code_executor.kafka;

import com.code_execution.code_executor.dto.SubmissionEvent;
import com.code_execution.code_executor.model.ExecutionResult;
import com.code_execution.code_executor.model.Submission;
import com.code_execution.code_executor.repository.ExecutionResultRepository;
import com.code_execution.code_executor.repository.SubmissionRepository;
import com.code_execution.code_executor.services.CodeExecutionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final MongoTemplate mongoTemplate;

    private final SubmissionRepository submissionRepository;
    private final ExecutionResultRepository executionResultRepository;
    private final CodeExecutionService codeExecutionService;
    private final Environment environment;
    private final ObjectMapper objectMapper;
    @KafkaListener(groupId = "code-executor-group", topics = "code-submissions")
    public void consume(String message){
         System.out.println("DB Name: " + mongoTemplate.getDb().getName());
        System.out.println("Received Submission: " + message);
        try{
            Thread.sleep(1000);
            SubmissionEvent event = objectMapper.readValue(message, SubmissionEvent.class);
            if (event == null) throw new RuntimeException("Failed to map event from message");
            String cleanedId = event.getSubmissionId().trim();
            System.out.println("Submission ID from event: " + event.getSubmissionId());
            System.out.println("Cleaned Length : " + cleanedId.length());
            Submission submission = submissionRepository.findById(new ObjectId(cleanedId))
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Submission not found in DB"));
            submission.setStatus("RUNNING");
            submissionRepository.save(submission);

            String output = codeExecutionService.executePythonCode(event.getCode());
            ExecutionResult result = getExecutionResult(event, output);
            executionResultRepository.save(result);
            submission.setStatus(result.getStatus());
            submissionRepository.save(submission);
        }
        catch (Exception e){
            System.out.println("Consuming and Processing went wrong: " + e);
        }
    }

    private ExecutionResult getExecutionResult(SubmissionEvent event, String output) {
        ExecutionResult result = new ExecutionResult();
        result.setSubmissionId(event.getSubmissionId());

        if (output.startsWith("ERROR")){
            result.setStatus("FAILED");
            result.setError(output);
            result.setTestCasesPassed(0);
            result.setTotalTestCases(1);
        }
        else {
            result.setStatus("SUCCESS");
            result.setTotalTestCases(1);
            result.setTestCasesPassed(1);
            result.setError(null);
        }
        result.setMemory(64L);
        result.setRuntime(120L);
        return result;
    }
}
