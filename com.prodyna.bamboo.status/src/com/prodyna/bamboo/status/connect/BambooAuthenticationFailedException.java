/*******************************************************************************
 * Copyright (c) 2012 PRODYNA AG. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: PRODYNA AG - initial API and implementation
 *******************************************************************************/
package com.prodyna.bamboo.status.connect;

/**
 * @author Andre Albert
 *
 */
public class BambooAuthenticationFailedException extends ResourceLoadException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public BambooAuthenticationFailedException() {
	}

	/**
	 * @param arg0
	 */
	public BambooAuthenticationFailedException(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public BambooAuthenticationFailedException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public BambooAuthenticationFailedException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
