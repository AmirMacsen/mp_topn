package com.amir.topn1;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class TOPNMapper1 extends Mapper<LongWritable , Text , Text, IntWritable> {
    private Text outKey = new Text();
    private IntWritable outValue0 = new IntWritable(0);
    private IntWritable outValue1 = new IntWritable(1);
    @Override
    protected void map(LongWritable key, Text value, Context context) throws java.io.IOException, InterruptedException {
        //tom hello hadoop cat
        //按照空格进行拆分
        String line = value.toString().trim();
        String[] words = line.split(" ");

        // 先处理直接关系 tom与其他人都是直接好友
        for (int i = 1; i < words.length; i++) {
            // 注意拼接，不管是AB还是BA，都是AB
            outKey.set(getFromName(words[0], words[i]));
            context.write(outKey, outValue0); // 表示直接好友

            // 再处理间接好友  hadoop:hello cat:hello cat:hadoop
            for (int j = i+1; j < words.length; j++) {
                outKey.set(getFromName(words[i], words[j]));
                context.write(outKey, outValue1);
            }
        }
    }

    private String getFromName(String word, String word1) {
        if (word.compareTo(word1) < 0) {
            return word+":"+word1;
        } else {
            return word1+":"+word;
        }
    }


}
