/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kfowler.cs_383;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class FileList {

  public static class TokenizerMapper 
       extends Mapper<Object, Text, Text, Text>{
    
    private Text word = new Text();
    private Text fileText = new Text();
    
    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
      StringTokenizer itr = new StringTokenizer(value.toString().toLowerCase());
      FileSplit fileSplit = (FileSplit)context.getInputSplit();
      String fileName = fileSplit.getPath().getName();
      fileText.set(fileName);
      
      while (itr.hasMoreTokens()) {
        word.set(itr.nextToken());
        context.write(word, fileText);
      }
    }
  }
  
  public static class FileNameReducer 
  	extends Reducer<Text,Text,Text,Text> {

	  public void reduce(Text key, Iterable<Text> values, Context context) 
			  throws IOException, InterruptedException {

	      boolean first = true;
	      List<String> fileList = new ArrayList<String>();
	      Text lastName = new Text("");
	      StringBuilder toReturn = new StringBuilder();
	      
	      // For some reason, using an Iterator<Text> values in a while loop with hasNext() does not work here.
	      for (Text fName : values) {
	    	  if (fName.equals(lastName)) {
	    		  // skip
	    		  continue;
	    	  }
	    	  lastName.set(fName);
	    	  fileList.add(fName.toString());
	          //if (!first)
	          //    toReturn.append(", ");
	          //first=false;
	          //toReturn.append(fName.toString());
	      }
	      
	      Collections.sort(fileList);
	      for (String file : fileList) {
	    	  if (!first)
	    		  toReturn.append(", ");
	    	  first = false;
	    	  toReturn.append(file);
	      }
	      context.write(key, new Text(toReturn.toString()));
      }
  }

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
    if (otherArgs.length < 2) {
      System.err.println("Usage: filelist <in> [<in>...] <out>");
      System.exit(2);
    }
//    @SuppressWarnings("deprecation")
//	Job job = new Job(conf, "file list");
// or
    Job job = Job.getInstance(conf, "file list");
    job.setJarByClass(FileList.class);
    job.setMapperClass(TokenizerMapper.class);
    //job.setCombinerClass(FileNameReducer.class);
    job.setReducerClass(FileNameReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(Text.class);
    for (int i = 0; i < otherArgs.length - 1; ++i) {
      FileInputFormat.addInputPath(job, new Path(otherArgs[i]));
    }
    FileOutputFormat.setOutputPath(job,
      new Path(otherArgs[otherArgs.length - 1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
