Gossip and Push-Sum Protocol using Scala/Akka model
---------------------------------------------------
---------------------------------------------------

Gossip protocol is a style of computer-to-computer communication protocol inspired by the form of gossip 
seen in social networks. To simulate a node in a distributed environment Actor's are used.
Every time a message is received, an actor randomly selects a neighbour and sends the message to that
Actor. This simulation is done over various kinds of topology, for e.g., Line, FullNetwork, 3D Grid and imperfect 
3D grid.

Design
-----
The Package uf.edu.mebin.algo defines a contract(trait) which is implemented(i.e., extended) by Gossip 
and PushSum class. This Trait defines the methods that are used in communication and also defines the termination 
criteria. 

Gossip and push sum class implement the trait Algorithm and provide appropriate functionality for the send,
receive and termination criteria. 

The AlgoFactory object is an implementation of Factory design pattern to obtain the instance of communication
to be used within the simulation. Being a factory design pattern,  it can be easily extended to incorporate 
more communication protocol in future too.

Similarly, the NetworkFactory class has been implemented which returns an instance of network over 
which the communication protocol is simulated. This too can be easily extended to incorporate more 
network typed in the future. 

The uf.edu.mebin.dist package contains the Boss and Worker class. The Boss Actor is used to gather statistics of 
the network that we simulate and as such does not participate in the network. The worker class actors are the ones 
that participate in the network simulation. 

Result
------

The Result of the simulation is contained within the Result folder.

COMMANDS
--------

```sbt "run 10 line gossip"```

where 10 is the number of actor nodes, line is the network topology and gossip is the protocol to be used.

Possible values for Network topology:-
1. fullNetwork
2. line
3. 3DGrid
4. Imp3DGrid

Possible values for communication protocol
1. push-sum
2. gossip

