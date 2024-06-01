## 一个通用的配置冲突解决方案

### 了解问题和场景(和git类似)

实际场景：一个项目的配置,即`List<key,value>`, 然后会有多人修改发布，每个人有自己的变更配置，可以记为`Map<ChangId, List<key，value>>`

需要考虑的就是如何处理这些变更配置和原有的主干配置的冲突问题

---

作为一个多变更配置发布和冲突解决产品，考虑如下的处理

1. 多人发布--->检测无冲突--->直接合并配置并告知--->发布
2. 多人发布--->检测有冲突--->通知处理--->依据冲突处理进行合并配置并告知--->发布

所以需要有冲突显示和解决的产品、需要有显示最终配置的产品、需要有配置diff的产品

### 冲突场景

考虑实际的场景就如下:

1. 新增key，只有一个变更
2. 新增key，多个变更新增相同的这个key ------冲突
3. 修改key，只有一个变更修改
4. 修改key，多个变更修改相同的这个key ------- 冲突
5. 删除key，一个变更删除
6. 删除key，多个变更删除
7. 同一个key，存在1个变更修改（可能多个变更），另一个变更删除（可能多个变更） ------- 冲突


### 代码处理例子

```java
public class UtilConflictDiffHasTest {
    @Test
    public void test() {
        List<AbstractConflictSubject<String, String>> propertyBase = Arrays.asList(
                new ConflictSubject("k1", "v1"),
                new ConflictSubject("k2", "v2"),
                new ConflictSubject("k3", "v3"));
        List<AbstractConflictSubject<String, String>> propertyList1 = Arrays.asList(
                new ConflictSubject("k1", "v1"),
                new ConflictSubject("k3", "v3"),
                new ConflictSubject("k4", "v4"));
        List<AbstractConflictSubject<String, String>> propertyList2 = Arrays.asList(
                new ConflictSubject("k1", "v1"),
                new ConflictSubject("k3", "v3"),
                new ConflictSubject("k4", "v5"));
        List<AbstractConflictSubject<String, String>> propertyList3 = Arrays.asList(
                new ConflictSubject("k1", "v1"),
                new ConflictSubject("k2", "v2222"),
                new ConflictSubject("k3", "v3"));
        List<AbstractConflictSubject<String, String>> propertyList4 = Arrays.asList(
                new ConflictSubject("k2", "v2"),
                new ConflictSubject("k3", "v3"));
        List<AbstractConflictSubject<String, String>> propertyList5 = Arrays.asList(
                new ConflictSubject("k2", "v2"),
                new ConflictSubject("k3", "v3"));
        List<AbstractConflictSubject<String, String>> propertyList6 = Arrays.asList(
                new ConflictSubject("k1", "v1"),
                new ConflictSubject("k2", "v2"),
                new ConflictSubject("k3", "v3"),
                new ConflictSubject("k6", "v3"));
        List<AbstractConflictSubject<String, String>> propertyList7 = Arrays.asList(
                new ConflictSubject("k1", "v1"),
                new ConflictSubject("k2", "v2"),
                new ConflictSubject("k3", "v333"));
        Map<Long, List<AbstractConflictSubject<String, String>>> changeMap = new HashMap<>();
        changeMap.put(1L, propertyList1);
        changeMap.put(2L, propertyList2);
        changeMap.put(3L, propertyList3);
        changeMap.put(4L, propertyList4);
        changeMap.put(5L, propertyList5);
        changeMap.put(6L, propertyList6);
        changeMap.put(7L, propertyList7);
        // 获取冲突
        // 添加：k4, k6
        // 修改: k3
        // 删除: k1
        // 修改删除并存: k2
        ConflictDiff<String, Long> conflictDiff = ConflictUtils.conflictKeyOfMultiChange(propertyBase, changeMap);
        assertNotNull(conflictDiff);

        // 判断有冲突
        ConflictHasAndRecord<String, String, Long> conflictHasAndRecord =
                ConflictUtils.hasConflict(conflictDiff, changeMap);
        assertTrue(conflictHasAndRecord.getHasConflict());
        Map<String, List<ConflictRecord<String, String, Long>>> keyChangeMap = conflictHasAndRecord.getKeyRecordMap();
        assertTrue(keyChangeMap.containsKey("k4"));
        assertTrue(keyChangeMap.containsKey("k2"));
    }

    @Test
    public void test2(){
        List<AbstractConflictSubject<String, String>> propertyBase = Arrays.asList(
                new ConflictSubject("k1", "v1"),
                new ConflictSubject("k2", "v2"),
                new ConflictSubject("k3", "v3"));
        List<AbstractConflictSubject<String, String>> propertyList1 = Arrays.asList(
                new ConflictSubject("k1", "v1"),
                new ConflictSubject("k2", "v2"),
                new ConflictSubject("k3", "v3"),
                new ConflictSubject("k4", "v4"));
        List<AbstractConflictSubject<String, String>> propertyList2 = Arrays.asList(
                new ConflictSubject("k2", "v2"),
                new ConflictSubject("k3", "v3"),
                new ConflictSubject("k4", "v4"));
        List<AbstractConflictSubject<String, String>> propertyList3 = Arrays.asList(
                new ConflictSubject("k2", "v2222"),
                new ConflictSubject("k3", "v3"));
        Map<Long, List<AbstractConflictSubject<String, String>>> changeMap = new HashMap<>();
        changeMap.put(1L, propertyList1);
        changeMap.put(2L, propertyList2);
        changeMap.put(3L, propertyList3);
        // 获取冲突
        // 添加：k4
        // 修改: k2
        // 删除: k1
        // 修改删除并存: 无
        ConflictDiff<String, Long> conflictDiff = ConflictUtils.conflictKeyOfMultiChange(propertyBase, changeMap);
        assertNotNull(conflictDiff);

        // 判断无冲突
        ConflictHasAndRecord<String, String, Long> conflictHasAndRecord =
                ConflictUtils.hasConflict(conflictDiff, changeMap);
        assertFalse(conflictHasAndRecord.getHasConflict());
    }
}
```
