package uf.edu.mebin.algo

import akka.actor.Actor
import akka.actor.ActorRef
import uf.edu.mebin.dist.PushSumMessage

/**
 * @author mebin
 */
class PushSum(actorNo: Int) extends Algorithm{
  var s = actorNo
  var w = 1
  
 
  
  def send(actor:ActorRef)={
    s = s/2;
    w = w/2;
    actor ! PushSumMessage(s,w)
  }
  
  def receiveMessage(m:PushSumMessage) = {
    s += m.s
    w += m.w
    
    //select a random neighbour and send it a message
  }
  
  def sumEstimate(): Float = {
    return s/w
  }
  
  //Should it terminate
  def isTerminate() = {
    sumEstimate() <= 0.0000000001
  }
}