/*******************************************************************************
 * Copyright (c) 2012 bamboo-status. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Andre Albert - initial API and implementation
 *******************************************************************************/
package com.prodyna.bamboo.status.connect;

/**
 * @author Andre Albert
 *
 */
public class ResourceLoadException extends Exception {

	private static final long serialVersionUID = 1L;

	public ResourceLoadException() {
	}

	public ResourceLoadException(String arg0) {
		super(arg0);
	}

	public ResourceLoadException(Throwable arg0) {
		super(arg0);
	}

	public ResourceLoadException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
