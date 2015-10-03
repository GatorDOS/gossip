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
  val neighboursArray = DistibutedApp.networkTopologyInst.getListOfNeighbours(actorNo)

  def receive = {

    case PushSumMsg =>
      algo = AlgoFactory.getInstance("push-sum", actorNo)
    case GossipMsg =>
      println("Worker id is ", actorNo)
      println("The neighbours are: ")
      println(neighboursArray)
      println()
      algo = AlgoFactory.getInstance("gossip", actorNo)
    case msg: Message =>
      //println("Hey i was called",actorNo)
      processMessage(algo, msg)
      if (sent == false) {
      //println("******************Message Received by worker actor "+actorNo.toString() + "******************")
       /* val workerResult = new PrintWriter(new File("worker.txt" + actorNo))
        workerResult.close()*/
       boss ! MessageReceived(actorNo)
        sent = true
      }
    case workerRegister => boss ! BackendRegistration(actorNo)
  }

  def processMessage(algo: Algorithm, msg: Message) = {
    if (algo != null) {
      algo.receiveMessage(msg)
      if (algo.isTerminate() == true) {

        if (boss != null)
          boss ! Stop
      }
      var randomNeighbour = getRandomNeighbour(neighboursArray)
      println("Neighbour selected ",neighboursArray(randomNeighbour))
      algo.send(workerActors.actors(neighboursArray(randomNeighbour)))
    } else
      throw new Exception("Algorithm has not been specified!!!")
  }

  def getRandomNeighbour(neighbours: List[Int]): Int = {
    val rand = new Random()
    println(neighbours.length)
    var rando = rand.nextInt(neighbours.length)
    println(neighbours)
    println("The random index selected was ",rando)
    rando
  }

}

object Worker {
  def start(n: Int, boss: ActorRef): Unit = {
    val system = ActorSystem() //"ClusterSystem", config
    val workerActor = system.actorOf(Props(new Worker(n, boss)), name = "worker")
    workerActor ! workerRegister
  }
}