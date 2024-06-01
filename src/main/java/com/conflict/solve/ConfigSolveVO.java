package com.conflict.solve;

import com.conflict.obj.ConflictValue;

import java.util.Map;

/**
 * 冲突解决方案
 *  冲突的key选择进行删除，还是使用某个值
 * @author dingqi on 2024/6/1
 * @since 1.0.0
 */
public class ConfigSolveVO<K, V, I> {
    private K key;
    private Map<I, ConflictValue<V>> changeValues;
    private ConflictValue<V> mergeValue;

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public Map<I, ConflictValue<V>> getChangeValues() {
        return changeValues;
    }

    public void setChangeValues(Map<I, ConflictValue<V>> changeValues) {
        this.changeValues = changeValues;
    }

    public ConflictValue<V> getMergeValue() {
        return mergeValue;
    }

    public void setMergeValue(ConflictValue<V> mergeValue) {
        this.mergeValue = mergeValue;
    }
}
