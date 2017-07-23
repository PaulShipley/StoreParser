

# State Aware SAX Parser

This is the example code that goes with my blog post

http://paulshipley.id.au/papers/coding/state-aware-sax-parser

One of the difficulties in writing SAX parsers is that, unlike DOM, SAX is not state aware and it is the applications responsibility to track its own state. This is made more difficult by the nature of the events that SAX reports. For example, the startElement method is triggered for every element, whereas the developer usually wants to know when a particular element starts. For character data the situation is even worse as the characters method is triggered for any character data and the containing element is not reported – it is up to the application to know this.

The usual solution to these issues is for the application to somehow maintain its state; and this is where the problems start as there are numerous ways to do this and the code often quickly becomes unmanageable.

My technique is to extend the DefaultHandler with a new class that is state aware and to provide a mechanism to register event listeners for each of the elements.



## Usage



## Developing



### Tools

