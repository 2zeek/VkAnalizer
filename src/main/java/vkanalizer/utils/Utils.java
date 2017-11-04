package vkanalizer.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by Nikolay V. Petrov on 04.11.2017.
 */
public class Utils {

    public static List<Integer> stringToList(String string) {
        List<Integer> resultList = new ArrayList<>();
        Pattern pattern = Pattern.compile(",");
        if (!string.isEmpty())
            resultList =  pattern.splitAsStream(string)
                    .map(Integer::valueOf)
                    .collect(Collectors.toList());
        return resultList;
    }

    public static String listToString(List<Integer> list) {
        return list.stream().map(Object::toString)
                .collect(Collectors.joining(","));
    }
}
