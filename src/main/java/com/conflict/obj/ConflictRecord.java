package com.conflict.obj;

/**
 * @author dingqi on 2024/6/1
 * @since 1.0.0
 */
public class ConflictRecord<K, V, I> {
    private K key;
    private I changeId;
    ConflictValue<V> changeValue;
    ConflictValue<V> mergeValue;

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public I getChangeId() {
        return changeId;
    }

    public void setChangeId(I changeId) {
        this.changeId = changeId;
    }

    public ConflictValue<V> getChangeValue() {
        return changeValue;
    }

    public void setChangeValue(ConflictValue<V> changeValue) {
        this.changeValue = changeValue;
    }

    public ConflictValue<V> getMergeValue() {
        return mergeValue;
    }

    public void setMergeValue(ConflictValue<V> mergeValue) {
        this.mergeValue = mergeValue;
    }
}
