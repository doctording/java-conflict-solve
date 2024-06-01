package com.conflict.obj;

import java.util.List;
import java.util.Map;

/**
 * @author dingqi on 2024/6/1
 * @since 1.0.0
 */
public class ConflictDiff<K, I> {
    /**
     * 纯新增
     */
    Map<K, List<I>> addKeyChangeMap;
    /**
     * 纯修改
     */
    Map<K, List<I>> modifyKeyChangeMap;
    /**
     * 纯删除
     */
    Map<K, List<I>> deleteKeyChangeMap;
    /**
     * 有删除,有修改的
     */
    List<K> deleteModifyKeys;
    Map<K, List<I>> justModifyChangeMap;
    Map<K, List<I>> justDeleteChangeMap;

    public Map<K, List<I>> getAddKeyChangeMap() {
        return addKeyChangeMap;
    }

    public void setAddKeyChangeMap(Map<K, List<I>> addKeyChangeMap) {
        this.addKeyChangeMap = addKeyChangeMap;
    }

    public Map<K, List<I>> getModifyKeyChangeMap() {
        return modifyKeyChangeMap;
    }

    public void setModifyKeyChangeMap(Map<K, List<I>> modifyKeyChangeMap) {
        this.modifyKeyChangeMap = modifyKeyChangeMap;
    }

    public Map<K, List<I>> getDeleteKeyChangeMap() {
        return deleteKeyChangeMap;
    }

    public void setDeleteKeyChangeMap(Map<K, List<I>> deleteKeyChangeMap) {
        this.deleteKeyChangeMap = deleteKeyChangeMap;
    }

    public List<K> getDeleteModifyKeys() {
        return deleteModifyKeys;
    }

    public void setDeleteModifyKeys(List<K> deleteModifyKeys) {
        this.deleteModifyKeys = deleteModifyKeys;
    }

    public Map<K, List<I>> getJustModifyChangeMap() {
        return justModifyChangeMap;
    }

    public void setJustModifyChangeMap(Map<K, List<I>> justModifyChangeMap) {
        this.justModifyChangeMap = justModifyChangeMap;
    }

    public Map<K, List<I>> getJustDeleteChangeMap() {
        return justDeleteChangeMap;
    }

    public void setJustDeleteChangeMap(Map<K, List<I>> justDeleteChangeMap) {
        this.justDeleteChangeMap = justDeleteChangeMap;
    }
}
