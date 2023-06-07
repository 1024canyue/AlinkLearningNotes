package u2CoreConcept;

import com.alibaba.alink.operator.batch.dataproc.FirstNBatchOp;
import com.alibaba.alink.operator.batch.source.CsvSourceBatchOp;
import com.alibaba.alink.operator.batch.sql.FilterBatchOp;
import com.alibaba.alink.operator.batch.sql.SelectBatchOp;

/**
 * Link，LinkTo，LinkForm的顺序
 */
public class LinkTest {
    public static void main(String[] args) throws Exception {
        String filePath = domain.DataPathConstants.IRIS;
        String schemaStr = "sepal_length double, sepal_width double, petal_length double, petal_width double, category string";
        //鸢尾花数据集
        CsvSourceBatchOp data = new CsvSourceBatchOp(filePath, schemaStr);

        data
                .link(  //选择几个字段
                        new SelectBatchOp("sepal_length, sepal_width, category")
                ).link( //只要山鸢尾
                        new FilterBatchOp("category='Iris-setosa'")
                ).link( //留前5行
                        new FirstNBatchOp().setSize(5)
                ).print();

        //简化
        data
                .select("sepal_length, sepal_width, category")
                .filter("category='Iris-setosa'")
                .firstN(5)
                .print();

    }
}
