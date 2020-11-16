#!/usr/bin/env python

from kafka import KafkaProducer


def main():
    producer = KafkaProducer(bootstrap_servers="kafka:9092")
    for i in range(200):
        producer.send(topic="db.database.collection", value=f"message-{i}".encode(), key=f"{i}".encode())
        print(f"Sent {i}.")


if __name__ == "__main__":
    main()
