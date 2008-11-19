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

import java.util.Map;

import org.apache.tapestry5.PrimaryKeyEncoder;
import org.apache.tapestry5.ioc.util.StrategyRegistry;

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

	/**
	 * Single constructor.
	 * 
	 * @param registrations
	 */
	@SuppressWarnings("unchecked")
	public PrimaryKeyEncoderSourceImpl(Map<Class, PrimaryKeyEncoder> registrations,
			EncoderSource encoderSource) {

		if (registrations == null) {
			throw new IllegalArgumentException("Parameter registrations cannot be null");
		}

		if (encoderSource == null) {
			throw new IllegalArgumentException("Parameter encoderSource cannot be null");
		}

		registry = StrategyRegistry.newInstance(PrimaryKeyEncoder.class, registrations, true);

		this.encoderSource = encoderSource;

	}

	/**
	 * @see br.com.arsmachina.tapestrycrud.services.PrimaryKeyEncoderSource#get(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public <T> PrimaryKeyEncoder<?, T> get(Class<T> clasz) {

		PrimaryKeyEncoder<?, T> encoder = registry.get(clasz);

		if (encoder == null) {
			encoder = encoderSource.get(clasz);
		}

		if (encoder == null) {
			throw new IllegalArgumentException("There is no PrimaryKeyEncoder configured for class "
					+ clasz.getName());
		}

		return encoder;

	}

}
