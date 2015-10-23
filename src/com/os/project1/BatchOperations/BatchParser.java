package com.os.project1.BatchOperations;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import java.io.File;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Document;

import com.os.project1.Commands.*;
import com.os.project1.Exceptions.ProcessException;
 
public class BatchParser {
	public BatchParser(){
	}
	
	public Batch buildBatch(File filename) {
		  Batch batch = new Batch();
		  try {

				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(filename);
				doc.getDocumentElement().normalize();

				Element root = doc.getDocumentElement();
				System.out.println("Processing Batch file:" + root.getNodeName());
				System.out.println("----------------------------");
				
				Node nNode = root.getFirstChild();
				while ( nNode.getNodeName() == "#text") {
					nNode = nNode.getNextSibling();
				}
				if (nNode.getNodeName() != "wd") {
					throw new ProcessException("unable to find the working directory from the first node" + nNode.getNodeName());
				}
				Element elem = (Element) nNode;
				String currentdir = System.getProperty("user.dir");
				batch.workingDir = currentdir + "/" + elem.getAttribute("path");
				while (nNode != null) {
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						elem = (Element) nNode;
						batch.addCommand(elem.getAttribute("id"), buildCommand(elem));
					    nNode = nNode.getNextSibling();
					    while ( nNode != null && nNode.getNodeName() == "#text") {
						    nNode = nNode.getNextSibling();
					    }
				    } 
				}
			    }catch (Exception e) {
				e.printStackTrace();
			    }
		  return batch;
	  }
	
	private Command buildCommand(Element elem) {
		String cmdName = elem.getTagName();
		if (cmdName == null) {
			throw new ProcessException("unable to parse command from " + elem.getTextContent());
		}
		if ("wd".equalsIgnoreCase(cmdName)) {
			System.out.println("Parsing wd");
			Command cmd = new Commandwd();
			cmd.parse(elem);
			return cmd;
		}
		else if ("file".equalsIgnoreCase(cmdName)) {
			System.out.println("Parsing file");
			Command cmd = new Commandfile();
			cmd.parse(elem);
			return cmd;
		}
		else if ("cmd".equalsIgnoreCase(cmdName)) {
			System.out.println("Parsing cmd");
			Command cmd = new Commandcmd();
			cmd.parse(elem);
			return cmd;
		}
		else if ("pipe".equalsIgnoreCase(cmdName)) {
			System.out.println("Parsing pipe");
			Command cmd = new Commandpipe();
			cmd.parse(elem);
			return cmd;
		}else {
			throw new ProcessException("Unknown command " + cmdName + " from: " + elem.getBaseURI());
		}
	}
}