import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.mockito.scalatest.MockitoSugar
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import java.nio.file.NoSuchFileException

class FileJoinerTest extends AnyWordSpec {

  "FileJoiner" when {
    "performJoinOperation" should {

      "throw an IllegalArgumentException exception" in new Context {
        assertThrows[IllegalArgumentException] {
          fileJoiner.performJoinOperation(firstFile, secondFile, columnName, "")
        }
      }
    }

    "getJoinColumnIndex functionalities" should {
      "return None if no headers are shared" in new Context {
        assertResult(None)(getJoinColumnIndexFunctionalities(firstFileHeaders, thirdFileHeaders))
      }

      "return correct indexes if headers are shared" in new Context {
        assertResult(Some(Indexes(2, 2)))(getJoinColumnIndexFunctionalities(firstFileHeaders, secondFileHeaders))
      }
    }

    "processTheFirstFile functionalities" should {
      "throw an NoSuchFileException exception" in new Context {
        assertThrows[NoSuchFileException] {
          fileJoiner.processTheFirstFile(firstFile, secondFile, columnName, true)
        }
      }
    }


    "addLeftRightJoinRow functionalities" should {
      "create new result row" in new Context{
        assertResult(
          new String("City,Address,Number,,\n")
        )(firstFileHeaders.mkString(CSVJoinerConf.csvDelimiter) ++ CSVJoinerConf.csvDelimiter.repeat(3 - 1) ++ new String(s"\n"))
      }
    }

    "mergeRows functionalities" should {
      "merge two rows" in new Context {
        assertResult(
          new String("City,Address,Number,Surname,Name\n")
        )(new String(firstFileHeaders.mkString(CSVJoinerConf.csvDelimiter) ++ CSVJoinerConf.csvDelimiter ++ secondFileHeaders.filter(elem => !elem.equals(secondFileHeaders(2))).mkString(CSVJoinerConf.csvDelimiter) ++ new String(s"\n")))
      }
    }
  }
  trait Context {

    def getJoinColumnIndexFunctionalities(firstHeaderElements: Array[String], secondHeaderElements: Array[String]): Option[Indexes] ={
      (firstHeaderElements.indexOf(joiningColumnName), secondHeaderElements.indexOf(joiningColumnName)) match {
        case (-1, -1) => None
        case (-1, _) => None
        case (_, -1) => None
        case (index1, index2) => Some(Indexes(index1, index2))
      }
    }
    val fileJoiner: FileJoiner = new FileJoiner

    val firstFileHeaders: Array[String] = Array("City", "Address", "Number")
    val secondFileHeaders: Array[String] = Array("Surname", "Name", "Number")
    val thirdFileHeaders: Array[String] = Array("Name", "Surname", "Cat")
    val joiningColumnName: String = "Number"
    val firstFile: String = "a.csv"
    val secondFile: String = "b.csv"
    val columnName = "column"
  }

}
