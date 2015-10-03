package uf.edu.mebin.algo

import akka.actor.ActorRef
import uf.edu.mebin.dist.GossipMessage
import uf.edu.mebin.dist.Message

/**
 * @author mebin
 */
class Gossip(actorNum:Int) extends Algorithm{
   var count = 0 //message received count
   var msg:GossipMessage = null
   def send(actor:ActorRef)={
     actor ! msg
  }
  
  def receiveMessage(m:Message) = {
    count += 1
    msg = m.asInstanceOf[GossipMessage]
  }
  
  def isTerminate() = {
    count >= 80
  }
}