package uf.edu.mebin.algo

import akka.actor.ActorRef
import uf.edu.mebin.dist.Message

/**
 * @author mebin
 */
trait Algorithm {
  def send(actor: ActorRef)
  def receiveMessage(m: Message)
  def isTerminate():Boolean
}