import mongoose from "mongoose";

const executionResultSchema = new mongoose.Schema({
  submissionId: { type: mongoose.Schema.Types.ObjectId, ref: "Submission" },
  status: { type: String },
  runtime: { type: Number },
  memory: { type: Number },
  testCasesPassed: { type: Number },
  totalTestCases: { type: Number },
  error: { type: String }
}, { timestamps: true });

executionResultSchema.index({submissionId: 1})

export const ExecutionResult = mongoose.model("ExecutionResult", executionResultSchema);