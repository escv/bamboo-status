/*******************************************************************************
 * Copyright (c) 2012 PRODYNA AG. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: PRODYNA AG - initial API and implementation
 *******************************************************************************/
package com.prodyna.bamboo.status;

import java.util.Timer;

import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.prodyna.bamboo.status"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	
	private Timer timer;
	
	/**
	 * The constructor
	 */
	public Activator() {
		timer = new Timer("PlanResultScanner");
	}

	public void log(int status, String message, Throwable t) {
		getLog().log(new Status(status,PLUGIN_ID, message, t));
	}
	/**
	 * {@inheritDoc}
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/**
	 * Cancel and purges the JDK Timer.
	 * 
	 * {@inheritDoc}
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
		timer.cancel();
		timer.purge();
	}

	public Timer getTimer() {
		return timer;
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
}
