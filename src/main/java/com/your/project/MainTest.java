package com.your.project;

import com.conflict.obj.AbstractConflictSubject;
import com.conflict.obj.ConflictDiff;
import com.conflict.obj.ConflictHasAndRecord;
import com.conflict.utils.ConflictMergeUtils;
import com.conflict.utils.ConflictUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dingqi on 2024/6/2
 * @since 1.0.0
 */
public class MainTest {

    static void printKV(List<AbstractConflictSubject<Integer, String>> list) {
        for (AbstractConflictSubject<Integer, String> subject : list) {
            System.out.println(String.format("<%d:%s>", subject.getKey(), subject.getValue()));
        }
    }

    public static void main(String[] args) {
        // 基线
        YourPropertyModify yourPropertyModify1 = new YourPropertyModify(1, "1");
        YourPropertyModify yourPropertyModify2 = new YourPropertyModify(2, "2");
        List<AbstractConflictSubject<Integer, String>> baseList = Arrays.asList(yourPropertyModify1,
                yourPropertyModify2);
        // 操作人1
        YourPropertyModify yourPropertyModify3 = new YourPropertyModify(1, "1");
        YourPropertyModify yourPropertyModify4 = new YourPropertyModify(2, "2");
        YourPropertyModify yourPropertyModify5 = new YourPropertyModify(3, "3");
        List<AbstractConflictSubject<Integer, String>> curList1 = Arrays.asList(yourPropertyModify3,
                yourPropertyModify4, yourPropertyModify5);
        // 操作人2
        YourPropertyModify yourPropertyModify6 = new YourPropertyModify(1, "1");
        YourPropertyModify yourPropertyModify7 = new YourPropertyModify(2, "22");
        List<AbstractConflictSubject<Integer, String>> curList2 = Arrays.asList(yourPropertyModify6,
                yourPropertyModify7);

        Map<String, List<AbstractConflictSubject<Integer, String>>> userChanges = new HashMap<>(4);
        userChanges.put("user1", curList1);
        userChanges.put("user2", curList2);

        System.out.println("基线<key,value>:");
        printKV(baseList);
        System.out.println("用户1<key,value>修改后如下");
        printKV(curList1);
        System.out.println("用户2<key,value>修改后如下:");
        printKV(curList2);

        // 冲突判断
        ConflictDiff<Integer, String> conflictDiff =
                ConflictUtils.conflictKeyOfMultiChange(baseList, userChanges);
        ConflictHasAndRecord<Integer, String, String> conflictHasAndRecord
                = ConflictUtils.hasConflict(conflictDiff, userChanges);
        System.out.println("---------------------------" );
        System.out.print("冲突情况:" );
        System.out.println(Boolean.TRUE.equals(conflictHasAndRecord.getHasConflict()) ? "有":"无");

        List<AbstractConflictSubject<Integer, String>> finalProperty = ConflictMergeUtils.mergeProperty(conflictDiff, baseList, userChanges);
        System.out.println("合并后的<key,value>情况:");
        printKV(finalProperty);
    }


}
