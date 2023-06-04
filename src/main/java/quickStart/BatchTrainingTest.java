package quickStart;

import com.alibaba.alink.operator.batch.BatchOperator;
import com.alibaba.alink.operator.batch.regression.LinearRegPredictBatchOp;
import com.alibaba.alink.operator.batch.regression.LinearRegTrainBatchOp;
import com.alibaba.alink.operator.batch.source.MemSourceBatchOp;
import org.apache.flink.types.Row;

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

        //创建一个批处理的线性回归操作
        LinearRegTrainBatchOp trainer = new LinearRegTrainBatchOp()
                .setFeatureCols("x", "x2")     //特征
                .setLabelCol("gmv");             //标签

        //记得那里看到过 Alink 可以看成 A + Link 其中Link就是可以把各种操作连接(有点像管道，但不是)
        trainSet.link(trainer);

        //看来做预测要先创建一个角色
        LinearRegPredictBatchOp predictor = new LinearRegPredictBatchOp()
                .setPredictionCol("pred");

        //做预测
        predictor
                .linkFrom(trainer, predSet).print();

        /*
            x|x|x2|pred
             -|--|----
             2018|4072324|2142.4048
             2019|4076361|2682.2263
        */

    }
}
