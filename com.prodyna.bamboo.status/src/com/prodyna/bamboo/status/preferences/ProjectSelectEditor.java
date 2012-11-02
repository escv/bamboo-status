/*******************************************************************************
 * Copyright (c) 2012 PRODYNA AG. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: PRODYNA AG - initial API and implementation
 *******************************************************************************/
package com.prodyna.bamboo.status.preferences;

import java.io.ByteArrayInputStream;
import java.util.Collection;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.ListEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;

import com.prodyna.bamboo.status.connect.BambooResourceLoader;
import com.prodyna.bamboo.status.connect.IResourceLoader;
import com.prodyna.bamboo.status.scan.BambooProjectResultHandler;

/**
 * @author Andre Albert
 *
 */
public class ProjectSelectEditor extends ListEditor {

	private StringFieldEditor hostField;
	private StringFieldEditor usernameField;
	private StringFieldEditor passwordField;
	
	private IResourceLoader bambooLoader;
	private BambooProjectResultHandler projectParserHandler;
	
	public ProjectSelectEditor(String name, String labelText, Composite parent, StringFieldEditor hostField, StringFieldEditor usernameField, StringFieldEditor passwordField) {
		super(name, labelText, parent);
		this.hostField = hostField;
		this.usernameField = usernameField;
		this.passwordField = passwordField;
		this.getAddButton().setText("Refresh");
		this.getUpButton().setVisible(false);
		this.getDownButton().setVisible(false);
	}
	
	@Override
	protected String createList(String[] items) {
		StringBuilder result = new StringBuilder();
		boolean first = true;
		for (String part : items) {
			if (!first) {
				result.append(",");
			} else {
				first = false;
			}
			result.append(part);
		}
		return result.toString();
	}

	@Override
	protected String getNewInputObject() {
		List list = getList();
		if (list != null) {
			RowData rd = new RowData();  
	        rd.height = 100;  
	        list.setLayoutData(rd);
			list.removeAll();
			if (bambooLoader == null) {
				bambooLoader = new BambooResourceLoader();
			}
			if (projectParserHandler == null) {
				projectParserHandler = new BambooProjectResultHandler();
			}
			projectParserHandler.reset();
			 
			try {
				String projectsXML = bambooLoader.load("project", hostField.getStringValue(), usernameField.getStringValue(), passwordField.getStringValue());
				SAXParserFactory spf = SAXParserFactory.newInstance();
				SAXParser parser = spf.newSAXParser();
				parser.parse(new ByteArrayInputStream(projectsXML.getBytes()), projectParserHandler);
				Collection<String> bambooProjects = projectParserHandler.getProjectResults();
				for (String bambooProject : bambooProjects) {
					list.add(bambooProject);
				}
			} catch (final Exception e) {
				getShell().getDisplay().asyncExec(new Runnable() {
					@Override
					public void run() {
						MessageDialog.openError(getShell(), "Error", e.getMessage());
					}
				});
				return null;
			}
		}
		return null;
	}

	@Override
	protected String[] parseString(String stringList) {
		return stringList.split(",");
	}

	
}
