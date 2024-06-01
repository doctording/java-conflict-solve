package com.conflict.util;

import com.conflict.obj.AbstractConflictSubject;
import com.conflict.ConflictSubject;
import com.conflict.utils.ConflictUtils;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author dingqi on 2024/6/1
 * @since 1.0.0
 */
public class UtilParseTest {

    @Test
    public void testParse() {
        ConflictSubject conflictSubject = new ConflictSubject("k1", "value1");
        ConflictSubject conflictSubject2 = new ConflictSubject("k2", "value2");
        List<AbstractConflictSubject<String, String>> conflictSubjectList = Arrays.asList(conflictSubject, conflictSubject2);
        Map<String, AbstractConflictSubject<String,String>> keySubjectMap = ConflictUtils.parse2MapSubject(conflictSubjectList);
        assertNotNull(keySubjectMap);
        assertTrue(keySubjectMap.containsKey("k1"));
        assertTrue(keySubjectMap.containsKey("k2"));

        Map<String, String> keyValueMap = ConflictUtils.parse2MapValue(conflictSubjectList);
        assertNotNull(keyValueMap);
        assertEquals("value1", keyValueMap.containsKey("k1"));
        assertEquals("value2",keyValueMap.containsKey("k2"));
    }

}
