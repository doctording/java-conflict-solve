package com.conflict.util;

import com.conflict.obj.AbstractConflictSubject;
import com.conflict.ConflictSubject;
import com.conflict.enums.ConflictOperateEnum;
import com.conflict.utils.ConflictUtils;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author dingqi on 2024/6/1
 * @since 1.0.0
 */
public class UtilCheckDiffTest {

    @Test
    public void test() {
        ConflictSubject conflictSubject = new ConflictSubject("k1", "value1");
        ConflictSubject conflictSubject2 = new ConflictSubject("k2", "value2");
        ConflictSubject conflictSubject3 = new ConflictSubject("k3", "value3");
        List<AbstractConflictSubject<String, String>> baseList = Arrays.asList(
                conflictSubject, conflictSubject2, conflictSubject3);

        ConflictSubject curConflictSubject = new ConflictSubject("k1", "value1");
        ConflictSubject curConflictSubject2 = new ConflictSubject("k2", "value2222");
        ConflictSubject curConflictSubject3 = new ConflictSubject("k4", "value4");
        List<AbstractConflictSubject<String, String>> curList = Arrays.asList(
                curConflictSubject, curConflictSubject2, curConflictSubject3);

        Map<ConflictOperateEnum, List<String>> conflictOperateEnumListMap =
                ConflictUtils.checkBaseDiff(baseList, curList);

        assertNotNull(conflictOperateEnumListMap);
        assertTrue(conflictOperateEnumListMap.containsKey(ConflictOperateEnum.ADD));
        assertTrue(conflictOperateEnumListMap.containsKey(ConflictOperateEnum.MODIFY));
        assertTrue(conflictOperateEnumListMap.containsKey(ConflictOperateEnum.DELETE));
    }
}
