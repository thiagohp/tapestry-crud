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
import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry5.PrimaryKeyEncoder;
import org.apache.tapestry5.ioc.util.StrategyRegistry;

import br.com.arsmachina.tapestrycrud.factory.PrimaryKeyEncoderFactory;
import br.com.arsmachina.tapestrycrud.services.EncoderSource;
import br.com.arsmachina.tapestrycrud.services.PrimaryKeyEncoderSource;

/**
 * {@link PrimaryKeyEncoderSource} implementation.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public class PrimaryKeyEncoderSourceImpl implements PrimaryKeyEncoderSource {
	
	@SuppressWarnings("unchecked")
	final private StrategyRegistry<PrimaryKeyEncoder> registry;

	final private EncoderSource encoderSource;

	final private PrimaryKeyEncoderFactory primaryKeyEncoderFactory;
	
	@SuppressWarnings("unchecked")
	final private Map<Class, PrimaryKeyEncoder> additionalEncoders = new HashMap<Class, PrimaryKeyEncoder>();
	
	/**
	 * Single constructor.
	 * 
	 * @param registrations a {@link Map}. It cannot be null.
	 * @param encoderSource an {@link EncoderSource}. It cannot be null.
	 * @param primaryKeyEncoderFactory an {@link PrimaryKeyEncoderFactory}. It cannot be null.
	 */
	@SuppressWarnings("unchecked")
	public PrimaryKeyEncoderSourceImpl(Map<Class, PrimaryKeyEncoder> registrations,
			EncoderSource encoderSource, PrimaryKeyEncoderFactory primaryKeyEncoderFactory) {

		if (registrations == null) {
			throw new IllegalArgumentException("Parameter registrations cannot be null");
		}

		if (encoderSource == null) {
			throw new IllegalArgumentException("Parameter encoderSource cannot be null");
		}

		if (primaryKeyEncoderFactory == null) {
			throw new IllegalArgumentException("Parameter primaryKeyEncoderFactory cannot be null");
		}
		
		registry = StrategyRegistry.newInstance(PrimaryKeyEncoder.class, registrations, true);

		this.encoderSource = encoderSource;
		this.primaryKeyEncoderFactory = primaryKeyEncoderFactory;

	}

	/**
	 * @see br.com.arsmachina.tapestrycrud.services.PrimaryKeyEncoderSource#get(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public <T, K extends Serializable> PrimaryKeyEncoder<K, T> get(Class<T> clasz) {

		PrimaryKeyEncoder<K, T> encoder = registry.get(clasz);

		if (encoder == null) {
			encoder = encoderSource.get(clasz);
		}

		if (encoder == null) {
			
			encoder = additionalEncoders.get(clasz);
			
			if (encoder == null) {
				
				encoder = primaryKeyEncoderFactory.build(clasz);
				
				if (encoder != null) {
					additionalEncoders.put(clasz, encoder);
				}
				
			}
			
		}

		if (encoder == null) {
			throw new IllegalArgumentException(
					"There is no PrimaryKeyEncoder configured for class " + clasz.getName());
		}

		return encoder;

	}
	

}
