package u1QuickStart;

import com.alibaba.alink.operator.batch.BatchOperator;
import com.alibaba.alink.operator.batch.source.MemSourceBatchOp;
import com.alibaba.alink.pipeline.Pipeline;
import com.alibaba.alink.pipeline.PipelineModel;
import com.alibaba.alink.pipeline.regression.LinearRegression;
import org.apache.flink.types.Row;

import static domain.BasicConstants.MODEL_PATH;

/**
 * @author canyue
 */
public class BatchTrainingTest {
    public static void main(String[] args) throws Exception {

        /*
          手动创建数据，这里根据书中，创建09-17年的训练数据
          MemSourceBatchOp 有两个参数，rows/vals与colNames
         */
        BatchOperator<?> trainSet = new MemSourceBatchOp(
                new Row[]{ //flink.types.Row
                        Row.of(2009, 0.5),
                        Row.of(2010, 9.36),
                        Row.of(2011, 52.0),
                        Row.of(2012, 191.0),
                        Row.of(2013, 350.0),
                        Row.of(2014, 571.0),
                        Row.of(2015, 912.0),
                        Row.of(2016, 1207.0),
                        Row.of(2017, 1682.0)
                },
                new String[]{"x", "gmv"}    //年份 gmv(万亿元)
        );

        // 再创建一个用于预测的数据，只要年份的数据
        BatchOperator<?> predSet = new MemSourceBatchOp(
                new Integer[]{2018, 2019},
                "x"
        );

        //书上说，成交总额与年份之间不成线性关系，确实，18和19年成交总额都有2000多万亿了，确实牛
        //所以书上将X平方作为新特征
        trainSet = trainSet.select("x, x*x as x2, gmv");
        predSet = predSet.select("x, x*x as x2");


        //创建一个批处理的线性回归
        LinearRegression linearRegression = new LinearRegression()
                .setFeatureCols("x", "x2")     //特征
                .setLabelCol("gmv")             //标签
                .setPredictionCol("pred");;

        //貌似书本上的写法有点旧，我还是用新的方法吧，这样还比较像SparkML，更习惯
        //先来一个管道
        Pipeline trainer = new Pipeline()
                .add(linearRegression);

        //通过fit方法可以训练处一个模型
        PipelineModel model = trainer.fit(trainSet);

        //保存模型
        model.save(MODEL_PATH  + "quickStart/linearModel.model");

        //预测
        model
                .transform(predSet)
                .print();

        /*
            x|x2|pred
            -|--|----
            2018|4072324|2142.4048
            2019|4076361|2682.2263
        */

    }
}
