package uf.edu.mebin.dist

import akka.actor.ActorRef

/**
 * @author mebin
 */

case class PushSumMessage(s: Float, w:Float)
case class BackendRegistration(actorNo:Int)
final case class JobFailed(reason: String, job: Array[Int])
case object PushSumMsg
case object GossipMsg
case object Stop
object workerActors{
  lazy val actors: Array[ActorRef] = new Array(DistibutedApp.noOfNodes)
}
