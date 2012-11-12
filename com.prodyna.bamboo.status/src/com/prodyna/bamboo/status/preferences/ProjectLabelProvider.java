/*******************************************************************************
 * Copyright (c) 2012 bamboo-status. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Andre Albert - initial API and implementation
 *******************************************************************************/
package com.prodyna.bamboo.status.preferences;

import org.eclipse.jface.viewers.LabelProvider;

import com.prodyna.bamboo.status.model.Project;

/**
 * @author Andre Albert
 *
 */
public class ProjectLabelProvider extends LabelProvider {
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getText(Object element) {
		String result;
		if (element instanceof Project) {
			result = ((Project)element).getName();
		} else {
			result = super.getText(element);
		}
		return result;
	}

}
