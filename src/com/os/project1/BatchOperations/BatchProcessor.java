package com.os.project1.BatchOperations;

import java.io.File;
import java.io.IOException;

import com.os.project1.Exceptions.ProcessException;

public class BatchProcessor {
	public static void main(String args[]) throws IOException {
		
		String currentdir = System.getProperty("user.dir");
		File runningxml = new File(currentdir + "/" +args[0]);
		
		System.out.println("Current directory =" + currentdir + "\n");
		
		BatchParser batchparser = new BatchParser();
		Batch batch = batchparser.buildBatch(runningxml);
		executebatch(batch);
	}
	
	public static void executebatch(Batch batch) {
		if(!batch.workingDir.contains(batch.commands.get(0).path)) {
			throw new ProcessException("unable to find the working directory from the first command path: " + batch.commands.get(0).path);
		}
		for(int i=0; i<batch.commands.size(); i++) {
		    batch.commands.get(i).describe();
			batch.commands.get(i).execute(batch.workingDir, batch.commandmap);
		}
		System.out.println("----------------------------");
		System.out.println("Batch finished successfully.");
	}
}
