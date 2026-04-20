import mongoose from "mongoose";

const problemSchema = new mongoose.Schema({
    title: { type: String, required: true },
    description: { type: String, required: true },
    difficulty: { type: String, enum: ["easy", "medium", "hard"] },
    tags: [{ type: String }]
}, {timestamps: true});

export const Problem = mongoose.model("Problem", problemSchema)