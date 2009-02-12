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

package br.com.arsmachina.tapestrycrud.services;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Field;

/**
 * Interface that defines some utility methods for validation of forms.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public interface FormValidationSupport {

	/**
	 * Finds a {@link Field} in a given page or component (passed by the
	 * <code>componentResources</code> parameter). If the field is not found, <code>null</code>is
	 * returned.
	 * 
	 * @param fieldId a {@link String}. It cannot be null.
	 * @param componentResources a {@link ComponentResources}. It cannot be null.
	 * @return a {@link Field} or null.
	 */
	Field getField(String fieldId, ComponentResources componentResources);

}
