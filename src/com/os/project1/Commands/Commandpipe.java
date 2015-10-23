package com.os.project1.Commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.os.project1.Exceptions.ProcessException;

public class Commandpipe extends Command {
	public Commandpipecmd cmd1;
	public Commandpipecmd cmd2;

	@Override
	public void describe() {
		System.out.println("Processing Pipe command: " + this.id);
	}

	@Override
	public void execute(String workingDir, Map<String, Command> commandmap)  {
		List<String> command1 = new ArrayList<String>();
		
		command1.add(this.cmd1.path); 
		System.out.println("Recognizing the first command path: " + this.cmd1.path);
		if(this.cmd1.args != null) {
			//mac cannot recognize the whole string "-jar addLines.jar"
			StringTokenizer st = new StringTokenizer(this.cmd1.args);
			while(st.hasMoreTokens()) {
				String token = st.nextToken();
				System.out.println("Recognizing the first command argument: " + token);
				command1.add(token);
			}
		}
		
		List<String> command2 = new ArrayList<String>();
		command2.add(this.cmd2.path);
		System.out.println("Recognizing the second command path: " + this.cmd2.path);
		if(this.cmd2.args != null) {
			StringTokenizer st = new StringTokenizer(this.cmd2.args);
			while(st.hasMoreTokens()) {
				String token = st.nextToken();
				System.out.println("Recognizing the second command argument: " + token);
				command2.add(token);
			}
		}

		System.out.println("Initizling the pipe command");
		ProcessBuilder builder1 = new ProcessBuilder(command1);
		builder1.directory(new File(workingDir));
		File wd = builder1.directory();
		builder1.redirectErrorStream(true);
		
		ProcessBuilder builder2 = new ProcessBuilder(command2);
		builder2.directory(wd);
		builder2.redirectErrorStream(true);

		Process process1;
		Process process2;
		try {
			process1 = builder1.start();
			
			OutputStream os1 = process1.getOutputStream();
			System.out.println("Loading the input for first command");
			FileInputStream fis = new FileInputStream(new File(wd, commandmap.get(this.cmd1.in).path));
			copyStreams(fis,os1);
			System.out.println("First Command is successfully running for pipe");
			
			InputStream is1 = process1.getInputStream();
			process2 = builder2.start();
			OutputStream os2 = process2.getOutputStream();
			copyStreams(is1,os2);
			System.out.println("Loading the output of the first command as input for the second command");
			
			File outfile = new File(wd,commandmap.get(this.cmd2.out).path);
			FileOutputStream fos = new FileOutputStream(outfile);
			InputStream is2 = process2.getInputStream();
			copyStreams(is2,fos);
			System.out.println("The pipe commands successfully executed");
			
			System.out.println("Program terminated!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Copy the contents of the input stream to the output stream in
	 * separate thread. The thread ends when an EOF is read from the 
	 * input stream.   
	 */
	static void copyStreams(final InputStream is, final OutputStream os) {
		Runnable copyThread = (new Runnable() {
			@Override
			public void run()
			{
				try {
					int achar;
					while ((achar = is.read()) != -1) {
						os.write(achar);
					}
					os.close();
				}
				catch (IOException ex) {
					throw new RuntimeException(ex.getMessage(), ex);
				}
			}
		});
		new Thread(copyThread).start();
	}

	@Override
	public void parse(Element elem) {
		this.id = elem.getAttribute("id");
		Node firstnode = elem.getFirstChild();
		while ( firstnode.getNodeName() == "#text") {
			firstnode = firstnode.getNextSibling();
		}
		
		Node secondnode = firstnode.getNextSibling();
		while (secondnode.getNodeName() == "#text") {
			secondnode = secondnode.getNextSibling();
		}
		
		if (firstnode.getNodeType() == Node.ELEMENT_NODE) {
			Element elem1 = (Element) firstnode;
			if ("cmd".equalsIgnoreCase(elem1.getNodeName())) {
				System.out.println("Parsing First Pipe CMD: " + elem1.getAttribute("id"));
				Commandpipecmd cmd = new Commandpipecmd();
				cmd.parse(elem1);
				this.cmd1 = cmd;
				if(this.cmd1.out != null) {
					throw new ProcessException("Error: The first command of pipe has been set an output file: " + this.cmd1.out);
				}
			} else {
				throw new ProcessException("unable to parse first command of pipe from " + elem1.getTextContent());
			}
	    } else {
	    	throw new ProcessException("unable to find first command of pipe");
	    }
		if (secondnode.getNodeType() == Node.ELEMENT_NODE) {
			Element elem2 = (Element) secondnode;
			if ("cmd".equalsIgnoreCase(elem2.getNodeName())) {
				System.out.println("Parsing Second Pipe CMD: " + elem2.getAttribute("id"));
				Commandpipecmd cmd = new Commandpipecmd();
				cmd.parse(elem2);
				this.cmd2 = cmd;
				if(this.cmd2.in != null) {
					throw new ProcessException("Error: The second command of pipe has been set an input file: " + this.cmd2.in);
				}
			} else {
				throw new ProcessException("unable to parse second command of pipe");
			}
		} else {
	    	throw new ProcessException("unable to find second command of pipe from " + secondnode.getNodeValue());
			
	    }
		
		
	}

}
