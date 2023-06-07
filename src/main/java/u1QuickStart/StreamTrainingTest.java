package u1QuickStart;

import com.alibaba.alink.operator.stream.StreamOperator;
import com.alibaba.alink.operator.stream.source.MemSourceStreamOp;
import com.alibaba.alink.pipeline.PipelineModel;

import static domain.BasicConstants.MODEL_PATH;

public class StreamTrainingTest {
    public static void main(String[] args) throws Exception {
        //其实流计算和批计算用法差不多
        StreamOperator<?> predSet = new MemSourceStreamOp(
                new Integer[]{2018, 2019},
                "x"
        );
        predSet = predSet.select("x, x*x as x2");

        //演示下如何读模型
        PipelineModel model = PipelineModel
                .load(MODEL_PATH + "quickStart/linearModel.model");

        //看来，流批通吃
        model.transform(predSet).print();


        //和Flink一样，记得execute()
        StreamOperator.execute();

        /*
            x|x2|pred
            -|--|----
            2019|4076361|2682.2263
            2018|4072324|2142.4048
        * */
    }
}
