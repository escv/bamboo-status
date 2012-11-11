/*******************************************************************************
 * Copyright (c) 2012 PRODYNA AG. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: PRODYNA AG - initial API and implementation
 *******************************************************************************/
package com.prodyna.bamboo.status.scan;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimerTask;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;

import com.prodyna.bamboo.status.Activator;
import com.prodyna.bamboo.status.IPlanScanner;
import com.prodyna.bamboo.status.ScanException;
import com.prodyna.bamboo.status.preferences.PreferenceConstants;

/**
 * Extends from JDK Timer Service. Execution will run in its own thread.
 * The task will scan all the plans execution results from the choosen Bamboo projects.
 * For each failing plan, it is checked if it is part of the exclude list, if not the
 * plan is marked as failing and will cause the UI to show this error.
 * 
 * @author Andre Albert
 */
public class ScanTask extends TimerTask {

	private Composite view;
	private IPlanScanner scanner;
	private List list; 
	
	public ScanTask(Composite view) {
		this.view = view;
		this.list = new List(view, SWT.BORDER | SWT.V_SCROLL | SWT.TRANSPARENT);
		this.scanner = new BambooPlanScanner();
	}
	
	@Override
	public void run() {
		try {
			Map<String, Boolean> scanPlansResult = scanner.scanPlans();
			Collection<String> failingProject = new ArrayList<String>();
			String[] excludedPlans = null;
			String excludes = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.P_BAMBOO_EXCLUDE_PLANS);
			if (excludes != null && !excludes.isEmpty()) {
				excludedPlans = excludes.split(",");
			}
			// filter by failing
			outer:for(Entry<String, Boolean> e : scanPlansResult.entrySet()) {
				if (!e.getValue()) {
					// first check if the failing project is handles by the Exclude-Filter
					// if so, bypass this failure
					if (excludedPlans != null) {
						for (String excluded : excludedPlans) {
							if (e.getKey().toUpperCase().contains(excluded.toUpperCase())) {
								continue outer;
							}
						}
					}
					failingProject.add(e.getKey());
				}
			}
			updateVisualStatus(failingProject);
			
		} catch (final ScanException e) {
			view.getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					MessageDialog.openError(view.getShell(), "Error", e.getMessage());
				}
			});
		}
		

	}

	private void updateVisualStatus(final Collection<String> failingProjects) {
		view.getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				list.removeAll();
				list.setBackground(view.getDisplay().getSystemColor(failingProjects.isEmpty() ?  SWT.COLOR_GREEN : SWT.COLOR_RED));
				for (String failing : failingProjects) {
					list.add(failing);
				}
			}
		});
	}

}
