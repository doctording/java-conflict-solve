package com.conflict.util;

import com.conflict.obj.AbstractConflictSubject;
import com.conflict.obj.ConflictDiff;
import com.conflict.ConflictSubject;
import com.conflict.obj.ConflictHasAndRecord;
import com.conflict.obj.ConflictRecord;
import com.conflict.utils.ConflictUtils;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author dingqi on 2024/6/1
 * @since 1.0.0
 */
public class UtilConflictDiffHasTest {
    @Test
    public void test() {
        List<AbstractConflictSubject<String, String>> propertyBase = Arrays.asList(
                new ConflictSubject("k1", "v1"),
                new ConflictSubject("k2", "v2"),
                new ConflictSubject("k3", "v3"));
        List<AbstractConflictSubject<String, String>> propertyList1 = Arrays.asList(
                new ConflictSubject("k1", "v1"),
                new ConflictSubject("k3", "v3"),
                new ConflictSubject("k4", "v4"));
        List<AbstractConflictSubject<String, String>> propertyList2 = Arrays.asList(
                new ConflictSubject("k1", "v1"),
                new ConflictSubject("k3", "v3"),
                new ConflictSubject("k4", "v5"));
        List<AbstractConflictSubject<String, String>> propertyList3 = Arrays.asList(
                new ConflictSubject("k1", "v1"),
                new ConflictSubject("k2", "v2222"),
                new ConflictSubject("k3", "v3"));
        List<AbstractConflictSubject<String, String>> propertyList4 = Arrays.asList(
                new ConflictSubject("k2", "v2"),
                new ConflictSubject("k3", "v3"));
        List<AbstractConflictSubject<String, String>> propertyList5 = Arrays.asList(
                new ConflictSubject("k2", "v2"),
                new ConflictSubject("k3", "v3"));
        List<AbstractConflictSubject<String, String>> propertyList6 = Arrays.asList(
                new ConflictSubject("k1", "v1"),
                new ConflictSubject("k2", "v2"),
                new ConflictSubject("k3", "v3"),
                new ConflictSubject("k6", "v3"));
        List<AbstractConflictSubject<String, String>> propertyList7 = Arrays.asList(
                new ConflictSubject("k1", "v1"),
                new ConflictSubject("k2", "v2"),
                new ConflictSubject("k3", "v333"));
        Map<Long, List<AbstractConflictSubject<String, String>>> changeMap = new HashMap<>();
        changeMap.put(1L, propertyList1);
        changeMap.put(2L, propertyList2);
        changeMap.put(3L, propertyList3);
        changeMap.put(4L, propertyList4);
        changeMap.put(5L, propertyList5);
        changeMap.put(6L, propertyList6);
        changeMap.put(7L, propertyList7);
        // 获取冲突
        // 添加：k4, k6
        // 修改: k3
        // 删除: k1
        // 修改删除并存: k2
        ConflictDiff<String, Long> conflictDiff = ConflictUtils.conflictKeyOfMultiChange(propertyBase, changeMap);
        assertNotNull(conflictDiff);

        // 判断有冲突
        ConflictHasAndRecord<String, String, Long> conflictHasAndRecord =
                ConflictUtils.hasConflict(conflictDiff, changeMap);
        assertTrue(conflictHasAndRecord.getHasConflict());
        Map<String, List<ConflictRecord<String, String, Long>>> keyChangeMap = conflictHasAndRecord.getKeyRecordMap();
        assertTrue(keyChangeMap.containsKey("k4"));
        assertTrue(keyChangeMap.containsKey("k2"));
    }

    @Test
    public void test2(){
        List<AbstractConflictSubject<String, String>> propertyBase = Arrays.asList(
                new ConflictSubject("k1", "v1"),
                new ConflictSubject("k2", "v2"),
                new ConflictSubject("k3", "v3"));
        List<AbstractConflictSubject<String, String>> propertyList1 = Arrays.asList(
                new ConflictSubject("k1", "v1"),
                new ConflictSubject("k2", "v2"),
                new ConflictSubject("k3", "v3"),
                new ConflictSubject("k4", "v4"));
        List<AbstractConflictSubject<String, String>> propertyList2 = Arrays.asList(
                new ConflictSubject("k2", "v2"),
                new ConflictSubject("k3", "v3"),
                new ConflictSubject("k4", "v4"));
        List<AbstractConflictSubject<String, String>> propertyList3 = Arrays.asList(
                new ConflictSubject("k2", "v2222"),
                new ConflictSubject("k3", "v3"));
        Map<Long, List<AbstractConflictSubject<String, String>>> changeMap = new HashMap<>();
        changeMap.put(1L, propertyList1);
        changeMap.put(2L, propertyList2);
        changeMap.put(3L, propertyList3);
        // 获取冲突
        // 添加：k4
        // 修改: k2
        // 删除: k1
        // 修改删除并存: 无
        ConflictDiff<String, Long> conflictDiff = ConflictUtils.conflictKeyOfMultiChange(propertyBase, changeMap);
        assertNotNull(conflictDiff);

        // 判断无冲突
        ConflictHasAndRecord<String, String, Long> conflictHasAndRecord =
                ConflictUtils.hasConflict(conflictDiff, changeMap);
        assertFalse(conflictHasAndRecord.getHasConflict());
    }
}
