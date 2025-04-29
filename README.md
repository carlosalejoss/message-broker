# Message Broker with RMI

This project implements a distributed message broker in Java using RMI (Remote Method Invocation), allowing producers and consumers to communicate across different JVMs.

## Project Structure

``` plaintext
message-broker
├── src
│   └── main
│       └── java
│           └── com
│               └── broker
│                   ├── core
│                   │   ├── MessageBroker.java    # RMI Server implementation
│                   │   ├── Queue.java            # Queue data structure
│                   │   └── Message.java          # Message class (Serializable)
│                   ├── interfaces
│                   │   ├── MessageBrokerRemote.java  # RMI Remote interface
│                   │   └── MessageCallback.java      # Callback interface
│                   └── examples
│                       ├── Producer.java         # RMI Client (Producer)
│                       └── Consumer.java         # RMI Client (Consumer)
├── pom.xml
└── README.md
```

## Features

- **Distributed Architecture**: Uses Java RMI for communication between components
- **Queue Declaration**: Create queues that can be used by producers and consumers
- **Message Publishing**: Send messages to specified queues
- **Message Consumption**: Read messages using callbacks
- **Thread Safety**: Synchronized queue operations

## Getting Started

### Prerequisites

- Java JDK 11 or higher
- Maven
- Git

### Building and Running

1. **Clone and Build**:

   ```bash
   git clone [<https://github.com/carlosalejoss/message-broker>]
   cd message-broker
   mvn clean install
   ```

2. **Start the Message Broker (RMI Server)**:

   ```bash
   java -cp target/message-broker-1.0-SNAPSHOT.jar com.broker.core.MessageBroker
   ```

   You should see: "Message Broker is running..."

3. **Run Clients** (in separate terminals):

   Start a Consumer:

   ```bash
   java -cp target/message-broker-1.0-SNAPSHOT.jar com.broker.examples.Consumer
   ```

   Start a Producer:

   ```bash
   java -cp target/message-broker-1.0-SNAPSHOT.jar com.broker.examples.Producer
   ```

## Implementation Details

### RMI Architecture

- Uses port 1099 for RMI registry
- Messages are serializable for network transmission
- Supports multiple producers and consumers
- Queues are thread-safe using synchronized methods

### Key Components

1. **MessageBroker**:
   - RMI server implementation
   - Manages queues and subscriptions
   - Handles message distribution

2. **Queue**:
   - Thread-safe message storage
   - FIFO message ordering
   - 5-minute message timeout

3. **Interfaces**:
   - `MessageBrokerRemote`: RMI interface for broker operations
   - `MessageCallback`: Interface for message delivery callbacks

## Usage Example

1. **Start the Broker**:

   ```bash
   java -cp target/message-broker-1.0-SNAPSHOT.jar com.broker.core.MessageBroker
   > Message Broker is running...
   ```

2. **Start a Consumer**:

   ```bash
   java -cp target/message-broker-1.0-SNAPSHOT.jar com.broker.examples.Consumer
   > Starting to consume messages from queue: exampleQueue
   ```

3. **Run the Producer**:

   ```bash
   java -cp target/message-broker-1.0-SNAPSHOT.jar com.broker.examples.Producer
   > Message sent to queue exampleQueue: Hello, World!
   > Message sent to queue exampleQueue: Another message
   > Message sent to queue exampleQueue: Third message
   ```

## Important Notes

- Start the Message Broker before any Producers or Consumers
- Ensure network connectivity between components
- Messages are distributed in real-time to all subscribed consumers
- Messages timeout after 5 minutes if not consumed

## Troubleshooting

Common issues and solutions:

1. **RMI Connection Refused**:
   - Ensure the Message Broker is running
   - Check if port 1099 is available
   - Verify network connectivity

2. **Messages Not Received**:
   - Confirm Consumer is subscribed to the correct queue
   - Verify Producer is using the correct queue name
   - Check for exceptions in the broker logs

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a new Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.