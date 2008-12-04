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

package br.com.arsmachina.tapestrycrud.base;

import java.io.Serializable;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.PrimaryKeyEncoder;
import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.PageDetached;
import org.apache.tapestry5.annotations.Retain;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.apache.tapestry5.services.Request;

import br.com.arsmachina.tapestrycrud.Constants;
import br.com.arsmachina.tapestrycrud.grid.ControllerGridDataSource;
import br.com.arsmachina.tapestrycrud.services.PrimaryKeyEncoderSource;

/**
 * Base class for pages that list entity objects. The <code>object</code> property is meant to be
 * used as the <code>row</code> parameter of the {@link Grid} component.
 * 
 * One example of its use can be found in the Ars Machina Project Example Application (<a
 * href="http://ars-machina.svn.sourceforge.net/viewvc/ars-machina/example/trunk/src/main/java/br/com/arsmachina/example/web/pages/project/ListProject.java?view=markup"
 * >page class</a>. <a
 * href="http://ars-machina.svn.sourceforge.net/viewvc/ars-machina/example/trunk/src/main/webapp/project/ListProject.tml?view=markup"
 * >template</a>).
 * 
 * 
 * @param <T> the entity class related to this encoder.
 * @param <K> the type of the class' primary key property.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public abstract class BaseListPage<T, K extends Serializable> extends BasePage<T, K> {

	@Inject
	private ComponentResources componentResources;

	@Inject
	private Request request;

	@Inject
	private PrimaryKeyEncoderSource primaryKeyEncoderSource;
	
	@Retain
	private PrimaryKeyEncoder<K, T> primaryKeyEncoder;

	@Inject
	private BeanModelSource beanModelSource;

	private T object;

	/**
	 * Single constructor of this class.
	 */
	@SuppressWarnings("unchecked")
	public BaseListPage() {
		
		super();
		
		primaryKeyEncoder = (PrimaryKeyEncoder<K, T>) primaryKeyEncoderSource.get(getEntityClass());
		
	}

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
	 * Adds an <code>action</code> property to the {@link BeanModel}.
	 * 
	 * @see br.com.arsmachina.tapestrycrud.base.BasePage#getBeanModel()
	 */
	@SuppressWarnings("unchecked")
	public BeanModel<T> getBeanModel() {

		final BeanModel<T> beanModel = beanModelSource.createDisplayModel(getEntityClass(),
				getMessages());
		beanModel.add(Constants.ACTION_PROPERTY_NAME, null);

		return beanModel;

	}

	/**
	 * Removes or not a given object. This method only removes an object, using
	 * <code>getController().delete(id)</code>, if {@link canRemove(K)} returns <code>true</code>.
	 * 
	 * @param object a {@link K}.
	 */
	protected final Object remove(T object) {
		
		if (object == null) {
			setRemoveErrorNotFoundMessage();
		}

		else if (canRemove(object)) {

			getController().delete(object);
			setRemoveSuccessMessage();

		}
		else {
			setRemoveErrorNotAllowedMessage();
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
	 * Sets the remove not done because of lack of priviledge message in this page.
	 */
	protected void setRemoveErrorNotAllowedMessage() {
		setMessage(getMessages().get(Constants.MESSAGE_ERROR_REMOVE_NOT_ALLOWED));
	}

	/**
	 * Sets the remove not done because object not found in this page.
	 */
	protected void setRemoveErrorNotFoundMessage() {
		setMessage(getMessages().get(Constants.MESSAGE_ERROR_REMOVE_NOT_FOUND));
	}

	/**
	 * Tells if a given object can be removed in this context. It must be overriden if you have some
	 * rules about when an object can be removed. This implementation just returns <code>true</code>.
	 * 
	 * @param object a {@link #T}.
	 * @return a <code>boolean</code>.
	 */
	protected boolean canRemove(T object) {
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

	/**
	 * This method listens to the {@link Constants#REMOVE_OBJECT_ACTION} event and removes the
	 * corresponding object.
	 * 
	 * @param context an {@link EventContext}.
	 */
	@OnEvent(Constants.REMOVE_OBJECT_ACTION)
	protected Object remove(EventContext context) {
		
		K id = context.get(getPrimaryKeyClass(), 0);
		final T toBeRemoved = primaryKeyEncoder.toValue(id);
		return remove(toBeRemoved);

	}

	/**
	 * Returns the configured {@link PrimaryKeyEncoder} for a given entity class.
	 * 
	 * @param <X> the type of the entity.
	 * @param clasz a {@link Class}.
	 * @return a {@link PrimaryKeyEncoder}.
	 * @see br.com.arsmachina.tapestrycrud.services.PrimaryKeyEncoderSource#get(java.lang.Class)
	 */
	protected <X, Y extends Serializable> PrimaryKeyEncoder<Y, X> getPrimaryKeyEncoder(Class<X> clasz) {
		return primaryKeyEncoderSource.get(clasz);
	}

}
