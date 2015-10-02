package uf.edu.mebin.dist

import scala.io.StdIn
import uf.edu.mebin.topology.NetworkFactory
import uf.edu.mebin.topology.Network

/**
 * @author mebin
 */

object DistibutedApp {
  var noOfNodes:Int = _
  var networkTopologyInst:Network = null 
  def main(args: Array[String]): Unit = {
    // starting 2 frontend nodes 
    println("Please enter the number of nodes  : ")
    noOfNodes = StdIn.readInt()
    println("Please enter the type of topology  : ")
    val topology: String = StdIn.readLine()
    println("Please enter the algorithm!")
    val algorithm = StdIn.readLine
    networkTopologyInst = new NetworkFactory(topology).getInstance(noOfNodes)
    Boss.start(Seq("2551",algorithm).toArray, networkTopologyInst, noOfNodes)
    //Start the workers
    for (x <- 1 to noOfNodes) {
      Worker.start(x-1)
    }

  }

}