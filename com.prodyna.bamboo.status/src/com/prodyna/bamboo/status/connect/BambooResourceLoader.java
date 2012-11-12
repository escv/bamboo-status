/*******************************************************************************
 * Copyright (c) 2012 bamboo-status. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Andre Albert - initial API and implementation
 *******************************************************************************/
package com.prodyna.bamboo.status.connect;

import java.io.InputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.eclipse.core.runtime.Status;

import com.prodyna.bamboo.status.Activator;
import com.prodyna.bamboo.status.preferences.PreferenceConstants;

/**
 * @author Andre Albert
 *
 */
public class BambooResourceLoader implements IResourceLoader {

	private static final String BAMBOO_REST_API_PATH = "/rest/api/latest/";
	private SSLSocketFactory factory;
	
	@Override
	public String load(String path) throws ResourceLoadException {
		String baseUrl = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.P_BAMBOO_BASE_URL);
		String username = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.P_BAMBOO_USERNAME);
		String password = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.P_BAMBOO_PASSWORD);
		
		return load(path, baseUrl, username, password);
	}

	@Override
	public String load(String path, String baseUrl, final String username,
			final String password) throws ResourceLoadException {
		
		if (path == null || path.isEmpty()) {
			throw new IllegalArgumentException("Given path is null or empty");
		}
		if (path.startsWith("/")) {
			path = path.substring(1);
		}
		if (baseUrl.endsWith("/")) {
			baseUrl = baseUrl.substring(0, baseUrl.length()-1);
		}

		//String userpass = username + ":" + password;
		
		StringBuilder result = new StringBuilder();
		HttpURLConnection conn = null;
		if (!baseUrl.isEmpty()) {
			baseUrl = baseUrl + BAMBOO_REST_API_PATH + path + "?os_authType=basic";
			try {
				URL url = new URL(baseUrl);
		        conn = (HttpURLConnection) url.openConnection();
		        //String basicAuth = "Basic " + EncodingUtils.encodeBase64(userpass.getBytes());
		        //conn.setRequestProperty ("Authorization", basicAuth);
		        conn.setDoOutput(true);
		        Authenticator.setDefault (new Authenticator() {
		            protected PasswordAuthentication getPasswordAuthentication() {
		                return new PasswordAuthentication (username, password.toCharArray());
		            }
		        });
		        if (conn instanceof HttpsURLConnection) {
		        	if (factory == null) {
		        		try {
		        			SSLContext ssl = SSLContext.getInstance("TLSv1");
		        		    ssl.init(null, new TrustManager[]{new BypassX509TrustManager()}, null);
		        		    factory = ssl.getSocketFactory();
		        		    ((HttpsURLConnection) conn).setSSLSocketFactory(factory);
		        		} catch(Exception e) {
		        			Activator.getDefault().log(Status.ERROR, "Could not init tls https handling", e);
		        		}
		        	}
		        }
		        conn.connect();

		        final int responseCode = conn.getResponseCode();
		        switch(responseCode){
		        case HttpURLConnection.HTTP_OK: {
		        	InputStream in = conn.getInputStream();
		        	byte[] buf = new byte[256];
		        	int read = -1;
		        	while ((read = in.read(buf)) >0 ) {
		        		result.append(new String (buf, 0, read));
		        	};
		        	break;
		        }
		        case HttpURLConnection.HTTP_UNAUTHORIZED: {
		        	throw new BambooAuthenticationFailedException("Unauthorized Access to Bamboo API");
		        }
		        }
			} catch (ResourceLoadException rle) {
				throw rle;
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

class BypassX509TrustManager implements X509TrustManager {
    public void checkClientTrusted(
            X509Certificate[] cert, String s)
            throws CertificateException {
    }

    public void checkServerTrusted(
            X509Certificate[] cert, String s)
            throws CertificateException {
      }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        // TODO Auto-generated method stub
        return null;
    }

}