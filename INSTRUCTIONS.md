To establish a **local connection to RabbitMQ**, you'll need to follow these steps:

1. **Install RabbitMQ**:

   - If you haven't already, download and install RabbitMQ on your local machine. You can find installation instructions for various platforms on the official [RabbitMQ website](https://www.rabbitmq.com/download.html).

2. **Start RabbitMQ Service**:

   - Open an elevated command line (Run as Administrator).
   - Navigate to the `sbin` directory of the RabbitMQ Server installation directory (usually located at `C:\Program Files (x86)\RabbitMQ Server\rabbitmq_server-x.x.x\sbin`).
   - Run the following command to enable the RabbitMQ management plugin:
     ```
     rabbitmq-plugins.bat enable rabbitmq_management
     ```

3. **Access the Management UI**:

   - Once the service is started, you should be able to open the RabbitMQ management UI at [http://localhost:15672](http://localhost:15672).
   - Use the default credentials (username: `guest`, password: `guest`) to log in.
   - From the management UI, you can monitor queues, exchanges, connections, and other RabbitMQ-related information.

4. **Configure Your Application**:
   - In your application code, configure the RabbitMQ connection properties. For example, if you're using Spring Boot, add the following to your `application.properties` file:
     ```
     spring.rabbitmq.host=localhost
     ```

Remember that RabbitMQ supports several protocols (such as AMQP, MQTT, and STOMP), and each protocol has its own set of client libraries. Clients interact with RabbitMQ using these libraries over long-lived TCP connections. Ensure that your application's connection settings match the RabbitMQ configuration on your local machine¹²³. 🐰🥕

Source: Conversation with Bing, 4/7/2024
(1) Connections | RabbitMQ. https://www.rabbitmq.com/docs/connections.
(2) How to configure RabbitMQ connection with spring-rabbit?. https://stackoverflow.com/questions/42200317/how-to-configure-rabbitmq-connection-with-spring-rabbit.
(3) go - RabbitMQ localhost connection failed - Stack Overflow. https://stackoverflow.com/questions/67656811/rabbitmq-localhost-connection-failed.
(4) RabbitMQ Setup on local machine(Windows) | by Kiran Kumar - Medium. https://medium.com/@kiranbs890/rabbitmq-setup-on-local-machine-windows-958bada6003c.
