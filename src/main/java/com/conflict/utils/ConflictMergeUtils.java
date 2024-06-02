package com.conflict.utils;

import com.conflict.obj.AbstractConflictSubject;
import com.conflict.obj.ConflictDiff;
import com.conflict.obj.ConflictValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dingqi on 2024/6/1
 * @since 1.0.0
 */
public class ConflictMergeUtils {
    /**
     * 无冲突下的合并配置
     */
    public static <K, V, I> List<AbstractConflictSubject<K, V>> mergeProperty(
            final ConflictDiff<K, I> conflictDiff,
            final List<AbstractConflictSubject<K, V>> baseProperty,
            final Map<I, List<AbstractConflictSubject<K, V>>> changePropertyMap) {
        List<AbstractConflictSubject<K, V>> finalProperty = new ArrayList();
        if (baseProperty != null) {
            finalProperty.addAll(baseProperty);
        }
        // changeId key AppPropertyDesc map结构
        Map<I, Map<K, AbstractConflictSubject<K, V>>> changeKeyPropertyMap = new HashMap<>(16);
        for (I changeId : changePropertyMap.keySet()) {
            List<AbstractConflictSubject<K, V>> properties = changePropertyMap.get(changeId);
            Map<K, AbstractConflictSubject<K,V>> keyPropertyMap = ConflictUtils.parse2MapSubject(properties);
            changeKeyPropertyMap.put(changeId, keyPropertyMap);
        }
        // 处理新增
        if (conflictDiff.getAddKeyChangeMap() != null) {
            for (K key : conflictDiff.getAddKeyChangeMap().keySet()) {
                // 无冲突，取其中一个变更值即可
                List<I> addChangeIds = conflictDiff.getAddKeyChangeMap().get(key);
                I changeId = addChangeIds.get(0);
                AbstractConflictSubject<K, V> addOne = changeKeyPropertyMap.get(changeId).get(key);
                finalProperty.add(addOne);
            }
        }
        Map<K, AbstractConflictSubject<K, V>> finalPropertyMap = ConflictUtils.parse2MapSubject(finalProperty);
        // 处理修改
        if (conflictDiff.getModifyKeyChangeMap() != null) {
            for (K key : conflictDiff.getModifyKeyChangeMap().keySet()) {
                // 无冲突，取其中一个变更值即可
                List<I> modifyChangeIds = conflictDiff.getModifyKeyChangeMap().get(key);
                I changeId = modifyChangeIds.get(0);
                AbstractConflictSubject<K, V> modifyOne = changeKeyPropertyMap.get(changeId).get(key);
                finalPropertyMap.put(key, modifyOne);
            }
        }
        // 处理删除
        if (conflictDiff.getDeleteKeyChangeMap() != null) {
            for (K key : conflictDiff.getDeleteKeyChangeMap().keySet()) {
                finalPropertyMap.remove(key);
            }
        }
        return new ArrayList(finalPropertyMap.values());
    }


    /**
     * 有配置冲突的合并配置，需要传递如何解决的
     */
    public static  <K, V, I> List<AbstractConflictSubject<K, V>> mergeConflictProperty(
            final Map<K, ConflictValue<V>> conflictKeyValueMap,
            final ConflictDiff<K, I> conflictDiff,
            final List<AbstractConflictSubject<K, V>> baseProperty,
            final Map<I, List<AbstractConflictSubject<K, V>>> changePropertyMap) {
        List<AbstractConflictSubject<K, V>> finalProperty = new ArrayList();
        if (baseProperty != null) {
            finalProperty.addAll(baseProperty);
        }
        // changeId key AppPropertyDesc map结构
        Map<I, Map<K, AbstractConflictSubject<K, V>>> changeKeyPropertyMap = new HashMap<>(16);
        for (I changeId : changePropertyMap.keySet()) {
            List<AbstractConflictSubject<K, V>> properties = changePropertyMap.get(changeId);
            Map<K, AbstractConflictSubject<K,V>> keyPropertyMap = ConflictUtils.parse2MapSubject(properties);
            changeKeyPropertyMap.put(changeId, keyPropertyMap);
        }
        // 处理新增
        if (conflictDiff.getAddKeyChangeMap() != null) {
            for (K key : conflictDiff.getAddKeyChangeMap().keySet()) {
                // 无冲突，取其中一个变更值即可
                List<I> addChangeIds = conflictDiff.getAddKeyChangeMap().get(key);
                I changeId = addChangeIds.get(0);
                AbstractConflictSubject<K, V> addOne = changeKeyPropertyMap.get(changeId).get(key);
                if (conflictKeyValueMap.containsKey(key)) {
                    // 使用合并后的值
                    addOne.setValue(conflictKeyValueMap.get(key).getValue());
                }
                finalProperty.add(addOne);
            }
        }
        Map<K, AbstractConflictSubject<K, V>> finalPropertyMap = ConflictUtils.parse2MapSubject(finalProperty);
        // 处理修改
        if (conflictDiff.getModifyKeyChangeMap() != null) {
            for (K key : conflictDiff.getModifyKeyChangeMap().keySet()) {
                // 无冲突，取其中一个变更值即可
                List<I> modifyChangeIds = conflictDiff.getModifyKeyChangeMap().get(key);
                I changeId = modifyChangeIds.get(0);
                AbstractConflictSubject<K, V> modifyOne = changeKeyPropertyMap.get(changeId).get(key);
                if (conflictKeyValueMap.containsKey(key)) {
                    // 使用合并后的值
                    modifyOne.setValue(conflictKeyValueMap.get(key).getValue());
                }
                finalPropertyMap.put(key, modifyOne);
            }
        }
        // 处理删除
        if (conflictDiff.getDeleteKeyChangeMap() != null) {
            for (K key : conflictDiff.getDeleteKeyChangeMap().keySet()) {
                finalPropertyMap.remove(key);
            }
        }
        // 一个key, 既有删除，又有修改
        if (conflictDiff.getDeleteModifyKeys() != null && conflictDiff.getDeleteModifyKeys().size() > 0) {
            for (K key : conflictDiff.getDeleteModifyKeys()) {
                List<I> modifyChangeIds = conflictDiff.getJustModifyChangeMap().get(key);
                I changeId = modifyChangeIds.get(0);
                AbstractConflictSubject<K, V> modifyOne = changeKeyPropertyMap.get(changeId).get(key);
                if (conflictKeyValueMap.containsKey(key)) {
                    // 使用合并后的值
                    modifyOne.setValue(conflictKeyValueMap.get(key).getValue());
                }
                finalPropertyMap.put(key, modifyOne);
            }
        }
        // 统一删除掉
        for (K deleteKey : conflictKeyValueMap.keySet()) {
            ConflictValue conflictValue = conflictKeyValueMap.get(deleteKey);
            if (conflictValue.getDeleted() != null && conflictValue.getDeleted().booleanValue()) {
                finalPropertyMap.remove(deleteKey);
            }
        }
        return new ArrayList(finalPropertyMap.values());
    }
}
