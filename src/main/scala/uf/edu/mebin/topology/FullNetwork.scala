package uf.edu.mebin.topology
import scala.collection.mutable.ListBuffer

/**
 * @author mebin
 */
// n is the number of actors involved!!
case class fullNetwork(n: Int) extends Network {
  //NxN matrix to store to whom all it can connect... 
  //val connectedNetwork = Array.ofDim[Boolean](n, n)
  
  override def getNoOfNodes(): Int = {
    n
  }
  override def getListOfNeighbours(node: Int): List[Int] = {
    /*val neighbours = new ListBuffer[Int]()    
    for(i <- 0 to n-1){
      if(node != i){
        neighbours += i        
      }
    }
    println("Sending Neighbours")
    neighbours.toList*/
    null
  }
}