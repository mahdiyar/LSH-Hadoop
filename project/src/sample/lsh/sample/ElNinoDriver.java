package lsh.sample;

import java.io.File;

import lsh.hadoop.CornerMapper;
import lsh.hadoop.CornerReducer;
import lsh.sample.ElNinoTextFormat;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

/*
 * Corner-collecting version- set of corners, each with points.
 */

public class ElNinoDriver {
	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
		if (otherArgs.length != 2) {
			System.err.println("Usage: CornerDriver <in> <out>");
			System.exit(2);
		}
		Job job = new Job(conf, "El Nino mini");
		//	    job.setJarByClass(CornerDriver.class);
		job.setInputFormatClass(ElNinoTextFormat.class);
		job.setMapperClass(CornerMapper.class);
		job.setReducerClass(CornerReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
		rmdir(new File(otherArgs[1]));
		FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}

	static void rmdir(File dir) {
		if (null == dir.listFiles())
			return;
		for(File f: dir.listFiles()) {
			f.delete();
		}
		dir.delete();
	}
}