package uf.edu.mebin.algo

/**
 * @author mebin
 */
object AlgoFactory {
  def getInstance(name:String, actorNum: Int) = {
    name match {
      case "push-sum" => new PushSum(actorNum) 
      case "gossip" => new Gossip(actorNum)
      case _ => throw new Exception("Invalid algorithm") 
    }
  }
}