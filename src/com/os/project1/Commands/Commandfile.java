package com.os.project1.Commands;

import java.io.File;
import java.util.Map;

public class Commandfile extends Command {

	@Override
	public void describe() {
		System.out.println("Processing command: " + this.id + " with path: " + this.path);
	}

	@Override
	public void execute(String workingDir, Map<String, Command> commandmap) {
		File file = new File(workingDir, this.path);
		if(file.exists()) {
			System.out.println("File: "+ this.path + " successfully detected");
		}else {
			System.out.println("File: "+ this.path + " doesn't exist yet");
		}
	}

}
