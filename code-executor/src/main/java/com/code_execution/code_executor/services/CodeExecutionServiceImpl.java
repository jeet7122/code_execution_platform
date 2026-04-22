package com.code_execution.code_executor.services;


import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;


@Service
public class CodeExecutionServiceImpl implements CodeExecutionService {
    @Override
    public String executePythonCode(String code) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("python", "-c", code);
            Process process = processBuilder.start();

            BufferedReader output = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );

            BufferedReader errorStream = new BufferedReader(
                    new InputStreamReader(process.getErrorStream())
            );

            StringBuilder result = new StringBuilder();

            String line;

            while ((line = output.readLine()) != null){
                result.append(line).append("\n");
            }

            StringBuilder errorResult = new StringBuilder();
            while ((line = errorStream.readLine()) != null){
                errorResult.append(line).append("\n");
            }
            process.waitFor();

            if (!errorResult.isEmpty()) {
                return "ERROR: " + errorResult;
            }

            return result.toString();
        }
        catch (Exception e){
            return "Execution Failed" + e;
        }
    }
}
