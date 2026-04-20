import express from "express"
import dotenv from "dotenv";
import {connectProducer} from "./kafka/producer.js"
import submitRoute from "./routes/submitRoutes.js"
import { connectToDB } from "./config/db.js";

const PORT = process.env.PORT || 3000;
dotenv.config()
const app = express();
app.use(express.json());
app.use('/api', submitRoute)

await connectToDB();
const startServer = async () => {
    await connectProducer();
    app.listen(PORT, () => {
        console.log(`Server is listnening on port: ${PORT}`);
    })
}

startServer();

