package uf.edu.mebin.dist

import scala.util.Random
import com.typesafe.config.ConfigFactory
import akka.actor.Actor
import akka.actor.ActorSelection.toScala
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.RootActorPath
import uf.edu.mebin.algo.AlgoFactory
import uf.edu.mebin.algo.Algorithm
import akka.actor.ActorRef
import java.io.PrintWriter
import java.io.File

class Worker(actorNo: Int, boss: ActorRef) extends Actor {
  var sent: Boolean = false
  var algo: Algorithm = null
  var timeout = null
  val neighboursArray = DistibutedApp.networkTopologyInst.getListOfNeighbours(actorNo)

  def receive = {
    case Stop => context.system.shutdown()
    case PushSumMsg =>
      algo = AlgoFactory.getInstance("push-sum", actorNo)
    case GossipMsg =>
      algo = AlgoFactory.getInstance("gossip", actorNo)
    case msg: Message =>
      processMessage(algo, msg)
      if (sent == false) {
       boss ! MessageReceived(actorNo)
        sent = true
      }
    case workerRegister => boss ! BackendRegistration(actorNo)
  }

  def processMessage(algo: Algorithm, msg: Message) = {
    if (algo != null) {
      algo.receiveMessage(msg)
      if (algo.isTerminate() == true) {

        if (boss != null){
          boss ! Stop
          context.system.shutdown()
        }
      }
      
      var randomNeighbour = getRandomNeighbour(neighboursArray)
      println("\nThe current worker is : ",actorNo)
      println("Neighbour selected ",neighboursArray(randomNeighbour))
      algo.send(workerActors.actors(neighboursArray(randomNeighbour)))
    } else
      throw new Exception("Algorithm has not been specified!!!")
  }

  def getRandomNeighbour(neighbours: List[Int]): Int = {
    var rando = new Random().nextInt(neighbours.length)
    rando
  }

}

object Worker {
  def start(n: Int, boss: ActorRef): Unit = {
    val system = ActorSystem() //"ClusterSystem", config
    for(workerNo <- 1 to n){
      var workerActor = system.actorOf(Props(new Worker(workerNo - 1, boss)), name = "worker"+workerNo)  
      workerActor ! workerRegister
    }
  }
}