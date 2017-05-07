package irontomato.blogcode.diff_between_stringbuffer_stringbuilder;

import org.junit.Test;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public class StringBuilderTest {
    @Test
    public void testMultithreadAppend() throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(5);
        int n = 100_000;
        StringBuilder[] sbs = new StringBuilder[n];
        IntStream.range(0, n)
                .forEach(i -> {
                    StringBuilder sb = sbs[i] = new StringBuilder();
                    pool.execute(() -> sb.append("A"));
                    pool.execute(() -> sb.append("B"));
                });
        pool.shutdown();
        pool.awaitTermination(30, TimeUnit.SECONDS);
        Map<String, Long> map = Arrays.stream(sbs)
                .map(StringBuilder::toString)
                .collect(groupingBy(Function.identity(), counting()));
        System.out.println(map.toString()); // {A=31, AB=97850, B=32, B =109, A =69, BA=1909}
    }
}
