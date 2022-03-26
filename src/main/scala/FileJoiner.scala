import scala.collection.mutable

class FileJoiner() {

  def mergeRows(firstFileElements: Array[String], secondFileElements: Array[String], joiningColumnIndex: Int): Unit = {
    val resultRow = firstFileElements.mkString(",") ++ new String(",") ++ secondFileElements.filter(elem => !elem.equals(secondFileElements(joiningColumnIndex))).mkString(",") ++ new String(s"\n")
    os.write.append(CSVJoinerConf.filesPaths / "result.csv", resultRow)
  }

  def addLeftRightJoinRow(firstFileElements: Array[String], secondFileCellNumber: Int):  Unit = {
    val firstFileElementsBuffer = firstFileElements.toBuffer
    val resultRow = firstFileElementsBuffer.mkString(",") ++ ",".repeat(secondFileCellNumber - 1) ++ new String(s"\n")
    os.write.append(CSVJoinerConf.filesPaths / "result.csv", resultRow)
  }

  def getJoinColumnIndex(firstFile: String, secondFile: String, columnName: String): Option[(Int, Int)] = {
    val firstHeader = os.read.lines.stream(CSVJoinerConf.filesPaths / firstFile).head
    val secondHeader = os.read.lines.stream(CSVJoinerConf.filesPaths / secondFile).head

    val firstHeaderElements = firstHeader.split(',')
    val secondHeaderElements = secondHeader.split(',')
    (firstHeaderElements.indexOf(columnName), secondHeaderElements.indexOf(columnName)) match {
      case (-1, -1) => None
      case (-1, _) => None
      case (_, -1) => None
      case (index1, index2) => Some((index1, index2))
    }
  }

  def findSecondFileMatchingRows(firstFileElements: Array[String], joiningColumnValue: String, secondFile: String, joiningColumnIndex: Int, outer: Boolean, isHeader: Boolean): Unit = {
    val outerStack = mutable.Stack[Boolean]()
    os.read.lines.stream(CSVJoinerConf.filesPaths / secondFile).foreach(line => {
      val rowElements = line.split(',')
      if(joiningColumnValue.equals(rowElements(joiningColumnIndex))) {
        //if(nonInnerStack.size < 2) {
          mergeRows(firstFileElements, rowElements, joiningColumnIndex)
          if(outerStack.isEmpty) outerStack.append(true)
        //} else throw new RuntimeException
      }
    })

    if(outerStack.isEmpty && outer && !isHeader) addLeftRightJoinRow(firstFileElements, os.read.lines.stream(CSVJoinerConf.filesPaths / secondFile).head.split(",").length)
  }

  def processTheFirstFile(firstFileName: String, joiningColumnName: String, secondFileName: String, outer: Boolean): Unit = {
    getJoinColumnIndex(firstFileName, secondFileName, joiningColumnName) match {
      case Some((index1, index2)) =>
        val firstHeader = os.read.lines.stream(CSVJoinerConf.filesPaths / firstFileName).head
        os.read.lines.stream(CSVJoinerConf.filesPaths / firstFileName).foreach( row => {
          val rowElements = row.split(',')
          findSecondFileMatchingRows(rowElements, rowElements(index1), secondFileName, index2, outer, isHeader = row.equals(firstHeader))
        })

      case None => throw new NoSuchElementException
    }
  }

  def mergeFiles(firstFileName: String, secondFileName: String, columnName: String, joinType: String): Unit = {
    joinType match {
      case "inner" => processTheFirstFile(firstFileName, columnName, secondFileName, outer = false)
      case "left" => processTheFirstFile(firstFileName, columnName, secondFileName, outer = true)
      case "right" => processTheFirstFile(secondFileName, columnName, firstFileName, outer = true)
      case _ => throw new IllegalArgumentException
    }
  }
}