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

import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.ValueEncoder;
import org.hibernate.SessionFactory;

import br.com.arsmachina.controller.Controller;


/**
 * {@link Encoder} implementation for classes with {@link Integer} activation context and primary
 * key field.
 * 
 * @param <T> the entity class related to this encoder.
 * @param <K> the type of the class' primary key property.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public class HibernateIntegerEncoder<T> extends HibernatePrimaryKeyEncoder<T, Integer> implements
		Encoder<T, Integer> {

	/**
	 * Single constructor of this class.
	 * 
	 * @param sessionFactory a {@link SessionFactory}.
	 * @param controller a {@link Controller}.
	 */
	public HibernateIntegerEncoder(SessionFactory sessionFactory, Controller<T, Integer> controller) {
		super(sessionFactory, controller);
	}

	public ValueEncoder<T> create(Class<T> type) {
		return this;
	}

	public Integer toActivationContext(T object) {
		return toKey(object);
	}

	public T toObject(EventContext context) {
		
		T object = null;
		
		if (context.getCount() > 0) {
			
			final Integer id = context.get(Integer.class, 0);
			object = toValue(id);
			
		}
		
		return object;
		
	}

	/**
	 * @see org.apache.tapestry5.ValueEncoder#toClient(java.lang.Object)
	 */
	public String toClient(T value) {
		final Integer key = toKey(value);
		return key != null ? key.toString() : "";
	}

	/**
	 * @see org.apache.tapestry5.ValueEncoder#toValue(java.lang.String)
	 */
	public T toValue(String clientValue) {

		T value = null;

		if (clientValue != null && clientValue.trim().length() > 0) {
			value = toValue(Integer.valueOf(clientValue));
		}

		return value;

	}

	/**
	 * Returns <code>object.toString()</code>. Override this method to use another value for the
	 * object label.
	 * 
	 * @see br.com.arsmachina.tapestrycrud.encoder.LabelEncoder#toLabel(java.lang.Object)
	 */
	public String toLabel(T object) {
		return object.toString();
	}

}
