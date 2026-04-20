import mongoose from "mongoose";

const submissionSchema = new mongoose.Schema({
  userId: { type: mongoose.Schema.Types.ObjectId, ref: "User" },
  problemId: { type: mongoose.Schema.Types.ObjectId, ref: "Problem" },
  language: { type: String, required: true },
  code: { type: String, required: true },
  status: {
    type: String,
    enum: ["PENDING", "RUNNING", "SUCCESS", "FAILED"],
    default: "PENDING"
  }
}, { timestamps: true });

submissionSchema.index({ userId: 1 });
submissionSchema.index({ problemId: 1 });

export const Submission = mongoose.model("Submission", submissionSchema);