package uf.edu.mebin.topology
import scala.collection.mutable.ListBuffer

case class Line(n: Int) extends Network {
  val lineNetwork = Array.ofDim[Boolean](n)

  override def getListOfNeighbours(nodeNum: Int): List[Int] = {
    var listOfNeighb = new ListBuffer[Int]()
    val actor = lineNetwork(nodeNum)
    if (nodeNum == 0) {
      listOfNeighb += 1      
    } else if (nodeNum == lineNetwork.length - 1) {
      listOfNeighb += lineNetwork.length - 2
    } else {
      listOfNeighb += nodeNum - 1
      listOfNeighb += nodeNum + 1
    }
    listOfNeighb.toList
  }
}