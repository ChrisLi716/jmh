package test;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 2, time = 1)
@Measurement(iterations = 5, time = 5)
@Fork(1)
@State(Scope.Thread)
public class SortBenchmark2 {
    @Param(value = {"100", "10000"})
    private int operationSize; // 操作次数
    private static List<Integer> arrayList;

    @Setup
    public void init() {
        // 启动执行事件
        arrayList = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < operationSize; i++) {
            arrayList.add(random.nextInt(10000));
        }
    }

    @Benchmark
    public void stream(Blackhole blackhole) {
        arrayList.stream().collect(Collectors.toList());
        blackhole.consume(arrayList);
    }

    @Benchmark
    public void sort(Blackhole blackhole) {
        arrayList.stream().sorted(Comparator.comparing(Integer::intValue)).collect(Collectors.toList());
        blackhole.consume(arrayList);
    }

    public static void main(String[] args) throws RunnerException {
        // 启动基准测试
        Options opt = new OptionsBuilder()
                .include(SortBenchmark2.class.getSimpleName()) // 要导入的测试类
                .result("SortBenchmark2.json")
                .mode(Mode.All)
                .resultFormat(ResultFormatType.JSON)
                .build();
        new Runner(opt).run(); // 执行测试
    }
}
