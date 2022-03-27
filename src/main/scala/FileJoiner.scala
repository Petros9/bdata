import scala.collection.mutable

class FileJoiner() {

  def mergeRows(firstFileElements: Array[String], secondFileElements: Array[String], joiningColumnIndex: Int): Unit = {
    val resultRow = firstFileElements.mkString(CSVJoinerConf.csvDelimiter) ++ CSVJoinerConf.csvDelimiter ++ secondFileElements.filter(elem => !elem.equals(secondFileElements(joiningColumnIndex))).mkString(CSVJoinerConf.csvDelimiter) ++ new String(s"\n")
    os.write.append(CSVJoinerConf.filesPaths / CSVJoinerConf.resultFileName, resultRow)
  }

  def addLeftRightJoinRow(firstFileElements: Array[String], secondFileCellNumber: Int):  Unit = {
    val firstFileElementsBuffer = firstFileElements.toBuffer
    val resultRow = firstFileElementsBuffer.mkString(CSVJoinerConf.csvDelimiter) ++ CSVJoinerConf.csvDelimiter.repeat(secondFileCellNumber - 1) ++ new String(s"\n")
    os.write.append(CSVJoinerConf.filesPaths / CSVJoinerConf.resultFileName, resultRow)
  }

  def getJoinColumnIndex(firstFile: String, secondFile: String, columnName: String): Option[(Int, Int)] = {
    val firstHeader = os.read.lines.stream(CSVJoinerConf.filesPaths / firstFile).head
    val secondHeader = os.read.lines.stream(CSVJoinerConf.filesPaths / secondFile).head

    val firstHeaderElements = firstHeader.split(CSVJoinerConf.csvDelimiter)
    val secondHeaderElements = secondHeader.split(CSVJoinerConf.csvDelimiter)
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
      val rowElements = line.split(CSVJoinerConf.csvDelimiter)
      if(joiningColumnValue.equals(rowElements(joiningColumnIndex))) {
        //if(nonInnerStack.size < 2) {
          mergeRows(firstFileElements, rowElements, joiningColumnIndex)
          if(outerStack.isEmpty) outerStack.append(true)
        //} else throw new RuntimeException
      }
    })

    if(outerStack.isEmpty && outer && !isHeader) addLeftRightJoinRow(firstFileElements, os.read.lines.stream(CSVJoinerConf.filesPaths / secondFile).head.split(CSVJoinerConf.csvDelimiter).length)
  }

  def processTheFirstFile(firstFileName: String, joiningColumnName: String, secondFileName: String, outer: Boolean): Unit = {
    getJoinColumnIndex(firstFileName, secondFileName, joiningColumnName) match {
      case Some((index1, index2)) =>
        val firstHeader = os.read.lines.stream(CSVJoinerConf.filesPaths / firstFileName).head
        os.read.lines.stream(CSVJoinerConf.filesPaths / firstFileName).foreach( row => {
          val rowElements = row.split(CSVJoinerConf.csvDelimiter)
          findSecondFileMatchingRows(rowElements, rowElements(index1), secondFileName, index2, outer, isHeader = row.equals(firstHeader))
        })

      case None => throw new NoSuchElementException
    }
  }

  def mergeFiles(firstFileName: String, secondFileName: String, columnName: String, joinType: String): Unit = {
    joinType match {
      case CSVJoinerConf.inner => processTheFirstFile(firstFileName, columnName, secondFileName, outer = false)
      case CSVJoinerConf.left => processTheFirstFile(firstFileName, columnName, secondFileName, outer = true)
      case CSVJoinerConf.right => processTheFirstFile(secondFileName, columnName, firstFileName, outer = true)
      case _ => throw new IllegalArgumentException
    }
  }
}