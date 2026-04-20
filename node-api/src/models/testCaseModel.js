import mongoose from "mongoose";

const testCaseSchema = new mongoose.Schema({
  problemId: { type: mongoose.Schema.Types.ObjectId, ref: "Problem" },
  input: { type: mongoose.Schema.Types.Mixed, required: true },
  expectedOutput: { type: mongoose.Schema.Types.Mixed, required: true },
  isHidden: { type: Boolean, default: false }
}, { timestamps: true });

testCaseSchema.index({ problemId: 1 });

export const TestCase = mongoose.model("TestCase", testCaseSchema);