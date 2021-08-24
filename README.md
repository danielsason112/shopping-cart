# Customer Shopping Cart Service 
## DB Setup And Configuration
The Service requires MongoDb database, with two optional setups: 
### Running A Standalone MongoDb Database
The service default configuration uses a standalone MongoDb server with the default host (“localhost”) and the default port (“27017”).
It can be easily set up with a Docker container, by running the following command in the terminal: 
```bash
docker run -p 27017:27017 mongo:latest
```
### Running A MongoDB Replica Set With Transactions Support
A MongoDB single document writing is atomic, so transactions are not required. But, when the service creates a new shopping cart, if an unexpired cart is found in the database, the service first updates it to expired, then saves the new shopping cart document. In order to prevent data incredibility (e.g. the database updates the found shopping cart to expired, but fails to create the new one) the use of transactions is required.  As of version 4.0, MongoDb supports transactions, but only for a replica set (​“A replica set in MongoDB is a group of mongod processes that maintain the same data set.” ​ - https://docs.mongodb.com/manual/replication/​).
It can be easily set up with a Docker container by running the following command: 
```bash
docker run -d -p 27017:27017 -p 27018:27018 -p 27019:27019 candis/mongo-replica-set
```
Next, the creation of the database and the collection needs to be done manually in a replica set. Open the container bash: 
```bash
docker exec -it [container-id] bash 
```
Open MongoDB Console: 
```bash
mongo 
```
Create the database: 
```bash
use shoppingcart 
```
Create the collection:
```bash
db.createCollection("carts")
```

Next, In application.properties uncomment the following line (9): 
 ```
#spring.data.mongodb.uri=mongodb://localhost:27017,localhost:27018,local host:27019/?replicaSet=rs0&readPreference=primary&ssl=false 
```
 
## Integration With Other Service 
### Users Management Service
#### [danielsason112/users-management-service](https://github.com/danielsason112/user-management-service)
When creating a new shopping cart, the service validates the existence of the provided user. The Users Management Service’s base-uri is defined in application.properties (line 13): 
```
users-management-service.uri=http://localhost:8082
```
### Shopping Catalog Service
#### [danielsason112/ShoppingCatalogService](https://github.com/danielsason112/ShoppingCatalogService)
When creating or updating a shopping cart, the service validates the existence of the provided products in the cart. The Shopping Catalog Service’s base-uri is defined in application.properties (line 14): 
```
shopping-catalog-service.uri=http://localhost:8081
```
### Products Coupon Service
When querying for a valid shopping cart (GET /shoppingCarts/{email}), the service checks if there are available coupons for each of the products. The Coupons Management Service’s base-uri is defined in application.properties (line 15): 
```
products-coupon-service.uri=http://localhost:8083 
```
## Usage Instructions 
### Build And Run
Import the root folder to an IDE and build it with Gradle, then run it.
In Spring Tool Suite / Eclipse:
- File > Import > Gradle > Existing Gradle Project.
- Click next until the “Project root directory” option appears and select the root folder.
- Click finish and wait for the build to finish.
- Right click “il.ac.cloud.afeka.CustomerShoppingCartService” > Run as > Spring Boot App.
The default port is set to 8080 and can be changed in the application.properties file. 

## Kafka Topics Subscription
The service is listening for the topic “new-shopping-cart-event” on the Kafka default server configuration (“localhost:9092”). 
### Running a Kafka Server
First, download Kafka [here](https://www.apache.org/dyn/closer.cgi?path=/kafka/2.7.0/kafka_2.13-2.7.0.tgz). 

Then, open a terminal session in the root directory and start the ZooKeeper service (on Windows add “win/” after “bin/” and replace all “.sh” with “.bat” on all of the following commands): 
```bash
bin/zookeeper-server-start.sh config/zookeeper.properties
```
Next, on another terminal session start the Kafka service: 
### Creating Topics And Events
Open another terminal session and create the “new-shopping-cart-event” topic: 
```bash
bin/kafka-server-start.sh config/server.properties
```
Start a Kafka console producer for the created topic: 
```bash
bin/kafka-topics.sh --create --topic new-shopping-cart-event --bootstrap-server localhost:9092
```
Finally, send a shopping cart Json. Each line will result in an event that the consumer in the service will handle, and store the provided shopping cart: 
```bash
bin/kafka-console-producer.sh --topic new-shopping-cart-event --bootstrap-server localhost:9092 
> { "user:" { "email" : "customer14@shop.com" }, "products": [ { "productId": "4736cg8", "amount": 1 } ] }
```
In order to listen to a different topic change the “new-shopping-cart-event” on the application.properties file (line 19): 
```bash
spring.cloud.stream.bindings.input-consumer.destination=new-shopping-car t-event
```
 
