package com.conflict;

import com.conflict.obj.AbstractConflictSubject;

import java.util.Objects;

/**
 * 一个实际冲突类，可以自己添加属性
 * 本次是常见的String类型的key Value的对象
 * @author dingqi on 2024/6/1
 * @since 1.0.0
 */
public class ConflictSubject extends AbstractConflictSubject<String, String> {

    /**
     * 自定义添加一个描述属性
     */
    String desc;

    public ConflictSubject() {
    }

    public ConflictSubject(String key, String value) {
        super(key, value);
    }

    public ConflictSubject(String key, String value, String desc) {
        super(key, value);
        this.desc = desc;
    }

    @Override
    public boolean valueEquals(String otherValue) {
        return Objects.equals(this.getValue(), otherValue);
    }

    @Override
    public int compareValues(String otherValue) {
        return Objects.compare(this.getValue(), otherValue, String::compareToIgnoreCase);
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
