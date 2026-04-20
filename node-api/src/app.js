import express from "express"
import "dotenv";

const PORT = process.env.PORT || 3000;

const app = express();
app.use(express.json());


app.listen(PORT, () => {
    console.log(`Server is running on port: ${PORT}`);
})



