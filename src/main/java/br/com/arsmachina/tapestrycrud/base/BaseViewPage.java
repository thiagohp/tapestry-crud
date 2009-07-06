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

/**
 * Base class for pages that edit entity objects.
 * 
 * @author Thiago H. de Paula Figueiredo
 * @param <T> the entity class related to this encoder.
 * @param <K> the type of the class' primary key property.
 */
public class BaseViewPage<T, ID extends Serializable> extends BasePage<T, ID> {

	private T object;
	
	/**
	 * Sets the object property from a given activation context value.
	 * 
	 * @param value an {@link EventContext}.
	 */
	Object onActivate(EventContext context) {

		boolean validRequest = false;
		
		checkReadTypeAccess();
		
		if (context.getCount() > 0) {

			object = loadOject(context);
	
			if (object != null) {
				checkReadObjectAccess(object);
				validRequest = true;
			}
			
		}
		// event requests
		else {
			
	        if (isEventRequest()) {
	        	validRequest = true;
	        }
	        
		}
		
		final Class<?> result = validRequest ? null : getListPage();

		return result;

	}

	/**
	 * Loads an object from an {@link EventContext}. This method is never invoked
	 * without at least one activation context parameter.
	 * This implementation returns
	 * <code>getActivationContextEncoder(getEntityClass()).toObject(context)</code>.
	 * 
	 * @param context an {@link EventContext}.
	 * @return a <code>T</code> or <code>null</code>.
	 */
	protected T loadOject(EventContext context) {
		return getActivationContextEncoder(getEntityClass()).toObject(context);
	}

	private Class<?> getListPage() {
		return getTapestryCrudModuleService().getListPageClass(getEntityClass());
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
	
	/**
	 * Changes the value of the <code>object</code> property.
	 *
	 * @param object a {@link T}.
	 */
	public void setObject(T object) {
		this.object = object;
	}

	/**
	 * Returns the current activation context of this page.
	 * 
	 * @return an {@link #A}.
	 */
	final public Object onPassivate() {

		final T o = getObject();
		return o != null ? getActivationContextEncoder(getEntityClass()).toActivationContext(o) : null;

	}

}
