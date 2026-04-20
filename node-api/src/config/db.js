import mongoose from "mongoose";

const DB_URI = process.env.MONGO_URI

export const connectToDB = async () => {
    try{
        await mongoose.connect(DB_URI);
        console.log("Connected to Database!");
    }
    catch(err){
        console.error("Failed to connect with DB!", err)
        process.exit(1)
    }
}