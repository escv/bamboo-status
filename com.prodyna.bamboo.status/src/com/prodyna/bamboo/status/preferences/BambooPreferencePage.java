/*******************************************************************************
 * Copyright (c) 2012 Andre Albert. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Andre Albert - initial API and implementation
 *******************************************************************************/
package com.prodyna.bamboo.status.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.IViewDescriptor;

import com.prodyna.bamboo.status.Activator;
import com.prodyna.bamboo.status.views.PlanStatusView;

/**
 * This class represents a preference page that
 * is contributed to the Preferences dialog. By 
 * subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows
 * us to create a page that is small and knows how to 
 * save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They
 * are stored in the preference store that belongs to
 * the main plug-in class. That way, preferences can
 * be accessed directly via the preference store.
 */
public class BambooPreferencePage
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {

	public BambooPreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("Configure the Access to a Atlassian Bamboo Server. After proving host and credentials, a click on Refresh to reload all the registered Projects from the Bamboo Server. To exclude a Project with all its configured Plans from the status calculation, select it and click on Remove. To exclude certain Plans, provide a comma separated list of String containment patterns");
	}
	
	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
	public void createFieldEditors() {

		StringFieldEditor host = new StringFieldEditor(PreferenceConstants.P_BAMBOO_BASE_URL, "Bamboo Host:", getFieldEditorParent());
		StringFieldEditor username = new StringFieldEditor(PreferenceConstants.P_BAMBOO_USERNAME, "Benutzername:", getFieldEditorParent());
		StringFieldEditor password = new StringFieldEditor(PreferenceConstants.P_BAMBOO_PASSWORD, "Passwort:", getFieldEditorParent());
		
		host.setEmptyStringAllowed(false);
		username.setEmptyStringAllowed(false);
		password.setEmptyStringAllowed(false);
		
		addField(host);
		addField(username);
		addField(password);
		addField(new ProjectFieldEditor(PreferenceConstants.P_BAMBOO_PROJECTS, "Status relevant Projects:", getFieldEditorParent(), host, username, password));
		addField(new StringFieldEditor(PreferenceConstants.P_BAMBOO_EXCLUDE_PLANS, "Exclude Plans (,):", getFieldEditorParent()));
	}

	public void init(IWorkbench workbench) {
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean performOk() {
		boolean performOk = super.performOk();
		IViewPart view = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(PlanStatusView.ID);
		if (view instanceof PlanStatusView) {
			((PlanStatusView)view).update();
		}
		return performOk;
	}
}