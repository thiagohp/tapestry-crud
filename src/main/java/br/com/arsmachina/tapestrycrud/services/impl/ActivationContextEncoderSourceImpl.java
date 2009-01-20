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

import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry5.PrimaryKeyEncoder;
import org.apache.tapestry5.ioc.util.StrategyRegistry;

import br.com.arsmachina.module.service.PrimaryKeyTypeService;
import br.com.arsmachina.tapestrycrud.encoder.ActivationContextEncoder;
import br.com.arsmachina.tapestrycrud.services.ActivationContextEncoderSource;
import br.com.arsmachina.tapestrycrud.services.EncoderSource;
import br.com.arsmachina.tapestrycrud.services.PrimaryKeyEncoderSource;

/**
 * {@link ActivationContextEncoderSource} implementation.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public class ActivationContextEncoderSourceImpl implements ActivationContextEncoderSource {

	@SuppressWarnings("unchecked")
	final private StrategyRegistry<ActivationContextEncoder> registry;

	final private EncoderSource encoderSource;
	
	final private PrimaryKeyEncoderSource primaryKeyEncoderSource;
	
	final private PrimaryKeyTypeService primaryKeyTypeService;

	@SuppressWarnings("unchecked")
	final private Map<Class, ActivationContextEncoder> additionalEncoders = new HashMap<Class, ActivationContextEncoder>();

	/**
	 * Single constructor.
	 * 
	 * @param registrations
	 */
	@SuppressWarnings("unchecked")
	public ActivationContextEncoderSourceImpl(Map<Class, ActivationContextEncoder> registrations,
			EncoderSource encoderSource, PrimaryKeyEncoderSource primaryKeyEncoderSource,
			PrimaryKeyTypeService primaryKeyTypeService) {

		if (registrations == null) {
			throw new IllegalArgumentException("Parameter registrations cannot be null");
		}

		if (encoderSource == null) {
			throw new IllegalArgumentException("Parameter encoderSource cannot be null");
		}
		
		if (primaryKeyTypeService == null) {
			throw new IllegalArgumentException("Parameter primaryKeyTypeService cannot be null");
		}
		
		registry = StrategyRegistry.newInstance(ActivationContextEncoder.class, registrations, true);

		this.encoderSource = encoderSource;
		this.primaryKeyEncoderSource = primaryKeyEncoderSource;
		this.primaryKeyTypeService = primaryKeyTypeService;
		

	}

	@SuppressWarnings("unchecked")
	public <T> ActivationContextEncoder<T> get(Class<T> clasz) {

		ActivationContextEncoder<T> encoder = registry.get(clasz);

		if (encoder == null) {
			encoder = encoderSource.get(clasz);
		}

		if (encoder == null) {

			encoder = additionalEncoders.get(clasz);
			
			if (encoder == null) {
				
				PrimaryKeyEncoder<?, T> primaryKeyEncoder = primaryKeyEncoderSource.get(clasz);
				
				if (primaryKeyEncoder != null) {
					
					Class primaryKeyType = primaryKeyTypeService.getPrimaryKeyType(clasz);
					encoder = 
						new PrimaryKeyEncoderActivationContextEncoder(primaryKeyEncoder, primaryKeyType);
					
					additionalEncoders.put(clasz, encoder);
					
				}
				
			}
			
		}

		if (encoder == null) {
			throw new IllegalArgumentException(
					"There is no ActivationContextEncoder configured for class " + clasz.getName());
		}

		return encoder;

	}

}
