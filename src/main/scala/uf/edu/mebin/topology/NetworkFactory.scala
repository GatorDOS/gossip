package uf.edu.mebin.topology

/**
 * @author mebin
 */
class NetworkFactory(name:String) {
  var line:Line = null
  var fullNetwork:fullNetwork = null
  var threeDGrid:ThreeDGrid = null
  var impThreeDGrid:ImperfectThreeDGrid = null
  def getInstance(n : Int):Network={
    name match {
      case "fullNetwork" => if(fullNetwork != null) fullNetwork else new fullNetwork(n)
      case "line" => if(line != null) line else new Line(n)
      case "3DGrid" => if(threeDGrid != null) threeDGrid else new ThreeDGrid(n)
      case "Imp3DGrid" => if(impThreeDGrid != null) impThreeDGrid else new ImperfectThreeDGrid(n)
      case _ => throw new Exception("You screwed up!!")
    }
  }
}