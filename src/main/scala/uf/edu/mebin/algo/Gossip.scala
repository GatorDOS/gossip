package uf.edu.mebin.algo

import uf.edu.mebin.dist.PushSumMessage
import akka.actor.ActorRef

/**
 * @author mebin
 */
class Gossip(actorNum:Int) extends Algorithm{
   var count = 0 //message received count
   def send(actor:ActorRef)={
  }
  
  def receiveMessage(m:PushSumMessage) = {
    count += 1
  }
  
  def isTerminate() = {
    count >= 10
  }
}