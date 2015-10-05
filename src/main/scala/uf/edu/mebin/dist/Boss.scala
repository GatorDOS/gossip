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
import scala.io.StdIn
/**
 * @author mebin
 */
class Boss(topology: Network, algo: String, noOfNodes: Int, killTime:Int, DiseasedNode: Int) extends Actor {
  var backends = IndexedSeq.empty[ActorRef]
  val workerResult = new PrintWriter(new File("worker.txt"))
  var firstActor: ActorRef = null
  var jobCounter = 0
  var shutDownReq: Boolean = false
  var startTime = 0l
  var kTime = killTime
  def receive = {
    case d: DeadLetter => println(d)
    case job: Array[Int] if backends.isEmpty =>
      sender() ! JobFailed("Service UnAvailable, try again later", job)
    //case job: Array[Int] =>
      jobCounter += 1
      backends(jobCounter % backends.size) forward job

    case BackendRegistration(num) if !backends.contains(sender()) =>      
      print("\n Registering to Backend, Worker: "+sender().path.name)
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
      
      if (backends.length == noOfNodes) {
        startTime = System.currentTimeMillis()
        if (algo == "push-sum")
          firstActor ! PushSumMessage(0, 0)
        else if (algo == "gossip")
          firstActor ! GossipMessage("fact")
      }
    case Terminated(a) =>
      backends = backends.filterNot(_ == a)

    case Stop =>
      if(shutDownReq==false){
      val difference = (System.currentTimeMillis() - startTime)
      val pw = new PrintWriter(new File("output.txt"))
      pw.write(s"The time taken is " + difference.toString() + "\n")
      pw.write(s"Getting converged at "+ sender().path.name+"\n")
      pw.close
      println("Time taken is " + difference)
      println("Going to shutdown!!!")
      stopAllWorkers
      workerResult.close()
      context.system.shutdown()
      shutDownReq = true
      }
      
    case MessageReceived(n) => 
      println("Meessage received for actor:",n)
      if (kTime>0){
        var ctime = System.currentTimeMillis()
        if(ctime-startTime>kTime){
          val pw = new PrintWriter(new File("killer.txt"))
      pw.write(s"killed the actor " + DiseasedNode.toString() + "\n")
      pw.close
          
          kTime = -1
          workerActors.actors(DiseasedNode) ! StopMyActor          
        }
      }
      println(s"**************Message received from $n ****************\n")
      workerResult.write(s"Message received from $n \n")
  }

  def stopAllWorkers() = {
    for (worker <- workerActors.actors) {
      worker ! Stop
    }
  }
}
//Boss needs to start the gossip..
object Boss {
  def start(args: Array[String], topology: Network, noOfNodes: Int, killTime: Int, DiseasedNode: Int): ActorRef = {
    //Override the configuration of the port when specified as program argument
    val port = if (args.isEmpty) "0" else args(0)
    val algo = if (args.isEmpty) "push-sum" else args(1)
    val system = ActorSystem()
    system.actorOf(Props(new Boss(topology, algo, noOfNodes, killTime, DiseasedNode)), name = "Boss")
  }
}