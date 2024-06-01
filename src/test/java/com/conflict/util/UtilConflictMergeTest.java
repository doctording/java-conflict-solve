package com.conflict.util;

import com.conflict.ConflictSubject;
import com.conflict.obj.AbstractConflictSubject;
import com.conflict.obj.ConflictDiff;
import com.conflict.obj.ConflictHasAndRecord;
import com.conflict.obj.ConflictValue;
import com.conflict.utils.ConflictMergeUtils;
import com.conflict.utils.ConflictUtils;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author dingqi on 2024/6/1
 * @since 1.0.0
 */
public class UtilConflictMergeTest {

    @Test
    public void testNoConflictMerge() {
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

        // 合并结果
        List<AbstractConflictSubject<String, String>> finalConfig =
                ConflictMergeUtils.mergeProperty(conflictDiff, propertyBase, changeMap);
        assertNotNull(finalConfig);
        Map<String, String> keyValueMap = ConflictUtils.parse2MapValue(finalConfig);
        assertFalse(keyValueMap.containsKey("k1"));
        assertTrue(keyValueMap.containsKey("k2"));
        assertTrue(keyValueMap.containsKey("k3"));
        assertTrue(keyValueMap.containsKey("k4"));
        assertEquals("v2222", keyValueMap.get("k2"));
    }


    @Test
    public void testConflictMerge1() {
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
        ConflictHasAndRecord<String, String, Long> conflictHasAndRecord = ConflictUtils.hasConflict(conflictDiff, changeMap);
        assertTrue(conflictHasAndRecord.getHasConflict());

        // 冲突处理方式1
        Map<String, ConflictValue<String>> conflictKeyValueMap = new HashMap<>();
        ConflictValue k2Value = new ConflictValue(null, true);
        ConflictValue k4Value = new ConflictValue("v45", false);
        conflictKeyValueMap.put("k2", k2Value);
        conflictKeyValueMap.put("k4", k4Value);

        List<AbstractConflictSubject<String, String>> finalConfig =
                ConflictMergeUtils.mergeConflictProperty(conflictKeyValueMap, conflictDiff, propertyBase, changeMap);
        assertNotNull(finalConfig);
        Map<String, String> keyValueMap = ConflictUtils.parse2MapValue(finalConfig);
        assertTrue(keyValueMap.containsKey("k3"));
        assertTrue(keyValueMap.containsKey("k4"));
        assertTrue(keyValueMap.containsKey("k6"));
        assertFalse(keyValueMap.containsKey("k2"));// 最后是删除了k2
        assertEquals("v45", keyValueMap.get("k4")); // 最后是k4的值设置为k45
    }

    @Test
    public void testConflictMerge2() {
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
        ConflictHasAndRecord<String, String, Long> conflictHasAndRecord = ConflictUtils.hasConflict(conflictDiff, changeMap);
        assertTrue(conflictHasAndRecord.getHasConflict());

        // 冲突处理方式1
        Map<String, ConflictValue<String>> conflictKeyValueMap = new HashMap<>();
        ConflictValue k2Value = new ConflictValue("v22", false);
        ConflictValue k4Value = new ConflictValue("v45", false);
        conflictKeyValueMap.put("k2", k2Value);
        conflictKeyValueMap.put("k4", k4Value);

        List<AbstractConflictSubject<String, String>> finalConfig =
                ConflictMergeUtils.mergeConflictProperty(conflictKeyValueMap, conflictDiff, propertyBase, changeMap);
        assertNotNull(finalConfig);
        Map<String, String> keyValueMap = ConflictUtils.parse2MapValue(finalConfig);
        assertTrue(keyValueMap.containsKey("k3"));
        assertTrue(keyValueMap.containsKey("k4"));
        assertTrue(keyValueMap.containsKey("k6"));
        assertTrue(keyValueMap.containsKey("k2"));// 最后是保留了k2，且值是v22
        assertEquals("v22", keyValueMap.get("k2")); // 最后是k4的值设置为k45
        assertEquals("v45", keyValueMap.get("k4")); // 最后是k4的值设置为k45
    }
}
