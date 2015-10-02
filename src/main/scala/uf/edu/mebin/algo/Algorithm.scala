package uf.edu.mebin.algo

import uf.edu.mebin.dist.PushSumMessage
import akka.actor.ActorRef

/**
 * @author mebin
 */
trait Algorithm {
  def send(actor: ActorRef)
  def receiveMessage(m: PushSumMessage)
  def isTerminate():Boolean
}