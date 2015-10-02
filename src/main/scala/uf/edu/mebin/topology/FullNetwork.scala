package uf.edu.mebin.topology

/**
 * @author mebin
 */
// n is the number of actors involved!!
case class fullNetwork(n: Int) extends Network {
  //NxN matrix to store to whom all it can connect... 
  val connectedNetwork = Array.ofDim[Boolean](n, n)
  override def getRandomNeighbour(node: Int): Int = {
    val connectedActors = connectedNetwork(node)
    val random = scala.util.Random
    var randomIndex = random.nextInt(connectedActors.length)
    while (connectedActors(randomIndex) == true) {
      randomIndex = random.nextInt(connectedActors.length)
    }
    connectedActors(randomIndex) = true
    randomIndex
  }
}