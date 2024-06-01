package com.conflict.utils;

import com.conflict.enums.ConflictOperateEnum;
import com.conflict.obj.AbstractConflictSubject;
import com.conflict.obj.ConflictDiff;
import com.conflict.obj.ConflictHasAndRecord;
import com.conflict.obj.ConflictRecord;
import com.conflict.obj.ConflictValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 冲突工具类
 * @author dingqi on 2024/6/1
 * @since 1.0.0
 */
public class ConflictUtils {

    /**
     * 判断是否有冲突, 并记录冲突记录
     */
    public static <K,V,I> ConflictHasAndRecord<K, V, I> hasConflict(final ConflictDiff<K, I> conflictDiff,
                                                           final Map<I, List<AbstractConflictSubject<K,V>>> changePropertyMap) {
        // 判断冲突，同时记录冲突情况（全部冲突都记录）
        ConflictHasAndRecord conflictHasAndRecord = new ConflictHasAndRecord();
        Map<K, List<ConflictRecord<K, V, I>>> keyConflictRecordMap = new HashMap<>(16);
        boolean flag = false;
        // 这些变更修改的转换
        Map<I, Map<K, V>> changeKeyValueMap = new HashMap<>(16);
        for (I changeId : changePropertyMap.keySet()) {
            List<AbstractConflictSubject<K, V>> propertyList = changePropertyMap.get(changeId);
            Map<K, V> keyValueMap = ConflictUtils.parse2MapValue(propertyList);
            changeKeyValueMap.put(changeId, keyValueMap);
        }
        // 检查场景2，4，7
        // 1. 多个变更新增相同key, value不一样
        if (conflictDiff.getAddKeyChangeMap() != null) {
            for (K key : conflictDiff.getAddKeyChangeMap().keySet()) {
                List<I> addChangeIds = conflictDiff.getAddKeyChangeMap().get(key);
                if (addChangeIds.size() <= 1) {
                    continue;
                }
                Set<V> valueSet = new HashSet(8);
                for (I changeId : addChangeIds) {
                    V value = changeKeyValueMap.get(changeId).get(key);
                    valueSet.add(value);
                }
                if (valueSet.size() > 1) {
                    flag = true;
                    // 冲突记录，每个变更都有
                    List<ConflictRecord<K, V, I>> conflictRecordList = new ArrayList();
                    for (I changeId : addChangeIds) {
                        ConflictRecord<K, V, I> conflictRecord = new ConflictRecord();
                        conflictRecord.setKey(key);
                        conflictRecord.setChangeId(changeId);
                        conflictRecord.setChangeValue(new ConflictValue<V>(changeKeyValueMap.get(changeId).get(key), false));
                        conflictRecordList.add(conflictRecord);
                    }
                    keyConflictRecordMap.put(key, conflictRecordList);
                }
            }
        }
        // 2. 多个变更修改相同key, value不一样
        if (conflictDiff.getModifyKeyChangeMap() != null) {
            for (K key : conflictDiff.getModifyKeyChangeMap().keySet()) {
                List<I> modifyChangeIds = conflictDiff.getModifyKeyChangeMap().get(key);
                if (modifyChangeIds.size() <= 1) {
                    continue;
                }
                Set<V> valueSet = new HashSet(8);
                for (I changeId : modifyChangeIds) {
                    V value = changeKeyValueMap.get(changeId).get(key);
                    valueSet.add(value);
                }
                if (valueSet.size() > 1) {
                    flag = true;
                    // 冲突记录，每个变更都有
                    List<ConflictRecord<K,V,I>> conflictRecordList = new ArrayList();
                    for (I changeId : modifyChangeIds) {
                        ConflictRecord conflictRecord = new ConflictRecord();
                        conflictRecord.setKey(key);
                        conflictRecord.setChangeId(changeId);
                        conflictRecord.setChangeValue(new ConflictValue<V>(changeKeyValueMap.get(changeId).get(key), false));
                        conflictRecordList.add(conflictRecord);
                    }
                    keyConflictRecordMap.put(key, conflictRecordList);
                }
            }
        }
        // 3. 一个key, 既有删除，又有修改
        if (conflictDiff.getDeleteModifyKeys() != null && conflictDiff.getDeleteModifyKeys().size() > 0) {
            flag = true;
            for (K deleteModifyKey : conflictDiff.getDeleteModifyKeys()) {
                List<I> deleteChanges = conflictDiff.getJustDeleteChangeMap().get(deleteModifyKey);
                List<I> modifyChanges = conflictDiff.getJustModifyChangeMap().get(deleteModifyKey);
                List<ConflictRecord<K, V, I>> conflictRecordList = new ArrayList();
                for (I changeId : deleteChanges) {
                    ConflictRecord conflictRecord = new ConflictRecord();
                    conflictRecord.setKey(deleteModifyKey);
                    conflictRecord.setChangeId(changeId);
                    conflictRecord.setChangeValue(new ConflictValue<V>(null, true));
                    conflictRecordList.add(conflictRecord);
                }
                for (I changeId : modifyChanges) {
                    ConflictRecord conflictRecord = new ConflictRecord();
                    conflictRecord.setKey(deleteModifyKey);
                    conflictRecord.setKey(deleteModifyKey);
                    conflictRecord.setChangeId(changeId);
                    conflictRecord.setChangeValue(new ConflictValue<V>(changeKeyValueMap.get(changeId).get(deleteModifyKey), false));
                    conflictRecordList.add(conflictRecord);
                }
                keyConflictRecordMap.put(deleteModifyKey, conflictRecordList);
            }
        }
        conflictHasAndRecord.setHasConflict(flag);
        conflictHasAndRecord.setKeyRecordMap(keyConflictRecordMap);
        return conflictHasAndRecord;
    }

    /**
     * 获取所有冲突的key的情况，合并冲突需要用到
     *  场景分析
     *     1. 新增key，只有一个变更
     *     2. 新增key，多个变更新增相同的这个key ------冲突
     *     3. 修改key，只有一个变更修改
     *     4. 修改key，多个变更修改相同的这个key ------- 冲突
     *     5. 删除key，一个变更删除
     *     6. 删除key，多个变更删除
     *     7. 同一个key，存在1个变更修改（可能多个变更），另一个变更删除（可能多个变更） ------- 冲突
     */
    public static <K, V, I> ConflictDiff<K, I> conflictKeyOfMultiChange(List<AbstractConflictSubject<K,V>> baseList,
                                                                     Map<I, List<AbstractConflictSubject<K,V>>> changeMap) {
        Map<I, List<K>> changeAddDiff = new HashMap<>(16);
        Map<I, List<K>> changeModifyDiff = new HashMap<>(16);
        Map<I, List<K>> changeDeleteDiff = new HashMap<>(16);
        for (I changeId : changeMap.keySet()) {
            List<AbstractConflictSubject<K, V>> changeSubjectList = changeMap.get(changeId);
            Map<ConflictOperateEnum, List<K>> diff = checkBaseDiff(baseList, changeSubjectList);
            changeAddDiff.put(changeId, diff.get(ConflictOperateEnum.ADD));
            changeModifyDiff.put(changeId, diff.get(ConflictOperateEnum.MODIFY));
            changeDeleteDiff.put(changeId, diff.get(ConflictOperateEnum.DELETE));
        }

        // 新增key的判断，List<I> 的数量为1，表示场景1， 其它场景2
        Map<K, List<I>> addKeyChangeMap = new HashMap<>(16);
        for (I changeId : changeAddDiff.keySet()) {
            List<K> addKeys = changeAddDiff.get(changeId);
            for (K key : addKeys) {
                List<I> changeIdList = addKeyChangeMap.getOrDefault(key, new ArrayList<>());
                changeIdList.add(changeId);
                addKeyChangeMap.put(key, changeIdList);
            }
        }

        // 修改key的判断，List<I> 的数量为1，表示场景1， 其它场景2
        Map<K, List<I>> modifyKeyChangeMap = new HashMap<>(16);
        for (I changeId : changeModifyDiff.keySet()) {
            List<K> modifyKeys = changeModifyDiff.get(changeId);
            for (K key : modifyKeys) {
                List<I> changeIdList = modifyKeyChangeMap.getOrDefault(key, new ArrayList<>());
                changeIdList.add(changeId);
                modifyKeyChangeMap.put(key, changeIdList);
            }
        }

        // 删除key的判断，List<I> crid 的数量为1，表示场景1， 其它场景2
        Map<K, List<I>> deleteKeyChangeMap = new HashMap<>(16);
        for (I changeId : changeDeleteDiff.keySet()) {
            List<K> deleteKeys = changeDeleteDiff.get(changeId);
            for (K key : deleteKeys) {
                List<I> changeIdList = deleteKeyChangeMap.getOrDefault(key, new ArrayList<>());
                changeIdList.add(changeId);
                deleteKeyChangeMap.put(key, changeIdList);
            }
        }

        // 同时存在删除和修改的场景
        List<K> deleteModifyKeys = new ArrayList<>();
        Map<K, List<I>> justModifyChangeMap = new HashMap<>(16);
        Map<K, List<I>> justDeleteChangeMap = new HashMap<>(16);
        for (K key : modifyKeyChangeMap.keySet()) {
            if (deleteKeyChangeMap.containsKey(key)) {
                deleteModifyKeys.add(key);

                List<I> modifyChanges = justModifyChangeMap.getOrDefault(key, new ArrayList<>());
                modifyChanges.addAll(modifyKeyChangeMap.get(key));
                justModifyChangeMap.put(key, modifyChanges);

                List<I> deleteChanges = justDeleteChangeMap.getOrDefault(key, new ArrayList<>());
                deleteChanges.addAll(deleteKeyChangeMap.get(key));
                justDeleteChangeMap.put(key, deleteChanges);
            }
        }

        // 纯修改 和 纯删除需要去除这些key
        for (K key : deleteModifyKeys) {
            modifyKeyChangeMap.remove(key);
            deleteKeyChangeMap.remove(key);
        }

        ConflictDiff<K,I> conflictDiff = new ConflictDiff();
        conflictDiff.setAddKeyChangeMap(addKeyChangeMap);
        conflictDiff.setModifyKeyChangeMap(modifyKeyChangeMap);
        conflictDiff.setDeleteKeyChangeMap(deleteKeyChangeMap);
        conflictDiff.setDeleteModifyKeys(deleteModifyKeys);
        conflictDiff.setJustModifyChangeMap(justModifyChangeMap);
        conflictDiff.setJustDeleteChangeMap(justDeleteChangeMap);

        return conflictDiff;
    }

    /**
     * 比较两个配置，得到增删改的变化
     */
    public static <K, V> Map<ConflictOperateEnum, List<K>> checkBaseDiff(List<AbstractConflictSubject<K,V>> baseList, List<AbstractConflictSubject<K,V>> curList) {
        Map<ConflictOperateEnum, List<K>> opKeys = new HashMap<>(32);
        List<K> addKeys = new ArrayList<>();
        List<K> deleteKeys = new ArrayList<>();
        List<K> modifyKeys = new ArrayList<>();
        Map<K, V> baseKeyValueMap = parse2MapValue(baseList);
        Map<K, V> curKeyValueMap = parse2MapValue(curList);
        for (K baseKey : baseKeyValueMap.keySet()) {
            // 删除
            if (!curKeyValueMap.containsKey(baseKey)) {
                deleteKeys.add(baseKey);
            } else {
                // 有修改的配置
                if (!baseKeyValueMap.get(baseKey).equals(curKeyValueMap.get(baseKey))) {
                    modifyKeys.add(baseKey);
                }
            }
        }
        // 新增的配置
        for (K key : curKeyValueMap.keySet()) {
            if (!baseKeyValueMap.containsKey(key)) {
                addKeys.add(key);
            }
        }
        opKeys.put(ConflictOperateEnum.ADD, addKeys);
        opKeys.put(ConflictOperateEnum.MODIFY, modifyKeys);
        opKeys.put(ConflictOperateEnum.DELETE, deleteKeys);
        return opKeys;
    }

    /**
     * 转化为key为map的结构1: <key, value>
     */
    public static <K, V> Map<K, V> parse2MapValue(final List<AbstractConflictSubject<K, V>> list) {
        if (list == null) {
            return new HashMap(4);
        }
        Map<K, V> keyValueMap = new HashMap<>(32);
        for (AbstractConflictSubject<K, V> subject : list) {
            keyValueMap.put(subject.getKey(), subject.getValue());
        }
        return keyValueMap;
    }

    /**
     * 转化为key为map的结构2: <key, AbstractConflictSubject>
     */
    public static <K, V> Map<K, AbstractConflictSubject<K, V>> parse2MapSubject(final List<AbstractConflictSubject<K,V>> list) {
        if (list == null) {
            return new HashMap<>(4);
        }
        Map<K, AbstractConflictSubject<K,V>> keySubjectMap = new HashMap(32);
        for (AbstractConflictSubject<K, V> subject : list) {
            keySubjectMap.put(subject.getKey(), subject);
        }
        return keySubjectMap;
    }

}
