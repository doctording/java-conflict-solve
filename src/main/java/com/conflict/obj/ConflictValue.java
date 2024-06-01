package com.conflict.obj;

/**
 * 冲突值的处理，用某个值或者直接删除
 * @author dingqi on 2024/6/1
 * @since 1.0.0
 */
public class ConflictValue<V> {
    V value;
    Boolean deleted;

    public ConflictValue() {
    }

    public ConflictValue(V value, Boolean deleted) {
        this.value = value;
        this.deleted = deleted;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
