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
class Boss(topology: Network, algo: String, noOfNodes: Int) extends Actor {
  var backends = IndexedSeq.empty[ActorRef]
  val workerResult = new PrintWriter(new File("worker.txt"))
  var firstActor: ActorRef = null
  var jobCounter = 0
  var b = 0l
  def receive = {
    case d: DeadLetter => println(d)
    case job: Array[Int] if backends.isEmpty =>
      sender() ! JobFailed("Service UnAvailable, try again later", job)
    //case job: Array[Int] =>
      jobCounter += 1
      backends(jobCounter % backends.size) forward job

    case BackendRegistration(num) if !backends.contains(sender()) =>
      print("Backend!!")
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
      println(s"The number of nodes are $backends.length")
      if (backends.length == noOfNodes) {
        b = System.currentTimeMillis()
        if (algo == "push-sum")
          firstActor ! PushSumMessage(0, 0)
        else if (algo == "gossip")
          firstActor ! GossipMessage("fact")
      }
    case Terminated(a) =>
      backends = backends.filterNot(_ == a)

    case Stop =>
      val difference = (System.currentTimeMillis() - b)
      val pw = new PrintWriter(new File("output.txt"))
      pw.write(s"The time taken is " + difference.toString())
      pw.close
      println("Time taken is " + difference)
      println("Going to shutdown!!!")
      stopAllWorkers
      workerResult.close()
      context stop self
    case MessageReceived(n) => 
      println(s"Message received from $n \n")
      workerResult.write(s"Message received from $n \n")
  }

  def stopAllWorkers() = {
    for (worker <- workerActors.actors) {
      worker ! PoisonPill
    }
  }
}
//Boss needs to start the gossip..
object Boss {
  def start(args: Array[String], topology: Network, noOfNodes: Int): ActorRef = {
    //Override the configuration of the port when specified as program argument
    val port = if (args.isEmpty) "0" else args(0)
    val algo = if (args.isEmpty) "push-sum" else args(1)
    val system = ActorSystem()
    system.actorOf(Props(new Boss(topology, algo, noOfNodes)), name = "Boss")
  }
}