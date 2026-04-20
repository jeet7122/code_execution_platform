import { Kafka, Partitioners } from "kafkajs";

const kafka = new Kafka({
    clientId: 'code-executor',
    brokers: ['localhost:9092']
});

const producer = kafka.producer({
    createPartitioner: Partitioners.LegacyPartitioner
});

export const connectProducer = async () => {
    await producer.connect();
    console.log("Connected to Kafka");
}


export const sendMessage = async (topic, message) => {
    await producer.send({
        topic,
        messages: [{value: JSON.stringify(message)}]
    });
}
