import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;

public class HelloFlink {
    public static void main(String[] args) {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        DataSource<String> textStream = env.fromElements("Hello Flink", "Hello Java");
        try {
            textStream.print();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}