package uf.edu.mebin.dist

import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import java.util.concurrent.atomic.AtomicInteger
import scala.concurrent.duration.DurationInt
import com.typesafe.config.ConfigFactory
import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.DeadLetter
import akka.actor.Props
import akka.actor.Terminated
import akka.actor.actorRef2Scala
import akka.pattern.ask
import akka.util.Timeout
import uf.edu.mebin.topology.Network
import akka.actor.PoisonPill
/**
 * @author mebin
 */
class Boss(topology: Network, algo: String) extends Actor {
  var backends = IndexedSeq.empty[ActorRef]
  var firstActor: ActorRef = null
  var jobCounter = 0

  def receive = {
    case d: DeadLetter => println(d)
    case job: Array[Int] if backends.isEmpty =>
      sender() ! JobFailed("Service UnAvailable, try again later", job)
    case job: Array[Int] =>
      jobCounter += 1
      backends(jobCounter % backends.size) forward job

    case BackendRegistration(num) if !backends.contains(sender()) =>
      context watch sender()
      workerActors.actors(num) = sender()
      backends = backends :+ sender()
      sender() ! topology
      if (num == 1)
        firstActor = sender()
      if (algo == "push-sum")
        sender() ! PushSumMsg
      else if (algo == "gossip")
        sender() ! GossipMsg
      else
        throw new Exception("Please enter \"push-sum\" or \"gossip\" for algorithm")

    case Terminated(a) =>
      backends = backends.filterNot(_ == a)
      
    case Stop => stopAllWorkers
                 context stop self
  }
  
  def stopAllWorkers() = {
    for(worker <- workerActors.actors){
      worker ! PoisonPill
    }
  }
}
//Boss needs to start the gossip..
object Boss {
  def start(args: Array[String], topology: Network, noOfNodes: Int): Unit = {
    //Override the configuration of the port when specified as program argument
    val port = if (args.isEmpty) "0" else args(0)
    val algo = if (args.isEmpty) "push-sum" else args(1) //default is push sum
    val config = ConfigFactory.parseString(s"akka.remote.netty.tcp.port=$port").
      withFallback(ConfigFactory.parseString("akka.cluster.roles = [Boss]")).
      withFallback(ConfigFactory.load())

    val system = ActorSystem("ClusterSystem", config)
//    val bossInstance = new Boss(topology, algo)
    val boss = system.actorOf(Props(new Boss(topology, algo)), name = "Boss")
//    system.eventStream.subscribe(boss, classOf[DeadLetter])
    val bossactorRef = boss.actorRef.path
    if (algo == "push-sum") {
      //send message ti fust actor
      while (bossactorRef.backends.length != noOfNodes) {} //wait till all workers register
      bossactorRef.firstActor ! PushSumMessage(0, 0) //start
    }

  }

}