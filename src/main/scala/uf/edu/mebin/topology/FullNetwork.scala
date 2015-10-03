package uf.edu.mebin.topology

/**
 * @author mebin
 */
// n is the number of actors involved!!
case class fullNetwork(n: Int) extends Network {
  //NxN matrix to store to whom all it can connect... 
  val connectedNetwork = Array.ofDim[Boolean](n, n)
  override def getListOfNeighbours(node: Int): Array[Int] = {
    val neighbours = Array(n-1)
    var j = 0
    for(i <- 0 to n){
      if(node != i){
        neighbours(j) = i
        j += 1
      }
    }
    neighbours
  }
}