package uf.edu.mebin.topology
import scala.collection.mutable.ListBuffer

case class ThreeDGrid(n: Int) extends Network {
  val connectedNetwork = Array.ofDim[Boolean](n, n, n)
  override def getListOfNeighbours(node: Int): List[Int] = {
    var neighbours = new ListBuffer[Int]()
    var gridPhase:Int = node/(n/3) //To identify the plane in the 3d Grid
    var row:Int = math.sqrt(n/3).toInt
    var col:Int = math.sqrt(n/3).toInt
    var curRow = math.ceil((node - (gridPhase*row*col-1)) / row.toDouble).toInt
    var curCol = node - ((gridPhase*row*col-1) + ((curRow-1)*row))
    /*println("Actor num is :",node)
    println("Current row is : ",curRow)
    println("Current Columns is : ",curCol)
    println("GridPhase is :",gridPhase)
      */  
    if (gridPhase == 0){
      neighbours += ((curRow-1)*row) + curCol      
    }
    else if(gridPhase==2){
      neighbours += ((gridPhase-1)*row*col-1) + ((curRow-1)*row) + curCol      
    }
    else
    {
      neighbours += ((curRow-1)*row) + curCol
      neighbours += ((gridPhase+1)*row*col-1) + ((curRow-1)*row) + curCol
    }
    //For x & y
    
    if(curRow==1){
      neighbours += (gridPhase*row*col-1) + (row + curCol) 
    }
    else if (curRow==row){
      neighbours += (gridPhase*row*col-1) + ((curRow-2)*row) + curCol
     }
    else
    {
      neighbours += (gridPhase*row*col-1) + curCol
      neighbours += (gridPhase*row*col-1) + (curRow*row) + curCol
    }
    
    if (curCol==1){
      neighbours += (gridPhase*row*col-1) + ((curRow-1)*row) + curCol+1
    }
    else if (curCol==col){
      neighbours += (gridPhase*row*col-1) + ((curRow-1)*row) + curCol-1
    }
    else{
      neighbours += (gridPhase*row*col-1) + ((curRow-1)*row) + curCol-1
      neighbours += (gridPhase*row*col-1) + ((curRow-1)*row) + curCol+1
    }
    //println(neighbours)
    neighbours.toList
  }  
}