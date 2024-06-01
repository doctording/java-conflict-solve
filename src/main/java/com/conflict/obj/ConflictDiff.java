package com.conflict.obj;

import java.util.List;
import java.util.Map;

/**
 * @author dingqi on 2024/6/1
 * @since 1.0.0
 */
public class ConflictDiff<K, V> {
    /**
     * 纯新增
     */
    Map<K, List<V>> addKeyChangeMap;
    /**
     * 纯修改
     */
    Map<K, List<V>> modifyKeyChangeMap;
    /**
     * 纯删除
     */
    Map<K, List<V>> deleteKeyChangeMap;
    /**
     * 有删除,有修改的
     */
    List<K> deleteModifyKeys;
    Map<K, List<V>> justModifyChangeMap;
    Map<K, List<V>> justDeleteChangeMap;

    public Map<K, List<V>> getAddKeyChangeMap() {
        return addKeyChangeMap;
    }

    public void setAddKeyChangeMap(Map<K, List<V>> addKeyChangeMap) {
        this.addKeyChangeMap = addKeyChangeMap;
    }

    public Map<K, List<V>> getModifyKeyChangeMap() {
        return modifyKeyChangeMap;
    }

    public void setModifyKeyChangeMap(Map<K, List<V>> modifyKeyChangeMap) {
        this.modifyKeyChangeMap = modifyKeyChangeMap;
    }

    public Map<K, List<V>> getDeleteKeyChangeMap() {
        return deleteKeyChangeMap;
    }

    public void setDeleteKeyChangeMap(Map<K, List<V>> deleteKeyChangeMap) {
        this.deleteKeyChangeMap = deleteKeyChangeMap;
    }

    public List<K> getDeleteModifyKeys() {
        return deleteModifyKeys;
    }

    public void setDeleteModifyKeys(List<K> deleteModifyKeys) {
        this.deleteModifyKeys = deleteModifyKeys;
    }

    public Map<K, List<V>> getJustModifyChangeMap() {
        return justModifyChangeMap;
    }

    public void setJustModifyChangeMap(Map<K, List<V>> justModifyChangeMap) {
        this.justModifyChangeMap = justModifyChangeMap;
    }

    public Map<K, List<V>> getJustDeleteChangeMap() {
        return justDeleteChangeMap;
    }

    public void setJustDeleteChangeMap(Map<K, List<V>> justDeleteChangeMap) {
        this.justDeleteChangeMap = justDeleteChangeMap;
    }
}
