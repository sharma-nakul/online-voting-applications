# Distributed Systems Projects
It has 3 projects in which assignment 1 and assignment 2 are similar with few additions in assignment 2. 

### Assignment 1 and Assignment 2 description ###

Steps to run this project:

1. Import this project as Gradle project in your IDE.
2. In terminal window of your IDE, run following commands:
    gradle build
    gradle run

Note: You should have gradle package installed in your system in order to import this project.

### Summary of Assignment 1 & 2 ###

This is a college assignment where we had to implement RestFUL web services using Spring MVC framework.

* There are two Model classes : Poll and Moderator. The Poll class holds the data structure of all the polls available for users.
* The Moderator class holds the user data structure who will do the polling/voting using RestFUL web services resources.
* Version - 0.1.0
* 'api' directory holds the class of RestFul web service controller and basic header authorization.

### Additional Changes in Assignment 2 ###
* Kafka Email Notification
* Spring Scheduler for scheduling email notification.
* MongoDb to persist data of Polls and Users.
* Advanced Header Authorization.

### Assignment 3 description ###

### Purpose ###
The goal of this lab is to learn how different types of hashing work by implementing a proof of concept (POC) client-side sharding, aka consistent hash ring and rendezvous hashing.
Fork this repo and use it as a baseline for the lab.
Papers:
Consistent Hashing and Random Trees
Web Caching with Consistent Hashing

### Part I (Consistent hashing) ####

The baseline code has two modules--client and server--and you can be running three server instances (A, B, and C) and one client that talks to the servers. You won’t be making any changes to the server module, but all consistent hashing implementation should be on the client side only.
Run the baseline code first to understand how GET and PUT to the distributed cache work before sharding the data. All the data should be distributed across:
A: http://localhost:3000
B: http://localhost:3001
C: http://localhost:3002
1. Implement node (cache server) lookup using consistent hashing in the client module.
2. Call the consistent hash interface from step 1 to get bucket number. See this example.
3. From the client, PUT this below sequence of key-value pairs into the three cache servers and check each server at http://localhost:300{0|1|2}/cache/ to make sure data is sharded across three servers.
{Key => Value}
1 => a
2 => b
3 => c
4 => d
5 => e
6 => f
7 => g
8 => h
9 => i
10 => j
4. Finally, retrieve all keys, 1 to 10, via GET(x) and print out their values to the console.


### Part II - Rendezvous or Highest Random Weight (HRW) hashing ###

In the part II, you will be implementing another sharding approach called the rendezvous hashing on the client side. Repeat step 1 to 4 from the part I using the rendezvous hashing.


