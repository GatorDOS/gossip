package uf.edu.mebin.dist

import akka.actor.ActorRef

/**
 * @author mebin
 */

trait Message
case class PushSumMessage(s: BigDecimal, w:BigDecimal) extends Message
case class GossipMessage(s: String) extends Message
case class BackendRegistration(actorNo: Int)
final case class JobFailed(reason: String, job: Array[Int])
case object PushSumMsg
case object GossipMsg
case object Stop
case object workerRegister
case class MessageReceived(n: Int)
object workerActors{
  lazy val actors: Array[ActorRef] = new Array(DistibutedApp.noOfNodes)
}
