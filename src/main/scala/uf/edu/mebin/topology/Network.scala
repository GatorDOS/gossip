package uf.edu.mebin.topology

/**
 * @author mebin
 */
trait Network {
  /*
   * get one random actor out of the connected ones of node Actor!!
   * @return: the random actor number
   */
  def getListOfNeighbours(node: Int): Array[Int]
    
}