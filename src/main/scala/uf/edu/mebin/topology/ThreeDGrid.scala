package uf.edu.mebin.topology

import scala.collection.immutable.Nil
import scala.xml.Null

case class ThreeDGrid(n: Int) extends Network {
  val connectedNetwork = Array.ofDim[Boolean](n, n, n)
  override def getListOfNeighbours(node: Int): Array[Int] = {
    var neighbours: Array[Int] = null
    var gridPhase:Int = node/(n/3) //To identify the plane in the 3d Grid
    var row:Int = math.sqrt(n/3).toInt
    var col:Int = math.sqrt(n/3).toInt
    var curRow = (node - (gridPhase+1)*(row*col-1)) % (row)
    var curCol = node - ((gridPhase+1)*(row*col-1) + ((curRow+1)*row))
    var z:Int = 0
    neighbours = new Array(6)
    if (gridPhase == 0){
      neighbours(4) = (gridPhase+1)*(row*col-1) + (curRow*row) + curCol
      neighbours(5) = (gridPhase+1)*(row*col-1) + (curRow*row) + curCol
    }
    else if(gridPhase==2){
      neighbours(4) = (gridPhase-1)*(row*col-1) + (curRow*row) + curCol
      neighbours(5) = (gridPhase+1)*(row*col-1) + (curRow*row) + curCol
    }
    else
    {
      neighbours(4) = (curRow*row) + curCol
      neighbours(5) = (gridPhase+1)*(row*col-1) + (curRow*row) + curCol
    }
    //For x & y
    var TempNetworkTopology:Network = null
    TempNetworkTopology = new NetworkFactory("line").getInstance(n/3)
    
    if(curRow==0){
      neighbours(0) = (gridPhase*(row*col-1)) + (row + curCol) 
    }
    else if (curRow==row-1){
      neighbours(0) = (gridPhase*(row*col-1)) + ((curRow-1)*row) + curCol
     }
    else
    {
      neighbours(0) = (gridPhase*(row*col-1)) + ((curRow-1)*row) + curCol
      neighbours(1) = (gridPhase*(row*col-1)) + ((curRow+1)*row) + curCol
    }
    
    if (curCol==0){
      neighbours(2) = (gridPhase*(row*col-1)) + (curRow*row) + curCol+1
    }
    else if (curCol==col-1){
      neighbours(2) = (gridPhase*(row*col-1)) + (curRow*row) + curCol-1
    }
    else{
      neighbours(2) = (gridPhase*(row*col-1)) + (curRow*row) + curCol-1
      neighbours(3) = (gridPhase*(row*col-1)) + (curRow*row) + curCol+1
    }
    
    neighbours
  }  
}