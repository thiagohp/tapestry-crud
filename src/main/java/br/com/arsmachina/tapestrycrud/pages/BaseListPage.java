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

package br.com.arsmachina.tapestrycrud.pages;

import java.io.Serializable;


import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.PageDetached;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;

import br.com.arsmachina.tapestrycrud.Constants;
import br.com.arsmachina.tapestrycrud.grid.ControllerGridDataSource;

/**
 * Base class for pages that list entity objects. The <code>object</code> property is meant to be
 * used as the <code>row</code> parameter of the {@link Grid} component.
 * 
 * @param <T> the entity class related to this encoder.
 * @param <K> the type of the class' primary key property.
 * @param <A> the type of the class' activation context.
 * 
 * @author Thiago H. de Paula Figueiredo (ThiagoHP)
 */
public abstract class BaseListPage<T, K extends Serializable, A extends Serializable> extends
		BasePage<T, K, A> {

	@Inject
	private ComponentResources componentResources;

	@Inject
	private Request request;

	private T object;

	/**
	 * Method used as the <code>source</code> parameter of the {@link Grid} component. This
	 * implementation returns <code>new {@link ControllerGridDataSource}(getController())</code>.
	 * 
	 * @return an {@link Object}.
	 */
	@SuppressWarnings("unchecked")
	@Cached
	public Object getObjects() {
		return new ControllerGridDataSource(getEntityClass(), getController());
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
	 * Returns <code>false</code>.
	 * 
	 * @see br.com.arsmachina.tapestrycrud.pages.BasePage#isfilterReadOnlyComponentsInBeanModel()
	 */
	@Override
	protected boolean isfilterReadOnlyComponentsInBeanModel() {
		return false;
	}

	/**
	 * Adds an <code>action</code> property to the {@link BeanModel}.
	 * 
	 * @see br.com.arsmachina.tapestrycrud.pages.BasePage#getBeanModel()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public BeanModel<T> getBeanModel() {

		final BeanModel<T> beanModel = super.getBeanModel();
		beanModel.add(Constants.ACTION_PROPERTY_NAME, null);

		return beanModel;

	}
	
	/**
	 * Removes or not a given object. This method only removes an object, using
	 * <code>getController().delete(id)</code>, if {@link canRemove(K)} returns <code>true</code>.
	 * 
	 * @param id a {@link K}.
	 */
	protected final Object doRemove(K id) {

		if (canRemove(id)) {
			getController().delete(id);
			setRemoveSuccessMessage();
		}

		return returnFromDoRemove();

	}

	/**
	 * Defines what {@link #doRemove()} will return.
	 * 
	 * @return an {@link Object} or <code>null</code>.
	 */
	protected Object returnFromDoRemove() {

		Object returnValue = null;

		if (request.isXHR()) {

			if (returnZoneOnXHR()) {
				returnValue = getFormZone();
			}
			else {
				returnValue = componentResources.getBlock(getFormBlockId());
			}

		}

		return returnValue;

	}

	/**
	 * Sets the remove success message in this page.
	 */
	protected void setRemoveSuccessMessage() {
		setMessage(getMessages().get(Constants.MESSAGE_SUCCESS_REMOVE));
	}

	/**
	 * Tells if a given object can be removed in this context. It must be overriden if you have some
	 * rules about when an object can be removed. This implementation just returns <code>true</code>.
	 * 
	 * @param id a {@link #K}.
	 * @return a <code>boolean</code>.
	 */
	protected boolean canRemove(K id) {
		return true;
	}

	/**
	 * Clears the message after it is shown, preventing the message from appearing twice in AJAX
	 * actions.
	 */
	@PageDetached
	void clearMessage() {
		if (request.isXHR()) {
			setMessage(null);
		}
	}

}
