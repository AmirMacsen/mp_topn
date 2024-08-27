package com.amir.topn1;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * 输出a:b sum
 */
public class TOPNReducer1 extends Reducer<Text, IntWritable, Text, IntWritable> {
    private IntWritable outValue = new IntWritable();

    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws java.io.IOException, InterruptedException
    {
        int sum = 0;
        boolean zeroExist = false;
        for (IntWritable value : values) {
            if (value.get() == 0) {
                zeroExist = true;
                break;
            }
            sum += value.get();
        }
        if (!zeroExist) {
            outValue.set(sum);
            context.write(key, outValue);
        }
    }
}
