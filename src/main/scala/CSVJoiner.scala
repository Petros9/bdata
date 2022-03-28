import java.nio.file.NoSuchFileException
import java.util.NoSuchElementException

object CSVJoiner {
  def main(args : Array[String]): Unit = {
    os.remove(CSVJoinerConf.filesPaths / CSVJoinerConf.resultFileName)
    val firstFile = args(0)
    val secondFile = args(1)
    val columnName = args(2)
    val joinType = args(3)
    val fileJoiner: FileJoiner = new FileJoiner

    if(firstFile.endsWith(".csv") && secondFile.endsWith(".csv")){
      try {
        fileJoiner.performJoinOperation(firstFile, secondFile, columnName, joinType)
      }
      catch {
        case _: NoSuchFileException => println("There is no file with such name!")
        case _: IllegalArgumentException => println("Wrong join type, only allowed: \"inner\", \"left\" and \"right\"!")
        case _: NoSuchElementException => println("Wrong column name, at least one of the files does not contains such column!")
      }
    } else println("Wrong file format!")
  }
}
