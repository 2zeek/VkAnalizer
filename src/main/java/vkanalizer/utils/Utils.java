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

    public static void listsSeparator(List<Integer> list1, List<Integer> list2,
                                      List<Integer> list3, List<String> list4) {
        for (Integer id : list1) {
            if (!list2.contains(id))
                if (id > 0)
                    list3.add(id);
                else
                    list4.add(String.valueOf(id));
        }
    }
}
