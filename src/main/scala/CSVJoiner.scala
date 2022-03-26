import java.nio.file.NoSuchFileException
import java.util.NoSuchElementException

object CSVJoiner {
  def main(args : Array[String]): Unit = {
    val firstFile = args(0)
    val secondFile = args(1)
    val columnName = args(2)
    val joinType = args(3)
    val fileJoiner: FileJoiner = new FileJoiner

    try {
      fileJoiner.mergeFiles(firstFile, secondFile, columnName, joinType)
    }
    catch {
      case _: NoSuchFileException => println("There is no file with such name!")
      case _: IllegalArgumentException => println("Wrong join type, only allowed: \"inner\", \"left\" and \"right\"!")
      case _: NoSuchElementException => println("Wrong column name, at least one of the files does not contains such column!")
      case _: RuntimeException => println("Joining column is not unique in its values!")
    }
  }
}
