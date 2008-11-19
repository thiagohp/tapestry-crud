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


import org.apache.tapestry5.ioc.util.StrategyRegistry;

import br.com.arsmachina.tapestrycrud.encoder.LabelEncoder;
import br.com.arsmachina.tapestrycrud.services.EncoderSource;
import br.com.arsmachina.tapestrycrud.services.LabelEncoderSource;

/**
 * {@link LabelEncoderSource} implementation.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public class LabelEncoderSourceImpl implements LabelEncoderSource {
	
	final private static LabelEncoder<Object> DEFAULT_ENCODER = new ToStringEncoder();

	@SuppressWarnings("unchecked")
	final private StrategyRegistry<LabelEncoder> registry;

	final private EncoderSource encoderSource;

	/**
	 * Single constructor.
	 * 
	 * @param registrations
	 */
	@SuppressWarnings("unchecked")
	public LabelEncoderSourceImpl(Map<Class, LabelEncoder> registrations,
			EncoderSource encoderSource) {

		if (registrations == null) {
			throw new IllegalArgumentException("Parameter registrations cannot be null");
		}

		if (encoderSource == null) {
			throw new IllegalArgumentException("Parameter encoderSource cannot be null");
		}

		registry = StrategyRegistry.newInstance(LabelEncoder.class, registrations, true);

		this.encoderSource = encoderSource;

	}

	/**
	 * @see br.com.arsmachina.tapestrycrud.services.LabelEncoderSource#get(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public <T> LabelEncoder<T> get(Class<T> clasz) {

		LabelEncoder<T> encoder = registry.get(clasz);

		if (encoder == null) {
			encoder = encoderSource.get(clasz);
		}

		if (encoder == null) {
			encoder = (LabelEncoder<T>) DEFAULT_ENCODER;
		}

		return encoder;

	}
	
	final private static class ToStringEncoder implements LabelEncoder<Object> {

		public String toLabel(Object object) {
			return object.toString();
		}
		
	}

}
