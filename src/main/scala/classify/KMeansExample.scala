package classify

import com.alibaba.alink.operator.batch.BatchOperator
import com.alibaba.alink.operator.batch.source.CsvSourceBatchOp
import com.alibaba.alink.pipeline.Pipeline
import com.alibaba.alink.pipeline.clustering.KMeans
import com.alibaba.alink.pipeline.dataproc.vector.VectorAssembler

object KMeansExample {
  def main(args: Array[String]): Unit = {
    val filePath = domain.DataPathConstants.IRIS
    val schemaStr = "sepal_length double, sepal_width double, petal_length double, petal_width double, category string"
    val data = new CsvSourceBatchOp().setFilePath(filePath).setSchemaStr(schemaStr)
    val vectorAssembler = new VectorAssembler().setSelectedCols("sepal_length", "sepal_width", "petal_length", "petal_width")
      .setOutputCol("features")
    val kMeans = new KMeans()
        .setVectorCol("features")
        .setK(3)
        .setPredictionCol("prediction_result")
        .setPredictionDetailCol("prediction_detail")
        .setReservedCols("category")
        .setMaxIter(100)

    val pipeline = new Pipeline().add(vectorAssembler).add(kMeans)
    val relust:BatchOperator[_] = pipeline.fit(data).transform(data)
    relust.print()
  }
}
