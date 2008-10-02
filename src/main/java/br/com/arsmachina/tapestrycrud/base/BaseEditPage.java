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
import org.apache.tapestry5.Field;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.AfterRenderTemplate;
import org.apache.tapestry5.annotations.Meta;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.internal.services.PersistentFieldManagerImpl;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;

import br.com.arsmachina.controller.Controller;
import br.com.arsmachina.tapestrycrud.Constants;
import br.com.arsmachina.tapestrycrud.encoder.ActivationContextEncoder;
import br.com.arsmachina.tapestrycrud.encoder.Encoder;

/**
 * Base class for pages that edit entity objects.
 * One example of its use can be found in the Ars Machina Project Example Application 
 * (<a href="http://ars-machina.svn.sourceforge.net/viewvc/ars-machina/example/trunk/src/main/java/br/com/arsmachina/example/web/pages/project/EditProject.java?view=markup"
 * 		>page class</a>.
 * <a href="http://ars-machina.svn.sourceforge.net/viewvc/ars-machina/example/trunk/src/main/webapp/project/EditProject.tml?view=markup"
 * 		>template</a>).
 * 
 * @param <T> the entity class related to this encoder.
 * @param <K> the type of the class' primary key property.
 * @param <A> the type of the class' activation context.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
@Meta(PersistentFieldManagerImpl.META_KEY + "=" + PersistenceConstants.FLASH)
public abstract class BaseEditPage<T, K extends Serializable, A extends Serializable> extends
		BasePage<T, K, A> {

	/**
	 * You can change the persistence strategy from flash to another using
	 * <code>@Meta("tapestry.persistence-strategy=" + PersistenceConstants.FLASH)</code> in your class.
	 */
	@Persist
	private T object;

	@Inject
	private Request request;

	@Inject
	private ComponentResources componentResources;

	@org.apache.tapestry5.annotations.Component(id = Constants.FORM_ID)
	private Form form;

	private Encoder<T, K, A> encoder;

	private ActivationContextEncoder<T, A> activationContextEncoder;

	private Controller<T, K> controller;

	/**
	 * Single constructor of this class.
	 */
	public BaseEditPage() {

		final Class<T> entityClass = getEntityClass();
		encoder = getEncoder(entityClass);
		activationContextEncoder = getActivationContextEncoder(entityClass);
		controller = getController();

	}

	/**
	 * Ensures the edited object is not null before form rendering and submission. It uses
	 * {@link #createNewObject()} to create a new entity object if needed.
	 */
	@OnEvent(component = Constants.FORM_ID, value = Form.PREPARE)
	final protected void prepare() {

		final T object = getObject();

		if (object == null) {
			setObject(createNewObject());
		}

	}

	/**
	 * Validates the object. This method invokes {@link #validateObject(Form)} and then takes care
	 * of handling AJAX form submissions.
	 */
	@OnEvent(component = Constants.FORM_ID, value = Form.VALIDATE_FORM)
	final protected Object validate() {

		// clear the confirmation message, if set.
		setMessage(null);

		final T object = getObject();
		final Form form = getForm();

		validateObject(object, form);

		Object returnValue = null;

		final boolean hasErrors = getForm().getHasErrors();

		if (hasErrors && request.isXHR()) {
			returnValue = getFormZone();
		}

		return returnValue;

	}

	/**
	 * Validates an <code>object</code> and stores the validation erros in a <code>form</code>.
	 * This implementation does nothing.
	 * 
	 * @param object an {@link #T}.
	 * @param a {@link Form}.
	 */
	protected void validateObject(T object, Form form) {
	}

	/**
	 * Adds an error to a given field in the form.
	 * 
	 * @param fieldId a {@link String}. It cannot be null.
	 * @param message a {@link String}. It cannot be null.
	 */
	final protected void addError(String fieldId, String message) {

		assert fieldId != null;
		assert message != null;

		Field field = (Field) componentResources.getEmbeddedComponent(fieldId);
		form.recordError(field, message);

	}

	/**
	 * Saves or updates the edited object. This method does the following steps:
	 * <ol>
	 * <li>Invokes {@link #prepareObjectForSaveOrUpdate()}</li>.
	 * <li>Invokes {@link #getController()}<code>.saveOrUpdate(entity);</code>.
	 * <li>Invokes and returns {@link #returnFromDoRemove()} </li>
	 * </ol>
	 */
	@OnEvent(component = Constants.FORM_ID, value = Form.SUCCESS)
	final public Object saveOrUpdate() {

		prepareObjectForSaveOrUpdate();
		T entity = getObject();
		controller.saveOrUpdate(entity);

		return returnFromDoRemove();

	}

	/**
	 * Defines what {@link #saveOrUpdate()} will return and sets the success message.
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

			setSaveOrUpdateSuccessMessage();

		}
		else {

			BaseListPage<T, K, A> page = getListPage();

			if (page != null) {

				page.setMessage(Constants.MESSAGE_SAVEORUPDATE_SUCCESS);
				returnValue = page;

			}
			else {
				setSaveOrUpdateSuccessMessage();
			}

		}

		return returnValue;

	}

	/**
	 * Sets the save or update success message in this page.
	 */
	protected void setSaveOrUpdateSuccessMessage() {
		setMessage(getMessages().get(Constants.MESSAGE_SAVEORUPDATE_SUCCESS));
	}

	/**
	 * Returns the {@link BaseListPage} instance associated to this object. This is used by
	 * {@link #returnAfterAction()} to define what will be returned by {@link #saveOrUpdate()}.
	 * This implementation returns null and must be overriden by pages that want to show the listing
	 * page after a successful save or update.
	 * 
	 * @return a {@link BaseListPage}.
	 */
	protected BaseListPage<T, K, A> getListPage() {
		return null;
	}

	/**
	 * Does any processing that must be done in the object before it is saved or updated.
	 */
	protected void prepareObjectForSaveOrUpdate() {

	}

	/**
	 * Creates a new entity object to be edited. Fields can be prefilled if desired. This method is
	 * used by {@link #prepare()}.
	 * 
	 * @return a {@link T}.
	 */
	abstract protected T createNewObject();

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
	 * Returns the value of the <code>form</code> property.
	 * 
	 * @return a {@link Form}.
	 */
	protected final Form getForm() {
		return form;
	}

	/**
	 * Returns the current activation context of this page.
	 * 
	 * @return an {@link #A}.
	 */
	final public A onPassivate() {
		
		final T o = getObject();
		return o != null ? encoder.toActivationContext(o) : null;
		
	}

	/**
	 * Clears the form errors and message after page rendering.
	 */
	@AfterRenderTemplate
	final void clearErrors() {
		form.getDefaultTracker().clear();
		setMessage(null);
	}

	/**
	 * Sets the object property from a given activation context value.
	 * 
	 * @param value an {@link #A}.
	 */
	final protected void setObjectFromActivationContext(A value) {
		setObject(activationContextEncoder.toObject(value));
	}

	/**
	 * Tells if the object is persistent.
	 * @return a <code>boolean</code>.
	 */
	final private boolean isObjectPersistent() {
		return object != null && controller.isPersistent(object);
	}

	/**
	 * Returns <code>null</code> if we are inserting a new object and
	 * {@link #DEFAULT_FORM_ZONE_ID} (<code>zone</code>) otherwise.
	 * 
	 * @return a {@link String}.
	 */
	public String getZone() {
		return isObjectPersistent() ? Constants.DEFAULT_FORM_ZONE_ID : null;
	}

	/**
	 * Sets the object to <code>null</code>.
	 */
	@OnEvent(Constants.NEW_OBJECT_EVENT)
	public final void clearObject() {
		setObject(null);
	}

}
