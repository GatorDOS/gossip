package uf.edu.mebin.topology

case class Line(n: Int) extends Network {
  val lineNetwork = Array.ofDim[Boolean](n)

  override def getListOfNeighbours(nodeNum: Int): Array[Int] = {
    var listOfNeighb: Array[Int] = null
    val actor = lineNetwork(nodeNum)
    if (nodeNum == 0) {
      listOfNeighb = new Array(1)
      listOfNeighb(0) = 1
    } else if (nodeNum == lineNetwork.length - 1) {
      listOfNeighb = new Array(1)
      listOfNeighb(0) = lineNetwork.length - 2
    } else {
      listOfNeighb = new Array(2)
      listOfNeighb(0) = nodeNum - 1
      listOfNeighb(1) = nodeNum + 1
    }
    listOfNeighb
  }
}