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


import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.hibernate.SessionFactory;

import br.com.arsmachina.controller.Controller;

/**
 * Partial {@link Encoder} implementation using Hibernate.
 * 
 * @author Thiago H. de Paula Figueiredo (ThiagoHP)
 * @param <T> the entity class related to this encoder.
 * @param <K> the type of the class' primary key property.
 * @param <A> the type of the class' activation context.
 */
public abstract class AbstractHibernateEncoder<T, K extends Serializable, A extends Serializable>
		extends HibernatePrimaryKeyEncoder<T, K> implements Encoder<T, K, A> {

	/**
	 * Single constructor of this class.
	 * 
	 * @param sessionFactory a {@link SessionFactory}. It cannot be null.
	 * @param controller a {@link Controller}. It cannot be null.
	 * @param typeCoercer a {@link TypeCoercer}. It cannot be null.
	 */
	public AbstractHibernateEncoder(SessionFactory sessionFactory, Controller<T, K> controller) {
		super(sessionFactory, controller);
	}

	/**
	 * Returns itself.
	 * 
	 * @see org.apache.tapestry.services.ValueEncoderFactory#create(java.lang.Class)
	 */
	public ValueEncoder<T> create(Class<T> type) {
		return this;
	}
	

}
