import {v4} from "uuid";
import {sendMessage} from "../kafka/producer.js";


export const submitCode = async (req, res) => {
    const {code, language, problemId} = req.body;

    const submissionId = v4();

    const payload = {
        submissionId,
        code,
        language,
        problemId
    };

    await sendMessage("code-submissions", payload);

    res.json({
        message: "Submission received",
        submissionId
    });
}
