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

import java.util.Collections;
import java.util.Set;

import org.apache.tapestry5.PrimaryKeyEncoder;

import br.com.arsmachina.module.service.ModuleService;
import br.com.arsmachina.tapestrycrud.encoder.ActivationContextEncoder;
import br.com.arsmachina.tapestrycrud.encoder.Encoder;
import br.com.arsmachina.tapestrycrud.encoder.LabelEncoder;
import br.com.arsmachina.tapestrycrud.module.TapestryCrudModule;
import br.com.arsmachina.tapestrycrud.services.TapestryCrudModuleService;

/**
 * Default {@link ModuleService} implementation.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public class TapestryCrudModuleServiceImpl implements TapestryCrudModuleService {

	final private Set<TapestryCrudModule> modules;

	/**
	 * Single constructor of this class.
	 * 
	 * @param entityClasses a {@link Set} of {@link TapestryCrudModule}s. It cannot be null.
	 */
	public TapestryCrudModuleServiceImpl(Set<TapestryCrudModule> modules) {

		if (modules == null) {
			throw new IllegalArgumentException("Parameter modules cannot be null");
		}

		this.modules = Collections.unmodifiableSet(modules);

	}

	public Set<TapestryCrudModule> getModules() {
		return modules;
	}

	public <T> Class<? extends ActivationContextEncoder<T>> getActivationContextEncoderClass(
			Class<T> entityClass) {
		
		Class<? extends ActivationContextEncoder<T>> encoder = null;
		
		for (TapestryCrudModule module : modules) {
			
			encoder = module.getActivationContextEncoderClass(entityClass);
			
			if (encoder != null) {
				break;
			}
			
		}
		
		return encoder;
		
	}

	public <T> Class<? extends Encoder<T, ?>> getEncoderClass(
			Class<T> entityClass) {
		
		Class<? extends Encoder<T, ?>> encoder = null;
		
		for (TapestryCrudModule module : modules) {
			
			encoder = module.getEncoderClass(entityClass);
			
			if (encoder != null) {
				break;
			}
			
		}
		
		return encoder;
		
	}

	public <T> Class<? extends LabelEncoder<T>> getLabelEncoderClass(Class<T> entityClass) {
		
		Class<? extends LabelEncoder<T>> encoder = null;
		
		for (TapestryCrudModule module : modules) {
			
			encoder = module.getLabelEncoderClass(entityClass);
			
			if (encoder != null) {
				break;
			}
			
		}
		
		return encoder;

		
	}

	public <T> Class<? extends PrimaryKeyEncoder<?, T>> getPrimaryKeyEncoderClass(
			Class<T> entityClass) {

		Class<? extends PrimaryKeyEncoder<?, T>> encoder = null;
		
		for (TapestryCrudModule module : modules) {
			
			encoder = module.getPrimaryKeyEncoderClass(entityClass);
			
			if (encoder != null) {
				break;
			}
			
		}
		
		return encoder;

	}

}
