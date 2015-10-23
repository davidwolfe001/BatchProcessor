package com.os.project1.Commands;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.w3c.dom.Element;

import com.os.project1.Exceptions.ProcessException;

public class Commandcmd extends Command {

	public String args;
	public String in;
	public String out;
	
	@Override
	public void describe() {
		if (this.args != null) {
			System.out.println("Processing command: " + this.id + ", with path: " + this.path + ", with argument: " + this.args);
		}else {
			System.out.println("Processing command: " + this.id + ", with path: " + this.path);
		}
	}

	@Override
	public void execute(String workingDir, Map<String, Command> commandmap) {
		
		if (this.out == null || this.out.isEmpty()) {
			throw new ProcessException("Missing OUTPUT in CMD command");
		}
		 
		List<String> command = new ArrayList<String>();
		command.add(this.path);
		if(this.args != null) {
			//mac cannot recognize the whole string "-jar addLines.jar"
			StringTokenizer st = new StringTokenizer(this.args);
			while(st.hasMoreTokens()) {
				command.add(st.nextToken());
			}
		}
		
		ProcessBuilder builder = new ProcessBuilder();
		builder.command(command);
		builder.directory(new File(workingDir));
		File wd = builder.directory();
		builder.redirectErrorStream(true);
		
		if (this.in != null) {
			if(commandmap.containsKey(this.in)) {
				String inputfile = commandmap.get(this.in).path;
				File inFile = new File(wd, inputfile);
				if(inFile.exists()) {
					builder.redirectInput(inFile);
					System.out.println("Find input file: " + inputfile);
				}else {
					throw new ProcessException("Unable to find IN command File with path: " + inputfile);
				}
				
			}else {
				System.out.println("Error Processing Batch Unable to locate IN command File with id: "+ this.in);
				throw new ProcessException("Unable to locate IN command File with id: " + this.in);
			}
			
		}
		
		if(commandmap.containsKey(this.out)){
			String outputfile = commandmap.get(this.out).path;
			File outFile = new File(wd, outputfile);
			builder.redirectOutput(outFile);
			System.out.println("Result will be sent to output file: " + outFile);
		}else {
			System.out.println("Error Processing Batch Unable to locate OUT command File with id: "+ this.out);
			throw new ProcessException("Unable to locate OUT command File with id: " + this.out);
		}
		
		Process process = null;
		try {
			process = builder.start();
			if (this.args != null) {
				System.out.println("Currently running command: " + this.id + ", with path: " + this.path + ", with argument: " + this.args);
			}else {
				System.out.println("Currently running command: " + this.id + ", with path: " + this.path);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			process.waitFor();
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Successfully executed command: " + this.id);
		
	}
 
	
	@Override
	public void parse(Element elem) {
		this.id = elem.getAttribute("id");
		
	    this.path = elem.getAttribute("path");

		if(elem.hasAttribute("args")) {
			this.args = elem.getAttribute("args");
		}else {
			this.args = null;
		}
		
		if(elem.hasAttribute("in")) {
			this.in = elem.getAttribute("in");
		}else {
			this.in = null;
		}
		
		this.out = elem.getAttribute("out");
	}

}
