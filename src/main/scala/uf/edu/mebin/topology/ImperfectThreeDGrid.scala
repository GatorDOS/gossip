package uf.edu.mebin.topology

import scala.collection.mutable.ListBuffer
import scala.io.StdIn
import scala.util.Random

case class ImperfectThreeDGrid(totalNumOfNodes:Int) extends Network {
  
  var dimension = math.cbrt(totalNumOfNodes).toInt
  /*println("Dimension :",dimension)
  StdIn.readChar()*/
  val connectedNetwork = Array.ofDim[Boolean](dimension, dimension, dimension)
  
  override def getNoOfNodes(): Int = {
    totalNumOfNodes
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
    /*println(node, curRow ,curCol, gridPhase)
    StdIn.readChar()*/  
    if (gridPhase == 1){
      neighbours += node+16-1      
    }
    else if(gridPhase==row){
      neighbours += node-16      
    }
    else
    {
      neighbours += node-16
      neighbours += node+16-1
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
    println("The neighbours are " + neighbours + node.toString())
    val range = totalNumOfNodes
    var found:Boolean = true
    val rand = new Random()
    while(found){
      val ind = rand.nextInt(range)
      if(ind>range)
      {
        println("##############################Exceeded##############################")
        println("The random numbers:",ind)
        //StdIn.readInt()
      }
      if(neighbours.contains(ind%range)){
      }
      else{
        neighbours += ind%range
        found = false
      }
    }
    //println(neighbours)
    for(i <- 0 to neighbours.length-1)
    {
      if(neighbours(i)>=totalNumOfNodes)
      {        
        println(neighbours)
        println("Found smth fishy in neighbours for actor: ",node)
        println(curRow,curCol)
        StdIn.readChar()
      }
    }
    //println(neighbours)
    neighbours.toList
  }
}