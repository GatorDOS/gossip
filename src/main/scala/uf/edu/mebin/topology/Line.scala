package uf.edu.mebin.topology

case class Line(n: Int) extends Network {
  val lineNetwork = Array.ofDim[Boolean](n)

  override def getRandomNeighbour(nodeNum: Int): Int = {
    val random = scala.util.Random
    val actor = lineNetwork(nodeNum)
    if (nodeNum == 1) {
      2
    } else if (nodeNum == lineNetwork.length - 1) {
      lineNetwork.length - 2
    } else {
      val randomIndex = random.nextInt(2)
      if (randomIndex == 0 ) nodeNum - 1
      else
        nodeNum + 1
    }

  }
}