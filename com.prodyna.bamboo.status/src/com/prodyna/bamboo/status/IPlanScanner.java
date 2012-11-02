/*******************************************************************************
 * Copyright (c) 2012 Andre Albert. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Andre Albert - initial API and implementation
 *******************************************************************************/
package com.prodyna.bamboo.status;

import java.util.Map;

/**
 * Scans the execution results of all Plans of the choosen Projects.
 * 
 * @author Andre Albert
 */
public interface IPlanScanner {

	/**
	 * Scans Plans maps a boolean (if successful or not) to each plan.
	 * 
	 * @return a map with the Plan name as key and a Boolean (if successful or not) as corresponding value
	 * @throws ScanException
	 * @since 0.0.1
	 */
	Map<String, Boolean> scanPlans() throws ScanException;
}
