# Hotel-Room-Booking-App
Backend project 

We are breaking the Hotel room booking application into three different microservices, which are as follows:

API-Gateway - This service is exposed to the outer world and is responsible for routing all requests to the microservices internally.

Booking service - This service is responsible for collecting all information related to user booking and sending a confirmation message once the booking is confirmed.

Payment service - This is a dummy payment service; this service is called by the booking service for initiating payment after confirming rooms.
 
 
Part 1 - Rationale behind the architecture of the application

The main reason for breaking the application into different microservices is to follow the single responsibility principle, where each service is responsible for its own single service, which is considered one of the best industry practices. Let’s try to understand this with the following scenario:


Suppose we are taking only UPI payment for now, and later, we find the necessity to include credit card/debit card payment as well; then, we have to update only the payment service keeping the remaining services untouched.


Application workflow

Let’s now try to understand the workflow of the application using the following architecture diagram.

Initially, the API Gateway, Booking service and Payment service register themselves on the Eureka server. Note that the user does not directly interact with Booking or Payment service. All requests are sent to API Gateway which then sends the requests to the relevant microservice.
The user initiates room booking using the 'Booking' service by providing information such as toDate, fromDate, aadharNumber and number of rooms required (numOfRooms).
The 'Booking' service returns back the list of room numbers and price associated and prompts the user to enter the payment details if they wish to continue ahead with the booking. It also stores the details provided by the user in its database. 
If the user wants to go ahead with the booking, then they can simply provide the payment related details like bookingMode, upiId/ cardNumber to the 'Booking' service which will be further sent to the 'Payment' service. Based on this data, the payment service will perform a dummy transaction and return back the transaction Id associated with the transaction to the Booking service. All the information related to transactions is saved in the database of the payment service.
Once the transaction completes, booking is confirmed and therefore Booking service sends a confirmation message on the console.


Part 2 - Rationale behind the communication of the application

The rationale behind choosing synchronous communication (REST) between the Booking and Payment services is that the response('transactionId') is required before confirming the booking and sending a message.



1. API GATEWAY

This service acts as a gateway for all user requests. Instead of exposing the Booking and Payment services, this gateway interacts with the users and re-routes the requests to the relevant service internally.


1.1 Configure this service to run on port number 9191.
1.2 Configure 'API Gateway' as Eureka  Client

Add Booking and Payment service as ids and URI. Also, add relevant paths for both services.

For eg:

Booking service configuration:

id: BOOKING-SERVICE
uri: lb://BOOKING-SERVICE
predicates:
  - Path=/hotel/**
 

Payment service configuration:

 
id: PAYMENT-SERVICE
uri: lb://PAYMENT-SERVICE
predicates:
  - Path=/payment/**
 
Once the configuration is done properly for this service, run the Eureka server and API Gateway service on your local host. Post this when you hit the Eureka server IP from your browser, you will see something similar. 

 

Address:

http://localhost:8761/


2. BOOKING SERVICE

This service is responsible for taking input from users like- toDate, fromDate, aadharNumber and the number of rooms required (numOfRooms) and save it in its database. This service also generates a random list of room numbers depending on 'numOfRooms' requested by the user and returns the room number list (roomNumbers) and total roomPrice to the user. The logic to calculate room price is as follows: 

roomPrice = 1000* numOfRooms*(number of days)
Here, 1000 INR is the base price/day/room.
 

 If the user wishes to go ahead with the booking, they can provide the payment related details like bookingMode, upiId / cardNumber, which will be further sent to the payment service to retrieve the transactionId. This transactionId then gets updated in the Booking table created in the database of the Booking Service and a confirmation message is printed on the console.

 

A sample code that you could refer to create random room numbers is as follows:                
/* The following code snippet returns a random number list with upperbound of 100 and 'count' number of entries in the number list*/                

 

public static ArrayList<String> getRandomNumbers(int count){                
         Random rand = new Random();                
         int upperBound = 100;                
         ArrayList<String>numberList = new ArrayList<String>();                

        for (int i=0; i<count; i++){                
                    numberList.add(String.valueOf(rand.nextInt(upperBound)));                
         }                

          return numberList;                
}
 

             
Output:                

for count=5, numberList contains any 5 numbers between 0 and 100          
   

     

2.1    Model Classes:
Refer to the “booking” table in the schema to create the entity class named “BookingInfoEntity”. 


2.2   Controller Layer:
 

Endpoint 1: This endpoint is responsible for collecting information like fromDate, toDate,aadharNumber,numOfRooms from the user and save it in its database.

 URI: /booking
 HTTP METHOD: POST
 RequestBody: fromDate, toDate,aadharNumber,numOfRooms
Booking Service
Booking Service
Response Status: Created
Response: ResponseEntity<BookingInfoEntity>
Response
Response
Note 1: The value of the transactionId returned is 0. It means that no transaction is made for this booking. Once the transaction is done, the transactionId field in the booking table will get replaced with the transactionId received from the Payment service.


Note 2: The room numbers displayed are not based on the availability of vacant rooms. They are rather randomly generated integers between 1 and 100. This is done to trim down the complexity of the problem statement.  

 

Note 3: The field 'id' in the response body represents the 'BookingId'.
 

Endpoint 2: This endpoint is responsible for taking the payment related details from the user and sending it to the payment service. It gets the transactionId from the Payment service in respose and saves it in the booking table. Please note that for the field 'paymentMode', if the user provides any input other than 'UPI' or 'CARD', then it means that the user is not interested in the booking and wants to opt-out.

URL: booking/{bookingId}/transaction
HTTP METHOD: POST
PathVariable: int
RequestBody: paymentMode, bookingId, upiId,cardNumber
Response Status: Created
Response: ResponseEntity<BookingInfoEntity>
Note that the transaction Id this time stores the actual transactionId associated with the transaction. 

 

Exception 1: If the user gives any other input apart from “UPI” or “CARD”, the response message should look like the following:

{
   "message": "Invalid mode of payment",
  "statusCode": 400
}
 

Exception 2: If no transactions exist for the Booking Id passed to this endpoint then the response message should look like the following: 

{
   "message": " Invalid Booking Id ",
  "statusCode": 400
}
 

2.3   Configure this service to run on port number 8081.
2.4    Configure the hotel booking service as Eureka  Client
 
Once the configuration is done properly for this service, run the Eureka server, API Gateway and Booking service on your localhost. Post this when you hit the Eureka server IP from your browser, you will see something similar.

 

Address:

http://localhost:8761/
 
 
 
3. PAYMENT SERVICE
 
 This service is responsible for taking payment-related information- paymentMode, upiId or cardNumber, bookingId and returns a unique transactionId to the booking service. It saves the data in its database and returns the transactionId as a response.

 

3.1 Model Classes:
Refer to the “transaction” table in the schema to create the entity class named “TransactionDetailsEntity”.

3.2 Controller Layer:

 

Endpoint 1: This endpoint is used to imitate performing a transaction for a particular booking. It takes details such as bookingId, paymentMode,upiId or cardNumber and returns the transactionId automatically generated while storing the details in the ‘transaction’ table. Note that this 'transactionId' is the primary key of the record that is being stored in the 'transaction' table. 

After receiving the transactionId from 'Payment' service, confirmation message is printed on the console.

 

Message String: 

String message = "Booking confirmed for user with aadhaar number: " 
+ bookingInfo.getAadharNumber()  
+    "    |    "  
+ "Here are the booking details:    " + bookingInfo.toString();
         

Note: This endpoint will be called by the ‘endpoint 2’ of the Booking service. 

URL: /transaction 
HTTP METHOD: POST
RequestBody: paymentMode, bookingId, upiId, cardNumber
Response Status: Created

Response: ResponseEntity<transactionId>
 

EndPoint 2: This endpoint presents the transaction details to the user based on the transactionId provided by the user.

 URL: /transaction/{transactionId}
 HTTP METHOD: GET
 RequestBody: (PathVariable) int
 Response Status: OK
 Response: ResponseEntity<TransactionDetailsEntity>
 

3.3 Configure the service to run on port 8083.

3.4 Configure the service as a Eureka client. 

 

Once the Eureka client configuration is done properly for this service,run the Eureka server, API Gateway, Booking service and Payment service on your localhost. Post this when you hit the Eureka server IP from your browser, you will see something similar.


Address:

http://localhost:8761/
