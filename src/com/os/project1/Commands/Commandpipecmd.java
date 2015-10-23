package com.os.project1.Commands;

import org.w3c.dom.Element;

public class Commandpipecmd extends Command {
	
	public String args;
	public String in;
	public String out;
	
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
		
		if(elem.hasAttribute("out")) {
			this.out = elem.getAttribute("out");
		}else {
			this.out = null;
		}
	}
}
