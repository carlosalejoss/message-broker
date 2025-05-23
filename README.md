# Message Broker with RMI

## Prerequisites

- Java JDK 11 or higher
- Maven
- Git

## How to Install and Run the Project

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
   java -cp target/message-broker-1.0-SNAPSHOT.jar com.broker.examples.Consumer <host>
   ```

4. **Start a Producer**:

   ```bash
   java -cp target/message-broker-1.0-SNAPSHOT.jar com.broker.examples.Producer <host>
   ```

5. **Start an Admin client to manage queues:**

   ```bash
   java -cp target/message-broker-1.0-SNAPSHOT.jar com.broker.examples.Admin <host> delete <queueName>
   ```

## Usage Example

1. **Start the Broker**:

   ```bash
   java -cp target/message-broker-1.0-SNAPSHOT.jar com.broker.core.MessageBroker
   > Message Broker is running...
   ```

2. **Start a Consumer**:

   ```bash
   java -cp target/message-broker-1.0-SNAPSHOT.jar com.broker.examples.Consumer localhost
   > Consumer ready and bound to rmi://localhost:1099/MessageBroker
   > === Consumer Menu ===
   > 1. Subscribe to Queue
   > 2. Unsubscribe from Queue
   > 3. List Queues
   > 4. Exit
   > Select an option:
   ```

3. **Run the Producer**:

   ```bash
   java -cp target/message-broker-1.0-SNAPSHOT.jar com.broker.examples.Producer localhost
   > Producer ready and bound to rmi://localhost:1099/MessageBroker
   > === Producer Menu ===
   > 1. Create Queue
   > 2. Send Message
   > 3. Exit
   > Select an option:
   ```

4. **Queue Management**:

   ```bash
   # Delete a queue
   java -cp target/message-broker-1.0-SNAPSHOT.jar com.broker.examples.Admin localhost delete <queueName>
   > Admin ready and bound to rmi://localhost:1099/MessageBroker
   > Queue '<queueName>' deleted successfully
   ```

## Troubleshooting

Common issues and solutions:

1. **RMI Connection Refused**:
   - Ensure the Message Broker is running
   - Check if port 1099 is available
   - Verify network connectivity
   - Make sure the hostname/IP is correct
   - Check firewall settings

2. **Messages Not Received**:
   - Confirm Consumer is subscribed to the correct queue
   - Verify Producer and Consumer are using the same queue name
   - Check for exceptions in the broker logs
   - Ensure all componentes are connected to the same broker

## Features

- **Distributed Architecture**: Uses Java RMI for communication between components
- **Queue Declaration**: Create queues that can be used by producers and consumers
- **Queue Management**: List and delete existing queues
- **Message Publishing**: Send messages to specified queues
- **Message Consumption**: Read messages using callbacks
- **Thread Safety**: Synchronized queue operations
- **Message Expiration**: Messages automatically expire after 5 minutes
- **Round-Robin Distribution**: Messages are distributed evenly among consumers

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

## Important Notes

- Start the Message Broker before any Producers or Consumers
- Ensure network connectivity between components
- Messages are distributed using round-robin between consumers
- Messages are automatically removed after 5 minutes if not consumed
- Messages are persisted in memory until consumed or expired
- Consumers receive all pending messages when connecting
- Queue listing functionality is available in the Consumer menu
- Components can connect to remote brokers by specifying the hostname

## License

This project is licensed under the MIT License - see the LICENSE file for details.
