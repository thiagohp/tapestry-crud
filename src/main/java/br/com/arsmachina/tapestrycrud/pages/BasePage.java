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
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.apache.tapestry5.services.ValueEncoderSource;

import br.com.arsmachina.controller.Controller;
import br.com.arsmachina.tapestrycrud.Constants;
import br.com.arsmachina.tapestrycrud.CrudPage;
import br.com.arsmachina.tapestrycrud.encoder.ActivationContextEncoder;
import br.com.arsmachina.tapestrycrud.encoder.Encoder;
import br.com.arsmachina.tapestrycrud.encoder.LabelEncoder;
import br.com.arsmachina.tapestrycrud.selectmodel.SelectModelFactory;
import br.com.arsmachina.tapestrycrud.services.ActivationContextEncoderSource;
import br.com.arsmachina.tapestrycrud.services.ControllerSource;
import br.com.arsmachina.tapestrycrud.services.EncoderSource;
import br.com.arsmachina.tapestrycrud.services.LabelEncoderSource;

/**
 * Class that implements some common infrastructure for listing and editing pages.
 * 
 * @author Thiago H. de Paula Figueiredo (ThiagoHP)
 * @param <T> the entity class related to this encoder.
 * @param <K> the type of the class' primary key property.
 * @param <A> the type of the class' activation context.
 */
public abstract class BasePage<T, K extends Serializable, A extends Serializable> implements
		CrudPage<T, K, A> {

	@Inject
	private ActivationContextEncoderSource activationContextEncoderSource;

	@Inject
	private ControllerSource controllerSource;

	@Inject
	private EncoderSource encoderSource;

	@Inject
	private LabelEncoderSource labelEncoderSource;

	@Inject
	private ValueEncoderSource valueEncoderSource;

	@Inject
	private SelectModelFactory selectModelFactory;

	private Class<T> entityClass;

	private Controller<T, K> controller;

	@Persist(PersistenceConstants.FLASH)
	private String message;

	@Inject
	private ComponentResources componentResources;

	@Inject
	private BeanModelSource beanModelSource;

	@Inject
	private Messages messages;

	/**
	 * Single constructor of this class.
	 */
	@SuppressWarnings("unchecked")
	public BasePage() {

		final Type genericSuperclass = getClass().getGenericSuperclass();
		final ParameterizedType parameterizedType = ((ParameterizedType) genericSuperclass);
		entityClass = (Class<T>) parameterizedType.getActualTypeArguments()[0];

		controller = controllerSource.get(entityClass);

		assert entityClass != null;
		assert controller != null;

	}

	/**
	 * Creates a {@link BeanModel} for this entity class using
	 * <code>beanModelSource.create(entityClass, false, componentResources)</code>. This method
	 * can ve overriden if needed.
	 * 
	 * @return a {@link BeanModel}.
	 */
	public BeanModel<T> getBeanModel() {
		return beanModelSource.create(entityClass, filterReadOnlyComponentsInBeanModel(),
				getMessages());
	}

	/**
	 * Used by {@link #getBeanModel()} to filter read only components or not. This implementation
	 * returns <code>true</code>.
	 * 
	 * @return a <code>boolean</code>.
	 */
	protected boolean filterReadOnlyComponentsInBeanModel() {
		return true;
	}

	/**
	 * Returns the {@link ValueEncoder} for a given class.
	 * 
	 * @param <V> the class.
	 * @param clasz a {@link Class};
	 * @return a {@link ValueEncoder}.
	 */
	protected <V> ValueEncoder<V> getValueEncoder(Class<V> clasz) {
		return valueEncoderSource.getValueEncoder(clasz);
	}

	/**
	 * Returns the {@link LabelEncoder} for a given class.
	 * 
	 * @param <V> the class.
	 * @param clasz a {@link Class};
	 * @return a {@link LabelEncoder}.
	 */
	protected <V> LabelEncoder<V> getLabelEncoder(Class<V> clasz) {
		return labelEncoderSource.get(clasz);
	}

	/**
	 * Returns the {@link ActivationContextEncoder} for a given class.
	 * 
	 * @param <V> the class.
	 * @param <X> the class' activation context type.
	 * @param clasz a {@link Class};
	 * @return a {@link LabelEncoder}.
	 */
	protected <V, X extends Serializable> ActivationContextEncoder<V, X> getActivationContextEncoder(
			Class<V> clasz) {
		return activationContextEncoderSource.get(clasz);
	}

	/**
	 * Returns the {@link Encoder} for a given class.
	 * 
	 * @param <V> the class.
	 * @param <X> the class' primary key field type.
	 * @param <Y> the class' activation context type.
	 * @param clasz a {@link Class};
	 * @return a {@link LabelEncoder}.
	 */
	protected <V, Y extends Serializable, X extends Serializable> Encoder<V, Y, X> getEncoder(
			Class<V> clasz) {
		return encoderSource.get(clasz);
	}

	/**
	 * @see br.com.arsmachina.tapestrycrud.CrudPage#getMessage()
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @see br.com.arsmachina.tapestrycrud.CrudPage#setMessage(java.lang.String)
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Returns the value of the <code>controller</code> property.
	 * 
	 * @return a {@link Controller<T,K>}.
	 */
	public final Controller<T, K> getController() {
		return controller;
	}

	/**
	 * @see br.com.arsmachina.tapestrycrud.CrudPage#getEntityClass()
	 */
	public final Class<T> getEntityClass() {
		return entityClass;
	}

	/**
	 * Returns the value of the <code>messages</code> property.
	 * 
	 * @return a {@link Messages}.
	 */
	public final Messages getMessages() {
		return messages;
	}

	/**
	 * Returns the {@link Zone} that surrounds the form.
	 * 
	 * @return a {@link Zone}.
	 */
	public Zone getFormZone() {
		return (Zone) componentResources.getEmbeddedComponent(getFormZoneId());
	}

	/**
	 * ID of the {@link Block} that will be returned when a form is submitted via AJAX. This
	 * implementation returns {@link #DEFAULT_FORM_BLOCK_ID} (<code>block</code>).
	 * 
	 * @return a {@link String}.
	 */
	String getFormBlockId() {
		return Constants.DEFAULT_FORM_BLOCK_ID;
	}

	/**
	 * ID of the {@link Zone} that will be returned when a form is submitted via AJAX. This
	 * implementation returns {@link #DEFAULT_FORM_ZONE_ID} (<code>zone</code>).
	 * 
	 * @return a {@link String}.
	 */
	String getFormZoneId() {
		return Constants.DEFAULT_FORM_ZONE_ID;
	}

	/**
	 * Used by {@link #returnFromDoRemove()} to know whether it must return a {@link Zone} or a
	 * {@link Block}. This implementation returns <code>true</code>.
	 * 
	 * @return a <code>boolean</code>.
	 */
	protected boolean returnZoneOnXHR() {
		return true;
	}

	/**
	 * Returns the value of the <code>selectModelFactory</code> property.
	 * 
	 * @return a {@link SelectModelFactory}.
	 */
	final protected SelectModelFactory getSelectModelFactory() {
		return selectModelFactory;
	}

}
