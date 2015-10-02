package uf.edu.mebin.topology

/**
 * @author mebin
 */
class NetworkFactory(name:String) {
  var line:Line = null
  var fullNetwork:fullNetwork = null
  def getInstance(n : Int):Network={
    name match {
      case "fullNetwork" => if(fullNetwork != null) fullNetwork else new fullNetwork(n)
      case "line" => if(line != null) line else new Line(n)
      case _ => throw new Exception("You screwed up!!")
    }
  }
}