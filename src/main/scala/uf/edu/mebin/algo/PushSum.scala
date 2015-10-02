package uf.edu.mebin.algo

import akka.actor.Actor
import akka.actor.ActorRef
import uf.edu.mebin.dist.PushSumMessage

/**
 * @author mebin
 */
class PushSum(actorNo: Int) extends Algorithm {
  var s: BigDecimal = actorNo
  var w: BigDecimal = 1.0f
  var previous: BigDecimal = 0.0f
  var count: Int = 0

  def send(actor: ActorRef) = {
    s = s / 2;
    w = w / 2;
    actor ! PushSumMessage(s, w)
  }

  def receiveMessage(m: PushSumMessage) = {
    s += m.s
    w += m.w
    //select a random neighbour and send it a message
  }

  def sumEstimate(): BigDecimal = {
    return s / w
  }

  //Should it terminate
  def isTerminate(): Boolean = {
    val threshold:BigDecimal = BigDecimal(0.0000000001)
    if (previous != 0) {
      var diff:BigDecimal =  previous - sumEstimate()
      if (diff.compare(threshold) == -1) {
        print("its inside it")
        count += 1
        if (count >= 3) {
          return true
        }
      } else {
        count = 0
      }
    }
    
    previous = sumEstimate()
    false
  }
}