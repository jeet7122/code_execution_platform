package com.code_execution.code_executor.kafka;

import com.code_execution.code_executor.dto.SubmissionEvent;
import com.code_execution.code_executor.model.ExecutionResult;
import com.code_execution.code_executor.model.Problem;
import com.code_execution.code_executor.model.Submission;
import com.code_execution.code_executor.model.TestCase;
import com.code_execution.code_executor.repository.ExecutionResultRepository;
import com.code_execution.code_executor.repository.ProblemRepository;
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

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final MongoTemplate mongoTemplate;

    private final SubmissionRepository submissionRepository;
    private final ExecutionResultRepository executionResultRepository;
    private final ProblemRepository problemRepository;
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

            Problem problem = problemRepository.findById(event.getProblemId())
                    .orElseThrow(() -> new RuntimeException("Problem not found"));

            int passed = 0;
            int total = problem.getTestCases().size();

            for (TestCase tc : problem.getTestCases()){
                String output = codeExecutionService.executePythonCode(
                        event.getCode(),
                        tc.getInput()
                );
                if(normalize(output).equals(normalize(tc.getExpectedOutput()))) passed++;

            }
            ExecutionResult result = new ExecutionResult();
            result.setSubmissionId(event.getSubmissionId());
            result.setTotalTestCases(total);
            result.setTestCasesPassed(passed);

            if (passed == total) {
                result.setStatus("SUCCESS");
            } else if (passed == 0) {
                result.setStatus("FAILED");
            } else {
                result.setStatus("PARTIAL");
            }
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

    private String normalize(String s){
        return s.trim().replaceAll("\\r\\n", "\n");
    }
}
