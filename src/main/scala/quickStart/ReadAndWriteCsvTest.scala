package quickStart

import com.alibaba.alink.operator.batch.sink.CsvSinkBatchOp
import com.alibaba.alink.operator.batch.source.CsvSourceBatchOp

object ReadAndWriteCsvTest {
  def main(args: Array[String]): Unit = {
    val filePath = domain.DataPathConstants.IRIS
    val schemaStr = "sepal_length double, sepal_width double, petal_length double, petal_width double, category string"

    //读取
    val source = new CsvSourceBatchOp()
      .setFilePath(filePath)
      .setSchemaStr(schemaStr)

    source.firstN(10).print()

    //写入
    source.link(
      new CsvSinkBatchOp()
        .setFilePath(domain.BasicConstants.DATA_PATH + "sinkIris.csv")
        .setOverwriteSink(true)
    )

  }
}
