package com.os.project1.BatchOperations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.os.project1.Commands.*;

public class Batch {
	String workingDir;
	ArrayList<Command> commands;
	Map<String, Command> commandmap;
	
	public Batch() {
		this.commands = new ArrayList<Command>();
		this.commandmap = new HashMap<String, Command>();
	}
	
	void addCommand(String name,Command command) {
		commands.add(command);
		commandmap.put(name, command);
	}

	String getWorkingDir() {
		return workingDir;
	}
	
	ArrayList<Command> getCommand() {
		return commands;
	}
}
