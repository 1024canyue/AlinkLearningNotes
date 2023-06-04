package practice

import com.alibaba.alink.operator.batch.source.CsvSourceBatchOp

object LazyFunction {
  def main(args: Array[String]): Unit = {
    val filePath = domain.DataPathConstants.IRIS
    val schemaStr = "sepal_length double, sepal_width double, petal_length double, petal_width double, category string"
    val data:CsvSourceBatchOp = new CsvSourceBatchOp().setFilePath(filePath).setSchemaStr(schemaStr)

    data.initializeDataSource()
  }
}
