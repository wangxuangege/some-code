package com.wx.somecode;

import lombok.Getter;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author xinquan.huangxq
 */
public class Test {

    private static class A {
        private static final AtomicInteger SEQUENCE = new AtomicInteger(1);
        @Getter
        private int i = SEQUENCE.getAndAdd(1);

        @Override
        public String toString() {
            return "A@" + i;
        }
    }

    private static class B {
        private static final AtomicInteger SEQUENCE = new AtomicInteger(1);
        @Getter
        private int i = SEQUENCE.getAndAdd(1);

        @Override
        public String toString() {
            return "B@" + i;
        }
    }

    private static class C {
        private static final AtomicInteger SEQUENCE = new AtomicInteger(1);
        @Getter
        private int i = SEQUENCE.getAndAdd(1);

        @Override
        public String toString() {
            return "C@" + i;
        }
    }

    public static void main(String[] args) {

        Map<A, Map<B, C>> map = new HashMap<>();
        map.put(new A(), new HashMap<B, C>() {{
            put(new B(), new C());
            put(new B(), new C());
            put(new B(), new C());
            put(new B(), new C());
        }});
        map.put(new A(), new HashMap<B, C>() {{
            put(new B(), new C());
            put(new B(), new C());
            put(new B(), new C());
            put(new B(), new C());
            put(new B(), new C());
            put(new B(), new C());
            put(new B(), new C());
            put(new B(), new C());
        }});

        System.out.println(map);

        Map<B, Map<A, C>> result1 = map.entrySet()
                .stream()
                .collect((Supplier<Map<B, Map<A, C>>>) HashMap::new,
                        (left, right) ->
                                right.getValue().entrySet().forEach(bcEntry -> {
                                    left.put(bcEntry.getKey(), new HashMap<A, C>() {{
                                        put(right.getKey(), bcEntry.getValue());
                                    }});
                                }),
                        Map::putAll
                );

        System.out.println(result1);

        Map<B, Map<A, C>> result2 = map.entrySet()
                .stream()
                .collect((Supplier<Map<B, Map<A, C>>>) HashMap::new,
                        (left, right) -> left.putAll(right.getValue().entrySet()
                                .stream()
                                .collect(Collectors.groupingBy(Map.Entry::getKey,
                                        Collectors.mapping(Function.identity(), Collectors.toMap(e -> right.getKey(), Map.Entry::getValue))))),
                        Map::putAll
                );
        System.out.println(result2);
    }
}
