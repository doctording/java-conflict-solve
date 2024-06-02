package com.your.project;

import com.conflict.obj.AbstractConflictSubject;

import java.util.Objects;

/**
 * @author dingqi on 2024/6/2
 * @since 1.0.0
 */
public class YourPropertyModify extends AbstractConflictSubject<Integer, String> {
//    Integer key;
//    String value;
    String otherProperty;

    public YourPropertyModify(Integer key, String value) {
        super(key, value);
    }

    @Override
    public boolean valueEquals(String otherValue) {
        return Objects.equals(this.getValue(), otherValue);
    }

    @Override
    public int compareValues(String otherValue) {
        return Objects.compare(this.getValue(), otherValue, String::compareToIgnoreCase);
    }
}
