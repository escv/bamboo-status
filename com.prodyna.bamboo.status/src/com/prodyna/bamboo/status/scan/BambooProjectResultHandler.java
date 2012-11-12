/*******************************************************************************
 * Copyright (c) 2012 bamboo-status. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Andre Albert - initial API and implementation
 *******************************************************************************/
package com.prodyna.bamboo.status.scan;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.prodyna.bamboo.status.model.Project;

/**
 * @author Andre Albert
 */
public class BambooProjectResultHandler extends DefaultHandler {

	private List<Project> projectResults = new ArrayList<Project>();
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if (qName.equals("project")) {
			projectResults.add(new Project(attributes.getValue("key"), attributes.getValue("name")));
		}
	}
	
	public List<Project> getProjectResults() {
		return this.projectResults;
	}

	public void reset() {
		this.projectResults.clear();
	}
}
