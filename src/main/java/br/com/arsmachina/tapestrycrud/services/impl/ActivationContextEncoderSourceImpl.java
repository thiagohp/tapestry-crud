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
import java.util.Map;


import org.apache.tapestry5.ioc.util.StrategyRegistry;

import br.com.arsmachina.tapestrycrud.encoder.ActivationContextEncoder;
import br.com.arsmachina.tapestrycrud.services.ActivationContextEncoderSource;
import br.com.arsmachina.tapestrycrud.services.EncoderSource;

/**
 * {@link ActivationContextEncoderSource} implementation.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public class ActivationContextEncoderSourceImpl implements ActivationContextEncoderSource {

	@SuppressWarnings("unchecked")
	final private StrategyRegistry<ActivationContextEncoder> registry;

	final private EncoderSource encoderSource;

	/**
	 * Single constructor.
	 * 
	 * @param registrations
	 */
	@SuppressWarnings("unchecked")
	public ActivationContextEncoderSourceImpl(Map<Class, ActivationContextEncoder> registrations,
			EncoderSource encoderSource) {

		if (registrations == null) {
			throw new IllegalArgumentException("Parameter registrations cannot be null");
		}

		if (encoderSource == null) {
			throw new IllegalArgumentException("Parameter encoderSource cannot be null");
		}

		registry = StrategyRegistry.newInstance(ActivationContextEncoder.class, registrations, true);

		this.encoderSource = encoderSource;

	}

	/**
	 * @see br.com.arsmachina.tapestrycrud.services.ActivationContextEncoderSource#get(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public <T, A extends Serializable> ActivationContextEncoder<T, A> get(Class<T> clasz) {

		ActivationContextEncoder<T, A> encoder = registry.get(clasz);

		if (encoder == null) {
			encoder = encoderSource.get(clasz);
		}

		if (encoder == null) {
			throw new IllegalArgumentException(
					"There is no ActivationContextEncoder configured for class " + clasz.getName());
		}

		return encoder;

	}

}
