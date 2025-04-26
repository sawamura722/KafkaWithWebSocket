#WebSocket only this time. No HTTP request.

#HTTP vs WebSockets

For Structured Data Objects
HTTP Advantages
Better built-in content negotiation (application/json)
Automatic validation in many frameworks
Clear API documentation standards (OpenAPI/Swagger)
Easier to debug with browser tools
Simpler error handling with HTTP status codes
WebSocket Trade-offs
Requires custom message format definitions
Needs more manual validation
Debugging can be more complex
When to Choose Each
Choose HTTP when:

Your data objects are complex with many fields
You need strong validation
Operations are infrequent
You want REST semantics (GET, POST, PUT, DELETE)
Choose WebSockets when:

You need high message frequency
Latency is critical
You're already using WebSockets for other parts of the app
You're sending many messages from same client
Hybrid Approach
Many apps use a hybrid approach:

HTTP for CRUD operations on complex objects
WebSockets for real-time updates and notifications

#Code explaination (frontend)
.subscribe() - Sets up a listener to receive messages from the server
.publish() - Sends a message/request to the server
This is similar to HTTP GET, but follows a different pattern:


#Installed library for frontend

npm install sockjs-client

npm install @stomp/stompjs

(https://github.com/user-attachments/assets/a78b0c18-c283-4858-9926-1fdfe1aaf0ee)


