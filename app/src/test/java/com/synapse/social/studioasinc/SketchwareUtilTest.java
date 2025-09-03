package com.synapse.social.studioasinc;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

public class SketchwareUtilTest {

    @Test
    public void testSortListMap_withDoubleValues_sortsCorrectly() {
        ArrayList<HashMap<String, Object>> list = new ArrayList<>();

        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("value", "2.5");
        list.add(map1);

        HashMap<String, Object> map2 = new HashMap<>();
        map2.put("value", "1.1");
        list.add(map2);

        HashMap<String, Object> map3 = new HashMap<>();
        map3.put("value", "3.0");
        list.add(map3);

        // Sort in ascending order
        SketchwareUtil.sortListMap(list, "value", true, true);

        assertEquals("1.1", list.get(0).get("value"));
        assertEquals("2.5", list.get(1).get("value"));
        assertEquals("3.0", list.get(2).get("value"));

        // Sort in descending order
        SketchwareUtil.sortListMap(list, "value", true, false);

        assertEquals("3.0", list.get(0).get("value"));
        assertEquals("2.5", list.get(1).get("value"));
        assertEquals("1.1", list.get(2).get("value"));
    }
}
