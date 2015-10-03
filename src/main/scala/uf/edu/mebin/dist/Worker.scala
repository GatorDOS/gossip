package uf.edu.mebin.dist

import scala.annotation.varargs

import com.typesafe.config.ConfigFactory

import akka.actor.Actor
import akka.actor.ActorSelection.toScala
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.RootActorPath
import akka.cluster.Cluster
import akka.cluster.ClusterEvent.CurrentClusterState
import akka.cluster.ClusterEvent.MemberUp
import akka.cluster.Member
import akka.cluster.MemberStatus
import uf.edu.mebin.algo.AlgoFactory
import uf.edu.mebin.algo.Algorithm

class Worker(actorNo: Int) extends Actor {

  val cluster = Cluster(context.system)
  var algo: Algorithm = null
  var boss: Member = null

  override def preStart(): Unit = cluster.subscribe(self, classOf[MemberUp])
  override def postStop(): Unit = cluster.unsubscribe(self)

  def receive = {
    case state: CurrentClusterState =>
      state.members.filter(_.status == MemberStatus.Up) foreach register
    case MemberUp(m) => register(m)
    case PushSumMsg =>
      algo = AlgoFactory.getInstance("push-sum", actorNo)
    case GossipMsg =>
      algo = AlgoFactory.getInstance("gossip", actorNo)
    case msg: Message => processMessage(algo, msg)
  }

  def processMessage(algo: Algorithm, msg: Message) = {
    if (algo != null) {
      algo.receiveMessage(msg)
      if (algo.isTerminate() == true) {
        if (boss != null)
          context.actorSelection(RootActorPath(boss.address) / "user" / "Boss") ! Stop
      }
      var randomNeighbour = DistibutedApp.networkTopologyInst.getRandomNeighbour(actorNo)
      algo.send(workerActors.actors(randomNeighbour))
    } else
      throw new Exception("Algorithm has not been specified!!!")
  }

  def register(member: Member): Unit =
    if (member.hasRole("Boss")) {
      boss = member
      context.actorSelection(RootActorPath(member.address) / "user" / "Boss") ! BackendRegistration(actorNo)
    }

}

object Worker {
  def start(n: Int): Unit = {
    val config = ConfigFactory.parseString(s"akka.remote.netty.tcp.port=0").
      withFallback(ConfigFactory.parseString("akka.cluster.roles = [worker]")).
      withFallback(ConfigFactory.load())

    val system = ActorSystem("ClusterSystem", config)
    system.actorOf(Props(new Worker(n)), name = "worker")
  }
}