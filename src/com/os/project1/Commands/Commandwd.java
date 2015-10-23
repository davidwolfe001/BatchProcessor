package com.os.project1.Commands;

import java.io.File;
import java.util.Map;

import org.w3c.dom.Element;

import com.os.project1.Exceptions.ProcessException;

public class Commandwd extends Command {

	@Override
	public void describe() {
		System.out.println("Processing command: " + this.id + " with path: " + this.path);
	}

	@Override
	public void execute(String workingDir, Map<String, Command> commandmap) {
		
	    File dir = new File(workingDir);
		if(!dir.exists()) {
			System.out.println("Warning, working directory: " + workingDir + ", doesn't exist.");
			throw new ProcessException("Unable to find working directory with name: " + workingDir);
		}else {
			System.out.println("Wokring directory correctly found");
		}
		
	}
	
	@Override
	public void parse(Element elem) {
		this.id = elem.getAttribute("id");
		if (this.id == null || this.id.isEmpty()) {
			throw new ProcessException("Missing ID in Command");
		}
		
		String currentdir = System.getProperty("user.dir");
		this.path = currentdir + "/" + elem.getAttribute("path");
		if (elem.getAttribute("path") == null || elem.getAttribute("path").isEmpty()) {
			throw new ProcessException("Missing PATH in Command");
		}
	}

}
