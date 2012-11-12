/*******************************************************************************
 * Copyright (c) 2012 bamboo-status. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Andre Albert - initial API and implementation
 *******************************************************************************/
package com.prodyna.bamboo.status.scan;

import java.util.HashMap;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


/**
 * @author Andre Albert
 */
public class BambooPlanResultHandler extends DefaultHandler {

	private Map<String, Boolean> planResults = new HashMap<String, Boolean>();
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if (qName.equals("result")) {
			planResults.put(attributes.getValue("key"), attributes.getValue("state").equals("Successful"));
		}
	}
	
	public Map<String, Boolean> getPlanResults() {
		return this.planResults;
	}
	public void reset() {
		this.planResults.clear();
	}
}
