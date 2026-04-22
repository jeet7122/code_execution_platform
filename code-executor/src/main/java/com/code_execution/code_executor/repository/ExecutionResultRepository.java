package com.code_execution.code_executor.repository;

import com.code_execution.code_executor.model.ExecutionResult;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExecutionResultRepository extends MongoRepository<ExecutionResult, ObjectId> {
}
