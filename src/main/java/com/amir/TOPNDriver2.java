package com.amir;

import com.amir.topn1.TOPNMapper1;
import com.amir.topn1.TOPNReducer1;
import com.amir.topn2.TOPNMapper2;
import com.amir.topn2.TOPNReducer2;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * Hello world!
 *
 */
public class TOPNDriver2
{
    public static void main( String[] args ) throws Exception {
        //1.判断输入输出路径是否合法
        if(args==null||args.length!=2){
            System.out.println("Usage:yarn jar hadoop_topn.jar com.amir.TOPDriver2 <inputPath> <outputPath>");
            System.exit(1);
        }
        Configuration conf = new Configuration(true);
        conf.set("mapreduce.framework.name", "local");
        System.setProperty("HADOOP_USER_NAME", "root");
        conf.set("mapreduce.cluster.local.dir","/Users/maningyu/workspace/javaprojects/hadoop_temperature/src/main/resources");

        Job job = Job.getInstance(conf);
        job.setJarByClass(TOPNDriver2.class);
        job.setJobName("TOPNDriver2-MR2");

        // 将每行的第一个制表符之前的当做key，将第一个制表符之后的当做value
        job.setInputFormatClass(KeyValueTextInputFormat.class);

        job.setMapperClass(TOPNMapper2.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        job.setReducerClass(TOPNReducer2.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);


        FileInputFormat.addInputPath(job,new Path(args[0]));

        Path outputPath = new Path(args[1]);
        FileSystem fs = outputPath.getFileSystem(conf);
        if(fs.exists(outputPath)){
            fs.delete(outputPath,true);
        }
        FileOutputFormat.setOutputPath(job,outputPath);
        job.setNumReduceTasks(4);
        job.waitForCompletion(true);
    }
}
