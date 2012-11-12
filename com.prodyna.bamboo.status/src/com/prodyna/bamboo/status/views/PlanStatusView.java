/*******************************************************************************
 * Copyright (c) 2012 bamboo-status. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Andre Albert - initial API and implementation
 *******************************************************************************/
package com.prodyna.bamboo.status.views;

import java.util.TimerTask;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import com.prodyna.bamboo.status.Activator;
import com.prodyna.bamboo.status.scan.ScanTask;


/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 */
public class PlanStatusView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "com.prodyna.bamboo.status.views.PlanStatusView";

	private TimerTask scanTask;

	/**
	 * The constructor.
	 */
	public PlanStatusView() {
		
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		parent.setBackground(parent.getShell().getDisplay().getSystemColor(SWT.COLOR_GREEN));
		if (scanTask == null) {
			scanTask = new ScanTask(parent);
			Activator.getDefault().getTimer().schedule(scanTask, 0, 3*60*1000);
		}
	}

	public void update() {
		if (scanTask != null) {
			scanTask.run();
		}
	}
	@Override
	public void setFocus() {
		
	}
	
	@Override
	public void dispose() {
		super.dispose();
		if (scanTask != null) {
			scanTask.cancel();
			scanTask = null;
		}
	}
}