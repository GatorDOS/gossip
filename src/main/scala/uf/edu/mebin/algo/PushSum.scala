package uf.edu.mebin.algo

import akka.actor.Actor
import akka.actor.ActorRef
import uf.edu.mebin.dist.PushSumMessage

/**
 * @author mebin
 */
class PushSum(actorNo: Int) extends Algorithm{
  var s:Float = actorNo
  var w:Float = 1.0f
  var previous:Float = 0.0f
  var count:Int = 0
  
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
    println("sum estimate " + s/w)
    return s/w
  }
  
  //Should it terminate
  def isTerminate():Boolean = {
    if(previous != 0){
       if(previous - sumEstimate() <= 0.0)
         {
           count +=1
           if(count >= 3){
             println("count tested")
             return true
           }
             
         }
       else{
         count = 0
       }
    }
    println("count is  " + count)
    println(previous - sumEstimate() + "is the diff")
    previous = sumEstimate()
    
    false
  }
}