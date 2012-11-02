/*******************************************************************************
 * Copyright (c) 2012 Andre Albert. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Andre Albert - initial API and implementation
 *******************************************************************************/
package com.prodyna.bamboo.status.scan;

import java.util.ArrayList;
import java.util.Collection;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Andre Albert
 */
public class BambooProjectResultHandler extends DefaultHandler {

	private Collection<String> projectResults = new ArrayList<String>();
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if (qName.equals("project")) {
			projectResults.add(attributes.getValue("key"));
		}
	}
	
	public Collection<String> getProjectResults() {
		return this.projectResults;
	}
	public void reset() {
		this.projectResults.clear();
	}
}
