class FileJoinerTest extends BaseServiceTest {

  "FileJoiner" when {
    "performJoinOperation" should {

      "throw an IllegalArgumentException exception" in new Context {
        assertThrows[IllegalArgumentException] {
          fileJoiner.performJoinOperation(firstFile, secondFile, columnName, "")
        }
      }
    }

    "processTheFirstFile functionalities" should {

    }

    "findSecondFileMatchingRows functionalities" should {

    }

    "getJoinColumnIndex functionalities" should {

    }

    "addLeftRightJoinRow functionalities" should {

    }

    "mergeRows functionalities" should {

    }
  }
  trait Context {

    val fileJoiner: FileJoiner = new FileJoiner

    val firstFile: String = "a.csv"
    val secondFile: String = "b.csv"
    val columnName = "column"
  }

}
