/*******************************************************************************
 * Copyright (c) 2012 Andre Albert. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Andre Albert - initial API and implementation
 *******************************************************************************/
package com.prodyna.bamboo.status.scan;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.eclipse.core.runtime.Status;

import com.prodyna.bamboo.status.Activator;
import com.prodyna.bamboo.status.IPlanScanner;
import com.prodyna.bamboo.status.ScanException;
import com.prodyna.bamboo.status.connect.BambooResourceLoader;
import com.prodyna.bamboo.status.connect.IResourceLoader;
import com.prodyna.bamboo.status.preferences.PreferenceConstants;

/**
 * @author Andre Albert
 */
public class BambooPlanScanner implements IPlanScanner {

	private BambooPlanResultHandler bambooHandler = new BambooPlanResultHandler();
	private IResourceLoader bambooLoader = new BambooResourceLoader();
	
	public static void main(String[] args) throws Exception {
		new BambooPlanScanner().scanPlans();
	}
	@Override
	public Map<String, Boolean> scanPlans() throws ScanException {
		SAXParserFactory spf = SAXParserFactory.newInstance();
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		try {
			SAXParser parser = spf.newSAXParser();
			String projects = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.P_BAMBOO_PROJECTS);
			if (projects != null && !projects.isEmpty()) {
				for (String proj : projects.split(",")) {
					bambooHandler.reset();
					String buildStatus = bambooLoader.load("result/" + proj);
					if (buildStatus == null || buildStatus.isEmpty()) {
						continue;
					}
					parser.parse(new ByteArrayInputStream(buildStatus.getBytes()), bambooHandler);
					result.putAll(bambooHandler.getPlanResults());
				}
			} else {
				parser.parse(new ByteArrayInputStream(bambooLoader.load("results").getBytes()), bambooHandler);
				result.putAll(bambooHandler.getPlanResults());
			}
		} catch (Exception e) {
			Activator.getDefault().log(Status.ERROR, "Error while scanning plan results", e);
			throw new ScanException("Could not scan for plan results", e);
		}

		return result;
	}

}
