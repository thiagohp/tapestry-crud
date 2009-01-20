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

package br.com.arsmachina.tapestrycrud.services.impl;

import java.io.Serializable;

import org.apache.tapestry5.PrimaryKeyEncoder;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.services.ValueEncoderFactory;

/**
 * A {@link ValueEncoder} implementation based in a {@link PrimaryKeyEncoder}.
 * 
 * @author Thiago H. de Paula Figueiredo
 * @param <T> the entity class related to this DAO.
 * @param <K> the type of the field that represents the entity class' primary key.
 */
public class PrimaryKeyEncoderValueEncoder<T, K extends Serializable> implements
		ValueEncoderFactory<T>, ValueEncoder<T> {

	final private Class<K> primaryKeyType;

	final private PrimaryKeyEncoder<K, T> primaryKeyEncoder;
	
	final private TypeCoercer typeCoercer;

	/**
	 * @param primaryKeyType
	 */
	public PrimaryKeyEncoderValueEncoder(Class<K> primaryKeyType,
			PrimaryKeyEncoder<K, T> primaryKeyEncoder, TypeCoercer typeCoercer) {

		if (primaryKeyType == null) {
			throw new IllegalArgumentException("Parameter primaryKeyType cannot be null");
		}

		if (primaryKeyEncoder == null) {
			throw new IllegalArgumentException("Parameter primaryKeyEncoder cannot be null");
		}
		
		if (typeCoercer == null) {
			throw new IllegalArgumentException("Parameter typeCoercer cannot be null");
		}

		this.primaryKeyEncoder = primaryKeyEncoder;
		this.primaryKeyType = primaryKeyType;
		this.typeCoercer = typeCoercer;

	}

	public ValueEncoder<T> create(Class<T> type) {
		return this;
	}

	public String toClient(T value) {
		
		final K key = primaryKeyEncoder.toKey(value);
		final String clientValue = typeCoercer.coerce(key, String.class);
		return clientValue;
		
	}

	public T toValue(String clientValue) {
		
		K key = typeCoercer.coerce(clientValue, primaryKeyType);
		T value = primaryKeyEncoder.toValue(key);
		
		return value;
		
	}

}