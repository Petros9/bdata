import os.Path

object CSVJoinerConf {
  val filesPaths: Path = os.pwd / "src" / "main" / "resources"
  val resultFileName: String = "result.csv"
  val inner: String = "inner"
  val left: String = "left"
  val right: String = "right"
  val csvDelimiter: String = ","
}
