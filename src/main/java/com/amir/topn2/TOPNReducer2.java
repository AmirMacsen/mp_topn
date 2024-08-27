package com.amir.topn2;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TOPNReducer2 extends Reducer<Text, Text, Text, Text> {
    private Text outValue = new Text();
    private Map<String, Integer> map = new HashMap<>();
    private List<Map.Entry<String, Integer>> list = new ArrayList<>();
    @Override
    protected void reduce(Text key, Iterable<Text> values, Reducer<Text, Text, Text, Text>.Context context) throws IOException, InterruptedException {
        // key hadoop
        // value cat,2 cat,3 cat,4 cat,5 cat,6 cat,7 cat,8 cat,9 cat,10
        // 我们现在想要的是名称和个数，并对其进行排序，所以先用一个Map保存数据
        for (Text value : values) {
            // cat,2
            String[] splits = value.toString().trim().split(",");
            // cat 2
            String name = splits[0];
            int count = Integer.parseInt(splits[1]);
            // 添加到Map中
            map.put(name, count);
        }

        // 添加到list中并进行排序
        list.addAll(map.entrySet());
        // list 排序
        list.sort((o1, o2) -> o2.getValue() - o1.getValue());

        // 获取推荐的top好友
        String result = "";
        for (int i = 0; i < (Math.min(list.size(), 2)); i++) {
            // 输出
            result += list.get(i).getKey() + ",";
        }

        // 去除最后的逗号
        result = result.substring(0, result.length() - 1);
        outValue.set(result);

        context.write(key, outValue);
    }
}
