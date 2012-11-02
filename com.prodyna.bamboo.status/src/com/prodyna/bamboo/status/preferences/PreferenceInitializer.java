/*******************************************************************************
 * Copyright (c) 2012 PRODYNA AG. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: PRODYNA AG - initial API and implementation
 *******************************************************************************/
package com.prodyna.bamboo.status.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.prodyna.bamboo.status.Activator;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(PreferenceConstants.P_BAMBOO_BASE_URL, "http://www.bambo-host:8080/bamboo");
		//store.setDefault(PreferenceConstants.P_BAMBOO_USERNAME, "username");
		//store.setDefault(PreferenceConstants.P_BAMBOO_PASSWORD, "s3cr3t");
	}

}
