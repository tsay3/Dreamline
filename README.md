# Dreamline
Dreamline is a Java application that looks up flights by origin and destination airports, or by airline and flight number, displays comfort scores for each flight, see the route on a map, and book airline tickets.

Original proposal by Cole Pearson, Nausherwan Tirmizi, Brian Kopec, and Harshal Patel.

Implementation by Shreya Boyapati, Thomas Say, Ayesha Quadri Syeda, and Danyal Warraich.

# Instructions

This program was designed to be executed from inside IntelliJ. The source code is provided purely for educational purposes only.

This program also makes use of an API client ID and secret key from Amadeus, as well as a Google Maps API key, which is not included in this repository. A working Amadeus ID and secret must be added to "api_keys.xml" within the resources folder with the XML tags `client_id` and `client_secret` for flight lookup functionality, and a Google Maps API key must be added under the tag `google_key` for the flight tracking functionality.

# Thomas Say's Contributions

(All files below can be found in src/main/java/com/example/projectdreamline)

Thomas Say fully wrote the following files:

* DreamScore.java
* FlightTrackingExample.java

Thomas Say partially wrote the following files:

* Menu.java (lines 48-89)
* FlightInfoController.java (lines 161-265)

In addition, Thomas Say wrote all of the files in the src/main/java/com/example/projectdreamline/data directory, although it is currently unused by the program:

* Data.java
* DatabaseHandler.java
* DataFaker.java
* FlightComfort.java
