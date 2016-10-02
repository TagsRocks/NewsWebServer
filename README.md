## Brief
Develop a simplistic news exchange proof-of-concept (POC) application which will receive [ninjs](http://dev.iptc.org/ninjs) format news documents as input, validate & categorise the content and publish to subscribing clients (via HTTP) if the news document matches a news category of interest to the client.

## Implementation Requirements
1. Create a Java 8 project on GitHub.
2. Use only standard Java SE 8 libraries to implement the functionality (versus open source or commercial frameworks, libraries, utilities etc.).
3. Use the ninjs standard as the data interchange format.

## Assessment
The application does not need to be feature-complete or production-ready.   It is designed to assess the approach to the solution, knowledge of the Java language & platform and use of implementation patterns.  The following aspects will form the basis of the assessment:

1. The approach to the architecture & design of the application.
2. The testability (functional and non-functional) of the application.
3. Use of good source code management practices.

## Implementation Review

I probably worked on this for a total of 16 hours, I made little attempt to make it look pretty and only serves to demonstrate
the common  techniques for web development. In practice I would separate the files into their respective classes and make
the JavaScript much more object oriented instead of polluting the global scope with random functions.

The sessions work by simply incrementing a number, in reality this would be something more like a hash of the username/ip/random number
or something else guaranteed to be unique.

I would also employ the microservice architecture, having the databases on separate servers (possibly on a private local network
or bound via vpn) exposing an API for setting
and retrieving data. For a large project I'd imagine there would be numerous servers and databases required so the
architecture of the backend would have more consideration.

I'd also use SSL as well as hashing all the user data stored on the database.

I didn't find time to implement adding user posts but I've clearly demonstrated the ability to send and receive data
from client -> server and visa versa.

Also none of the input is validated so this website is currently incredibly insecure, you're free to steal sessions and passwords
and wreak havoc, so if user posts were implemented you could probably have a lot of fun.

## Would I use any other libraries?

Java EE Edition would've been nice to parse JSON on the server.

Other than those this basic website is easily implemented cleanly and quickly without any libraries really. I'd happily continue
down this route, at least for the client side. That said I'm very familiar with the advantages of using jQuery, 
Bootstrap and other client side libraries for large or commercial projects as they can save a lot of time. 
