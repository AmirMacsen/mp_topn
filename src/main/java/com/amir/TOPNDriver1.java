package com.amir;

import com.amir.topn1.TOPNMapper1;
import com.amir.topn1.TOPNReducer1;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.yarn.webapp.hamlet2.Hamlet;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class TOPNDriver1
{
    public static void main( String[] args ) throws Exception {
        //1.判断输入输出路径是否合法
        if(args==null||args.length!=2){
            System.out.println("Usage:yarn jar hadoop_topn.jar com.amir.TOPDriver1 <inputPath> <outputPath>");
            System.exit(1);
        }
        Configuration conf = new Configuration(true);
        conf.set("mapreduce.framework.name", "local");
        System.setProperty("HADOOP_USER_NAME", "root");
        conf.set("mapreduce.cluster.local.dir","/Users/maningyu/workspace/javaprojects/hadoop_temperature/src/main/resources");

        Job job = Job.getInstance(conf);
        job.setJarByClass(TOPNDriver1.class);
        job.setJobName("TOPNDriver1-MR1");

        job.setMapperClass(TOPNMapper1.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        job.setReducerClass(TOPNReducer1.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);


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
