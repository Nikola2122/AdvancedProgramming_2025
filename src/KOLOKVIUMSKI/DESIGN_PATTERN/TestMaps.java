package KOLOKVIUMSKI.DESIGN_PATTERN;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestMaps {
    static void main() {
        List<Map<String, Integer>> maps = new ArrayList<>();
        Map<String, Integer> map2 = maps.stream().reduce(
                new HashMap<>(),
                (left,right) -> {
                    left.forEach((k,v)-> right.merge(k, v, (v1, v2) -> 0));
                    return right;
                }
        );
    }
}
