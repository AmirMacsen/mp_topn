package com.amir.topn2;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * 输入是mr的输出，所以输入都是Text
 */
public class TOPNMapper2 extends Mapper<Text, Text, Text, Text> {
    private Text outKey = new Text();
    private Text outValue = new Text();
    @Override
    protected void map(Text key, Text value, Context context) throws java.io.IOException, InterruptedException {
        // 输出cat:hadoop 2  输出 cat hadoop,2
        // 切割key
        String[] splits = key.toString().trim().split(":");
        // 输出cat hadoop,2
        outKey.set(splits[0]);
        outValue.set(splits[1] + "," + value.toString());
        context.write(outKey, outValue);

        // 输出hadoop cat,2
        outKey.set(splits[1]);
        outValue.set(splits[0] + "," + value.toString());
        context.write(outKey, outValue);
    }
}
