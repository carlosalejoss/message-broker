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
│                       ├── Admin.java                # Admin client for queue management
│                       ├── Consumer.java             # RMI Client (Producer)
│                       ├── MessageCallbackImpl.java  # Callback implementation
│                       └── Producer.java             # RMI Client (Consumer)
├── pom.xml
└── README.md
```

## Features

- **Distributed Architecture**: Uses Java RMI for communication between components
- **Queue Declaration**: Create queues that can be used by producers and consumers
- **Queue Management**: List and delete existing queues
- **Message Publishing**: Send messages to specified queues
- **Message Consumption**: Read messages using callbacks
- **Thread Safety**: Synchronized queue operations
- **Message Expiration**: Messages automatically expire after 5 minutes
- **Round-Robin Distribution**: Messages are distributed evenly among consumers

## Getting Started

### Prerequisites

- Java JDK 11 or higher
- Maven
- Git

### Building and Running

1. **Clone and Build**:

   ```bash
   git clone https://github.com/carlosalejoss/message-broker.git
   cd message-broker
   mvn clean install
   ```

2. **Start the Message Broker (RMI Server)**:

   ```bash
   java -cp target/message-broker-1.0-SNAPSHOT.jar com.broker.core.MessageBroker
   ```

3. **Run Clients** (in separate terminals):

   Start a Consumer (multiple consumers can be started to observe round-robin message distribution):

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
   - Handles message distribution in round-robin fashion
   - Maintains queue state across connections

2. **Queue**:
   - Thread-safe message storage
   - FIFO message ordering
   - Automatic message cleanup after 5 minutes
   - Delivers pending messages to new subscribers

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
   > === Consumer Menu ===
   > 1. Subscribe to Queue
   > 2. Unsubscribe from Queue
   > 3. Exit
   > Select an option:
   ```

3. **Run the Producer**:

   ```bash
   java -cp target/message-broker-1.0-SNAPSHOT.jar com.broker.examples.Producer
   > === Producer Menu ===
   > 1. Create Queue
   > 2. Send Message
   > 3. Exit
   > Select an option:
   ```

4. **Queue Management**:

   ```bash
   # List all queues
   java -cp target/message-broker-1.0-SNAPSHOT.jar com.broker.examples.Admin list
   > Available queues:
   > - exampleQueue

   # Delete a queue
   java -cp target/message-broker-1.0-SNAPSHOT.jar com.broker.examples.Admin delete <queueName>
   > Queue '<queueName>' deleted successfully
   ```

## Important Notes

- Start the Message Broker before any Producers or Consumers
- Ensure network connectivity between components
- Messages are distributed using round-robin between consumers
- Messages are automatically removed after 5 minutes if not consumed
- Messages are persisted in memory until consumed or expired
- Consumers receive all pending messages when connecting

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

## License

This project is licensed under the MIT License - see the LICENSE file for details.
