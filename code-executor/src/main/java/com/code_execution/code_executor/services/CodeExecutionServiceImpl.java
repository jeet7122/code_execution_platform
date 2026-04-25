package com.code_execution.code_executor.services;

import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;


@Service
public class CodeExecutionServiceImpl implements CodeExecutionService {
    @Override
    public String executePythonCode(String code, String input) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("python", "-c", code);
            Process process = processBuilder.start();

            // ✅ Send input
            if (input != null && !input.isEmpty()) {
                process.getOutputStream().write(input.getBytes());
            }

            // 🔥 CRITICAL: close stdin so Python stops waiting
            process.getOutputStream().close();

            // ✅ Read output
            BufferedReader outputReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );

            String output = outputReader.lines().collect(Collectors.joining("\n"));

            // ✅ Also read errors (IMPORTANT)
            BufferedReader errorReader = new BufferedReader(
                    new InputStreamReader(process.getErrorStream())
            );

            String error = errorReader.lines().collect(Collectors.joining("\n"));

            process.waitFor(); // ensure process completes

            if (!error.isEmpty()) {
                return "ERROR: " + error;
            }

            return output;

        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }
}
