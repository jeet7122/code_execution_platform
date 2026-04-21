import {Submission} from "../models/submissionModel.js";
import {sendMessage} from "../kafka/producer.js";


export const submitCode = async (req, res) => {
    try{
        const {userId, code, language, problemId} = req.body;
        const submission = await Submission.create({
            userId,
            problemId,
            language,
            code,
            status: "PENDING"
        });
        await sendMessage("code-submission", {
            submissionId: submission._id.toString(),
            problemId,
            userId,
            code,
            language,
        });

        res.status(201).json({
            message: "Submission received",
            submissionId: submission._id
        })
    }
    catch(err){
        console.error("Submission Error: ", err);
        res.status(500).json({message: "Submission Failed"});
    }

}
