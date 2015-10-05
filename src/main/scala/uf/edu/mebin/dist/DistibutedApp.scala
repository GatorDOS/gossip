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
    noOfNodes = args(0).toInt
    val topology: String = args(1)
    val algorithm = args(2)
    networkTopologyInst = new NetworkFactory(topology).getInstance(noOfNodes)
    
    var killTime:Int = -1
    var DiseasedNode: Int = -1
    println("Args length is :",args.length)
    if(args.length==5)
    {
       killTime = args(3).toInt
       DiseasedNode = args(4).toInt
     }
    println("Kill Time is ",killTime)
    println("Diseased Node is ", DiseasedNode)
    
    val boss = Boss.start(Seq("2551",algorithm).toArray, networkTopologyInst, noOfNodes, killTime, DiseasedNode)
    Worker.start(noOfNodes, boss)
  }

}