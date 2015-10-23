package com.os.project1.Commands;

import java.util.Map;

import org.w3c.dom.Element;
import com.os.project1.Exceptions.ProcessException;

public abstract class Command {
	public String id;
	public String path;
	
	public void describe(){
		
	};
	
	public void execute(String workingDir, Map<String, Command> commandmap){
		
	};
	
	public void parse(Element elem) {
		this.id = elem.getAttribute("id");
		if (this.id == null || this.id.isEmpty()) {
			throw new ProcessException("Missing ID in Command");
		}
		
		this.path = elem.getAttribute("path");
		if (this.path == null || this.path.isEmpty()) {
			throw new ProcessException("Missing PATH in Command");
		}
	}
}
