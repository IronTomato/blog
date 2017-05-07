package irontomato.blogcode.diff_between_stringbuffer_stringbuilder;

import org.junit.Test;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.*;

public class StringBufferTest {

    @Test
    public void testMultithreadAppend() throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(5);
        int n = 100_000;
        StringBuffer[] sbs = new StringBuffer[n];
        IntStream.range(0, n)
                .forEach(i -> {
                    StringBuffer sb = sbs[i] = new StringBuffer();
                    pool.execute(() -> sb.append("A"));
                    pool.execute(() -> sb.append("B"));
                });
        pool.shutdown();
        pool.awaitTermination(30, TimeUnit.SECONDS);
        Map<String, Long> map = Arrays.stream(sbs)
                .map(StringBuffer::toString)
                .collect(groupingBy(Function.identity(), counting()));
        System.out.println(map.toString()); // {AB=97236, BA=2764}
    }
}
