package com.conflict.obj;

/**
 * @author dingqi on 2024/6/1
 * @since 1.0.0
 */
public abstract class AbstractConflictSubject<K, V> {
    /**
     * 冲突key
     */
    protected K key;
    /**
     * 冲突value
     */
    protected V value;

    public AbstractConflictSubject() {
    }

    public AbstractConflictSubject(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    /**
     *  抽象方法来判断两个值的相等性
     */
    public abstract boolean valueEquals(V otherValue);

    /**
     * 抽象方法来比较两个值的大小
     */
    public abstract int compareValues(V otherValue);
}
