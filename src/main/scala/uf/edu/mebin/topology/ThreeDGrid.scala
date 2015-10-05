package uf.edu.mebin.topology
import scala.collection.mutable.ListBuffer
import scala.io.StdIn

case class ThreeDGrid(n: Int) extends Network {
  
  var dimension = math.cbrt(n).toInt
  val connectedNetwork = Array.ofDim[Boolean](dimension, dimension, dimension)
  
  override def getNoOfNodes(): Int = {
    n
  }
  
  override def getListOfNeighbours(node: Int): List[Int] = {
  var neighbours = new ListBuffer[Int]()
    var gridPhase:Int = node/(dimension*dimension) + 1 //To identify the plane in the 3d Grid
    var row:Int = dimension
    var col:Int = dimension
    var curRow = math.ceil( (node -((gridPhase-1)*row*row-1))/row.toDouble ).toInt
    var diffRow = 0
    if(curRow==1){
      diffRow = -1
    }
    else{
      diffRow = (curRow-1)*row-1
    }
    var curCol = node - ( (gridPhase-1)*row*row + diffRow )
    if (gridPhase == 1){
      neighbours += node+(row*row)-1      
    }
    else if(gridPhase==row){
      neighbours += node-(row*row)      
    }
    else
    {
      neighbours += node-(row*row)
      neighbours += node+(row*row)-1
    }
    //For x & y
    
    if(curRow==1){
      neighbours += node + row 
    }
    else if (curRow==row){
      neighbours += node - row
     }
    else
    {
      neighbours += node+row
      neighbours += node-row
    }
    
    if (curCol==1){
      neighbours += node+1
    }
    else if (curCol==col){
      neighbours += node-1
    }
    else{
      neighbours += node+1
      neighbours += node-1
    }
    //println(neighbours)
    for(i <- 0 to neighbours.length-1)
    {
      if(neighbours(i)>=n)
      {
        println(neighbours,node)
        println("Found smth fishy in neighbours for actor: ",node)
        StdIn.readChar()
      }
    }
    neighbours.toList
  }  
}