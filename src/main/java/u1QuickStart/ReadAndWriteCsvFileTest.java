package u1QuickStart;

import com.alibaba.alink.operator.batch.BatchOperator;
import com.alibaba.alink.operator.batch.sink.CsvSinkBatchOp;
import com.alibaba.alink.operator.batch.source.CsvSourceBatchOp;
import domain.BasicConstants;

public class ReadAndWriteCsvFileTest {
    public static void main(String[] args) throws Exception {
        String filePath = domain.DataPathConstants.IRIS;
        String schemaStr = "sepal_length double, sepal_width double, petal_length double, petal_width double, category string";

        //Alink读取CSV文件，为后续批训练做准备
        CsvSourceBatchOp source = new CsvSourceBatchOp()
                .setFilePath(filePath)
                .setSchemaStr(schemaStr);
        source.firstN(10).print();

        //写入
        source
                .link(new CsvSinkBatchOp()
                        .setFilePath(BasicConstants.DATA_PATH + "sinkIris.csv")
                        .setOverwriteSink(true)
                );
        BatchOperator.execute();
    }
}
