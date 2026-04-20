import express from "express"
import "dotenv";
import {connectProducer} from "./kafka/producer.js"
import submitRoute from "./routes/submitRoutes.js"

const PORT = process.env.PORT || 3000;

const app = express();
app.use(express.json());

app.use('/api', submitRoute)


const startServer = async () => {
    await connectProducer();
    app.listen(PORT, () => {
        console.log(`Server is listnening on port: ${PORT}`);
    })
}

startServer();

