import mongoose from "mongoose";

const testCaseSchema = new mongoose.Schema({
    input: String,
    expected: String
});

const problemSchema = new mongoose.Schema({
    title: { type: String, required: true },
    description: { type: String, required: true },
    difficulty: { type: String, enum: ["easy", "medium", "hard"] },
    tags: [{ type: String }],
    testCases: [testCaseSchema],
}, {timestamps: true});



export const Problem = mongoose.model("Problem", problemSchema)