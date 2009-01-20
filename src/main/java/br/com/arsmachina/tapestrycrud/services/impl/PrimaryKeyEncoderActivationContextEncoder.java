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

import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.PrimaryKeyEncoder;

import br.com.arsmachina.tapestrycrud.encoder.ActivationContextEncoder;

/**
 * An {@link ActivationContextEncoder} implementation based in a {@link PrimaryKeyEncoder}.
 * 
 * @author Thiago H. de Paula Figueiredo
 * @param <T> the entity class related to this DAO.
 * @param <K> the type of the field that represents the entity class' primary key.
 */
class PrimaryKeyEncoderActivationContextEncoder<T, K extends Serializable> implements
		ActivationContextEncoder<T> {

	final private Class<K> primaryKeyType;

	final private PrimaryKeyEncoder<K, T> primaryKeyEncoder;

	/**
	 * @param primaryKeyType
	 */
	public PrimaryKeyEncoderActivationContextEncoder(PrimaryKeyEncoder<K, T> primaryKeyEncoder,
			Class<K> primaryKeyType) {

		if (primaryKeyType == null) {
			throw new IllegalArgumentException("Parameter primaryKeyType cannot be null");
		}

		if (primaryKeyEncoder == null) {
			throw new IllegalArgumentException("Parameter primaryKeyEncoder cannot be null");
		}

		this.primaryKeyEncoder = primaryKeyEncoder;
		this.primaryKeyType = primaryKeyType;

	}

	/**
	 * @see br.com.arsmachina.tapestrycrud.encoder.ActivationContextEncoder#toActivationContext(java.lang.Object)
	 */
	public Object toActivationContext(T object) {
		return primaryKeyEncoder.toKey(object);
	}

	/**
	 * @see br.com.arsmachina.tapestrycrud.encoder.ActivationContextEncoder#toObject(org.apache.tapestry5.EventContext)
	 */
	public T toObject(EventContext value) {
		return primaryKeyEncoder.toValue(value.get(primaryKeyType, 0));
	}

}