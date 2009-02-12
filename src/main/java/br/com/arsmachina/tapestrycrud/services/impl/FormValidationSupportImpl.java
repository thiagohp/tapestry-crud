// Copyright 2008 Thiago H. de Paula Figueiredo
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package br.com.arsmachina.tapestrycrud.services.impl;

import java.util.List;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Field;
import org.apache.tapestry5.ioc.internal.util.TapestryException;
import org.apache.tapestry5.model.ComponentModel;
import org.apache.tapestry5.runtime.Component;

import br.com.arsmachina.tapestrycrud.services.FormValidationSupport;

/**
 * Default {@link FormValidationSupport} implementation.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public class FormValidationSupportImpl implements FormValidationSupport {

	public Field getField(String fieldId, ComponentResources componentResources) {

		Field field;
		
		try {
			field = (Field) componentResources.getEmbeddedComponent(fieldId);
		}
		catch (TapestryException e) {
			
			// there is no field with such fieldId inside the component or page 
			// represented by componentResources
			field = null;
			
		}
		
		if (field == null) {

			ComponentModel model = componentResources.getComponentModel();
	
			final List<String> embeddedComponentIds = model.getEmbeddedComponentIds();
	
			for (String componentId : embeddedComponentIds) {
	
				if (componentId.equalsIgnoreCase(fieldId)) {
					field = (Field) componentResources.getEmbeddedComponent(fieldId);
					break;
				}
	
			}
	
			// let's search recursively now
			if (field == null) {
	
				for (String componentId : embeddedComponentIds) {
	
					final Component component = componentResources.getEmbeddedComponent(componentId);
					field = getField(fieldId, component.getComponentResources());
	
					if (field != null) {
						break;
					}
	
				}
	
			}
			
		}

		return field;

	}

}
