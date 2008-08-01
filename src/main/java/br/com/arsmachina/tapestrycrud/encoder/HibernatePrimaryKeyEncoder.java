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

package br.com.arsmachina.tapestrycrud.encoder;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;


import org.apache.tapestry5.PrimaryKeyEncoder;
import org.hibernate.EntityMode;
import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;

import br.com.arsmachina.controller.Controller;

/**
 * Partial {@link PrimaryKeyEncoder} implementation using Hibernate and a {@link Controller}.
 * 
 * @param <T> the entity class related to this controller.
 * @param <K> the type of the field that represents the entity class' primary key.
 * 
 * @author Thiago H. de Paula Figueiredo (ThiagoHP)
 */
public class HibernatePrimaryKeyEncoder<T, K extends Serializable> implements
		PrimaryKeyEncoder<K, T> {

	final private ClassMetadata classMetadata;

	final private Controller<T, K> controller;

	/**
	 * Single construtor of this class.
	 * 
	 * @param sessionFactory a {@link SessionFactory}. It cannot be null.
	 * @param controller a {@link Controller}. It cannot be null.
	 */
	@SuppressWarnings("unchecked")
	public HibernatePrimaryKeyEncoder(SessionFactory sessionFactory, Controller<T, K> controller) {

		if (sessionFactory == null) {
			throw new IllegalArgumentException("Parameter sessionFactory cannot be null");
		}

		if (controller == null) {
			throw new IllegalArgumentException("Parameter controller cannot be null");
		}

		this.controller = controller;

		final Type genericSuperclass = getClass().getGenericSuperclass();
		final ParameterizedType parameterizedType = ((ParameterizedType) genericSuperclass);

		Class<T> entityClass = (Class<T>) parameterizedType.getActualTypeArguments()[0];

		classMetadata = sessionFactory.getClassMetadata(entityClass);

	}

	/**
	 * @see org.apache.tapestry.PrimaryKeyEncoder#toKey(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public K toKey(T value) {
		
		K key = null;

		if (value != null) {
			key = (K) classMetadata.getIdentifier(value, EntityMode.POJO);
		}
		
		return key;
		
	}

	/**
	 * Does nothing.
	 * 
	 * @see org.apache.tapestry.PrimaryKeyEncoder#prepareForKeys(java.util.List)
	 */
	public void prepareForKeys(List<K> keys) {
	}

	/**
	 * Returns <code>controller.findById(key)</code>. {@inheritDoc}
	 * 
	 * @see org.apache.tapestry.PrimaryKeyEncoder#toValue(java.io.Serializable)
	 */
	public T toValue(K key) {
		return controller.findById(key);
	}

	/**
	 * Returns the value of the <code>controller</code> property.
	 * 
	 * @return a {@link Controller<T,K>}.
	 */
	public final Controller<T, K> getController() {
		return controller;
	}

}
