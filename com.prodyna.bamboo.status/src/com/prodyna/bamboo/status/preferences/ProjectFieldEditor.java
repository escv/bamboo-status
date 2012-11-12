/*******************************************************************************
 * Copyright (c) 2012 bamboo-status. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Andre Albert - initial API and implementation
 *******************************************************************************/
package com.prodyna.bamboo.status.preferences;

import java.io.ByteArrayInputStream;
import java.util.Collection;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;

import com.prodyna.bamboo.status.Activator;
import com.prodyna.bamboo.status.connect.BambooResourceLoader;
import com.prodyna.bamboo.status.connect.IResourceLoader;
import com.prodyna.bamboo.status.scan.BambooProjectResultHandler;

/**
 * @author Andre Albert
 *
 */
public class ProjectFieldEditor extends FieldEditor {

	private StringFieldEditor hostField;
	private StringFieldEditor usernameField;
	private StringFieldEditor passwordField;
	private IResourceLoader bambooLoader;
	private BambooProjectResultHandler projectParserHandler;
	
    /**
     * The list widget; <code>null</code> if none
     * (before creation or after disposal).
     */
    private List list;
	private Composite listButtonComposite;
    
    /**
     * Creates a list field editor.
     * 
     * @param name the name of the preference this field editor works on
     * @param labelText the label text of the field editor
     * @param parent the parent of the field editor's control
     */
    protected ProjectFieldEditor(String name, String labelText, Composite parent, StringFieldEditor hostField, StringFieldEditor usernameField, StringFieldEditor passwordField) {
        init(name, labelText);
        this.hostField = hostField;
		this.usernameField = usernameField;
		this.passwordField = passwordField;
        createControl(parent);
    }
    
	/**
	 * {@inheritDoc}
	 */
    @Override
    protected void adjustForNumColumns(int numColumns) {
    	GridData gd = (GridData) listButtonComposite.getLayoutData();
        gd.horizontalSpan = numColumns - 1;
        // We only grab excess space if we have to
        // If another field editor has more columns then
        // we assume it is setting the width.
        gd.grabExcessHorizontalSpace = gd.horizontalSpan == 1;
    }
    
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doFillIntoGrid(Composite parent, int numColumns) {
        getLabelControl(parent);

        listButtonComposite = new Composite(parent, SWT.FILL);
        GridLayout layout = new GridLayout(1, true);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        //gd.verticalAlignment = GridData.FILL;
        gd.horizontalSpan = numColumns - 1;
        gd.grabExcessHorizontalSpace = true;
        listButtonComposite.setLayoutData(gd);
        listButtonComposite.setLayout(layout);

        createPushButton(listButtonComposite, "Refresh Projects");
        list = getListControl(listButtonComposite);
        gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.grabExcessHorizontalSpace = true;
        gd.heightHint = 150;
        list.setLayoutData(gd);
        
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doLoad() {
		if (list != null) {
			list.removeAll();
			String username = usernameField.getStringValue();
			String password = passwordField.getStringValue();
			
			if (username.isEmpty() || password.isEmpty()) {
				return;
			}
			
			if (bambooLoader == null) {
				bambooLoader = new BambooResourceLoader();
			}
			if (projectParserHandler == null) {
				projectParserHandler = new BambooProjectResultHandler();
			}
			projectParserHandler.reset();
			
			try {
				String projectsXML = bambooLoader.load("project", hostField.getStringValue(), username, password);
				SAXParserFactory spf = SAXParserFactory.newInstance();
				SAXParser parser = spf.newSAXParser();
				parser.parse(new ByteArrayInputStream(projectsXML.getBytes()), projectParserHandler);
				Collection<String> bambooProjects = projectParserHandler.getProjectResults();
				for (String bambooProject : bambooProjects) {
					list.add(bambooProject);
				}
				String savedSelection = getPreferenceStore().getString(getPreferenceName());
				list.setSelection(savedSelection.split(","));
				//list.selectAll();
			} catch (final Exception e) {
				Activator.getDefault().log(Status.ERROR, "Error while loading Bamboo Project list", e);
				getPage().getShell().getDisplay().asyncExec(new Runnable() {
					@Override
					public void run() {
						MessageDialog.openError(getPage().getShell(), "Error", e.getMessage());
					}
				});
			}
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doLoadDefault() {
		// TODO Auto-generated method stub

	}
	
	/**
     * Returns this field editor's list control.
     *
     * @param parent the parent control
     * @return the list control
     */
    public List getListControl(Composite parent) {
        if (list == null) {
            list = new List(parent, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL
                    | SWT.H_SCROLL);
            list.setFont(parent.getFont());
            list.addDisposeListener(new DisposeListener() {
                public void widgetDisposed(DisposeEvent event) {
                    list = null;
                }
            });
        } else {
            checkParent(list, parent);
        }
        return list;
    }
    
    private Button createPushButton(Composite parent, String key) {
        Button button = new Button(parent, SWT.PUSH);
        button.setText(JFaceResources.getString(key));
        button.setFont(parent.getFont());
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        int widthHint = convertHorizontalDLUsToPixels(button,
                IDialogConstants.BUTTON_WIDTH);
        data.widthHint = Math.max(widthHint, button.computeSize(SWT.DEFAULT,
                SWT.DEFAULT, true).x);
        button.setLayoutData(data);
        button.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				doLoad();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});
        return button;
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void doStore() {
		if (list!=null) {
			StringBuilder selectedProjects = new StringBuilder();
			boolean first = true;
			String[] selection = list.getSelection();
			for (String selectItem : selection) {
				if (!first) {
					selectedProjects.append(",");
				} else {
					first = false;
				}
				selectedProjects.append(selectItem);
			}
			getPreferenceStore().setValue(getPreferenceName(), selectedProjects.toString());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getNumberOfControls() {
		return 2;
	}

}
