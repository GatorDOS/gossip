package uf.edu.mebin.dist

import scala.util.Random
import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.ActorSelection.toScala
import akka.actor.ActorSystem
import akka.actor.Cancellable
import akka.actor.Props
import akka.event.LoggingReceive
import uf.edu.mebin.algo.AlgoFactory
import uf.edu.mebin.algo.Algorithm
import scala.concurrent.duration._

class Worker(actorNo: Int, boss: ActorRef) extends Actor {
  var sent: Boolean = false
  var algo: Algorithm = null
  var timeout = null
  val totNoNodes:Int = DistibutedApp.networkTopologyInst.getNoOfNodes() 
  val neighboursArray = DistibutedApp.networkTopologyInst.getListOfNeighbours(actorNo)

  def receive = {
    case Stop => context.system.shutdown()
    case PushSumMsg =>
      algo = AlgoFactory.getInstance("push-sum", actorNo)
    case GossipMsg =>
      algo = AlgoFactory.getInstance("gossip", actorNo)
    
    case msg: Message =>
      processMessage(algo, msg)/*
      timeout = context.system.scheduler.scheduleOnce(1000 milliseconds, self, Down)
      context become waitingForAck(sender, timeout, msg)*/
      if (sent == false) {
       boss ! MessageReceived(actorNo)
        sent = true
      }
    case workerRegister => boss ! BackendRegistration(actorNo)
  }
  
  
  private def waitingForAck(origin: ActorRef, timeout: Cancellable, msg: Message): Receive = LoggingReceive {
  case Acknowledgment =>
    timeout.cancel()
    context become receive
  case Down =>
    var sendBy = sender.path.name
    var neighbour = sendBy.subSequence(6,sendBy.length+1)
    println(neighbour)
    self ! msg
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
   
      if(neighboursArray==null){
        var randomNeighbour = getNeighbour(totNoNodes-1)
        algo.send(workerActors.actors(randomNeighbour))        
      }
      else{      
      var randomNeighbour = getRandomNeighbour(neighboursArray)
      println("\nThe current worker is : ",actorNo)
      println("Neighbour selected ",neighboursArray(randomNeighbour))
      algo.send(workerActors.actors(neighboursArray(randomNeighbour)))
      
      }
    } else
      throw new Exception("Algorithm has not been specified!!!")
  }

  def getNeighbour(n:Int)={
    var rando:Int = new Random().nextInt(n)
    while(rando==actorNo)
    {
      rando = new Random().nextInt(n)
    }    
    rando
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