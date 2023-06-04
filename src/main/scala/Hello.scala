import org.apache.flink.streaming.api.scala._

object Hello {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val dataStream:DataStream[String] = env.fromElements("Hello Flink")
    dataStream.print()

    env.execute("Hello")
  }
}
