/*******************************************************************************
 * Copyright (c) 2012 Andre Albert. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Andre Albert - initial API and implementation
 *******************************************************************************/
package com.prodyna.bamboo.status.connect;

/**
 * Performs a HTTP URL Request to a given path and returns the Response as a String.
 * @author Andre Albert
 *
 */
public interface IResourceLoader {

	public String load(String path) throws ResourceLoadException;

	public String load(String path, String baseUrl, String username, String password) throws ResourceLoadException;
	
}
