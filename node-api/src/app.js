import express from "express"
import dotenv from "dotenv";
import {connectProducer} from "./kafka/producer.js"
import submitRoute from "./routes/submitRoutes.js"
import { connectToDB } from "./config/db.js";
import * as path from "node:path";

const PORT = process.env.PORT || 3000;
dotenv.config({
    path: path.resolve(process.cwd(), ".env"),
})
const app = express();
app.use(express.json());
app.use('/api', submitRoute)

await connectToDB();
const startServer = async () => {
    await connectProducer();
    app.listen(PORT, () => {
        console.log(`Server is listening on port: ${PORT}`);
    })
}

startServer();

