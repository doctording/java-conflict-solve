package com.conflict.obj;

import java.util.List;
import java.util.Map;

/**
 * @author dingqi on 2024/6/1
 * @since 1.0.0
 */
public class ConflictHasAndRecord<K, V, I> {
    Map<K, List<ConflictRecord<K, V, I>>> keyRecordMap;
    Boolean hasConflict;

    public Map<K, List<ConflictRecord<K, V, I>>> getKeyRecordMap() {
        return keyRecordMap;
    }

    public void setKeyRecordMap(Map<K, List<ConflictRecord<K, V, I>>> keyRecordMap) {
        this.keyRecordMap = keyRecordMap;
    }

    public Boolean getHasConflict() {
        return hasConflict;
    }

    public void setHasConflict(Boolean hasConflict) {
        this.hasConflict = hasConflict;
    }
}
