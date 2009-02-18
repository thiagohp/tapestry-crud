// Copyright 2009 Thiago H. de Paula Figueiredo
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

package br.com.arsmachina.tapestrycrud.base;

import java.io.Serializable;

import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.ioc.annotations.Inject;

import br.com.arsmachina.tapestrycrud.services.TapestryCrudModuleService;

/**
 * Base class for pages that edit entity objects.
 * 
 * @author Thiago H. de Paula Figueiredo
 * @param <T> the entity class related to this encoder.
 * @param <K> the type of the class' primary key property.
 */
public class BaseViewPage<T, ID extends Serializable> extends BasePage<T, ID> {

	private T object;

	@Inject
	private TapestryCrudModuleService tapestryCrudModuleService;

	/**
	 * Sets the object property from a given activation context value.
	 * 
	 * @param value an {@link EventContext}.
	 */
	Object onActivate(EventContext context) {

		checkReadTypeAccess();

		object = getActivationContextEncoder(getEntityClass()).toObject(context);

		if (object != null) {
			checkReadObjectAccess(object);
		}

		final Class<?> result = object != null ? null : getListPage();

		return result;

	}

	private Class<?> getListPage() {
		return tapestryCrudModuleService.getListPageClass(getEntityClass());
	}

	/**
	 * Checks if the current user has permission to read instances of the page entity class and
	 * throws an exception if not. This method calls
	 * <code>getAuthorizer().checkRead(getEntityClass())</code>.
	 */
	protected void checkReadTypeAccess() {
		getAuthorizer().checkRead(getEntityClass());
	}

	/**
	 * Checks if the current user has permission to ra given object and throws an exception if not.
	 * This method calls <code>getAuthorizer().checkRead(object)</code>.
	 */
	protected void checkReadObjectAccess(T object) {
		getAuthorizer().checkRead(object);
	}

	/**
	 * Returns the value of the <code>object</code> property.
	 * 
	 * @return a {@link T}.
	 */
	public T getObject() {
		return object;
	}

}
