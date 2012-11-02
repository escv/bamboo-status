/*******************************************************************************
 * Copyright (c) 2012 PRODYNA AG. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: PRODYNA AG - initial API and implementation
 *******************************************************************************/
package com.prodyna.bamboo.status.connect;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.eclipse.core.runtime.Status;
import org.eclipse.equinox.security.storage.EncodingUtils;

import com.prodyna.bamboo.status.Activator;
import com.prodyna.bamboo.status.preferences.PreferenceConstants;

/**
 * @author Andre Albert
 *
 */
public class BambooResourceLoader implements IResourceLoader {

	private static final String BAMBOO_REST_API_PATH = "/rest/api/latest/";
	
	@Override
	public String load(String path) throws ResourceLoadException {
		String baseUrl = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.P_BAMBOO_BASE_URL);
		String username = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.P_BAMBOO_USERNAME);
		String password = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.P_BAMBOO_PASSWORD);
		
		return load(path, baseUrl, username, password);
	}

	@Override
	public String load(String path, String baseUrl, String username,
			String password) throws ResourceLoadException {
		
		if (path == null || path.isEmpty()) {
			throw new IllegalArgumentException("Given path is null or empty");
		}
		if (path.startsWith("/")) {
			path = path.substring(1);
		}
		if (baseUrl.endsWith("/")) {
			baseUrl = baseUrl.substring(0, baseUrl.length()-1);
		}

		String userpass = username + ":" + password;
		
		StringBuilder result = new StringBuilder();
		HttpURLConnection conn = null;
		if (!baseUrl.isEmpty()) {
			baseUrl = baseUrl + BAMBOO_REST_API_PATH + path;
			try {
				URL url = new URL(baseUrl);
		        conn = (HttpURLConnection) url.openConnection();
		        String basicAuth = "Basic " + EncodingUtils.encodeBase64(userpass.getBytes());
		        conn.setRequestProperty ("Authorization", basicAuth);
		        conn.setDoOutput(true);
		        conn.connect();

		        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
		        	InputStream in = conn.getInputStream();
		        	byte[] buf = new byte[256];
		        	int read = -1;
		        	while ((read = in.read(buf)) >0 ) {
		        		result.append(new String (buf, 0, read));
		        	}
		        }
			} catch (Exception e) {
				Activator.getDefault().log(Status.ERROR, "Error while requesting Bamboo resource "+baseUrl, e);
				throw new ResourceLoadException("Could not load Resource "+path, e);
			} finally {
				if (conn != null) {
					conn.disconnect();
				}
			}
		}
	    return result.toString();
	}
}
