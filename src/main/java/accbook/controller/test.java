package accbook.controller;

import java.util.*;
import java.util.stream.IntStream;

public class test
{

    public static void main(String[] args)
    {
        System.out.println(IntStream.range(0, 1001).skip(500).filter(i -> i % 2 == 0).filter(i -> i % 5 == 0).sum());

        Map<String, String> map = new HashMap<>();
        map.put("one", "일임");
        map.put("two", "이임");


        List<String> list = new ArrayList<>();
        list.add("one");
        list.add("one");
        list.add("one");
        list.add("one");
        list.add("one");
        list.add("two");

        System.out.println("--------zz--------");
        list.stream()
                .filter(i -> i.equals("one"))
                .forEach(System.out::println);

        list.stream()
                .map(i -> i.equals("one"))
                .forEach(System.out::println);

        System.out.println("----------------");

        Arrays.asList(1, 2, 3).stream()
                .map(i -> i * i)
                .forEach(System.out::println);

        Arrays.asList(1, 2, 3).stream()
                .limit(1)
                .forEach(System.out::println);
        System.out.println("----------------");
        Arrays.asList(1, 2, 3).stream()
                .skip(1)
                .forEach(System.out::println);
        System.out.println("---------------");
        Arrays.asList(1, 2, 3).stream()
                .filter(i -> i == 1)
                .forEach(System.out::println);
        System.out.println("---------------");
        Arrays.asList(Arrays.asList(1, 2), Arrays.asList(3, 4, 5), Arrays.asList(6, 7, 8, 9)).stream()
                .flatMap(i -> i.stream())
                .forEach(System.out::println);
        System.out.println("-----------------");
        Arrays.asList(1, 2, 3).stream()
                .reduce((a, b) -> a - b)
                .get();
    }
}
