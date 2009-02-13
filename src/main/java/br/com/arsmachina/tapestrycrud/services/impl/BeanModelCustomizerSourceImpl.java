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

import br.com.arsmachina.tapestrycrud.beanmodel.AbstractBeanModelCustomizer;
import br.com.arsmachina.tapestrycrud.beanmodel.BeanModelCustomizer;
import br.com.arsmachina.tapestrycrud.services.BeanModelCustomizerSource;

/**
 * {@link BeanModelCustomizerSource} implementation.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public class BeanModelCustomizerSourceImpl implements BeanModelCustomizerSource {
	
	@SuppressWarnings("unchecked")
	final private StrategyRegistry<BeanModelCustomizer> registry;

	/**
	 * Single constructor.
	 * 
	 * @param registrations
	 */
	@SuppressWarnings("unchecked")
	public BeanModelCustomizerSourceImpl(Map<Class, BeanModelCustomizer> registrations) {

		if (registrations == null) {
			throw new IllegalArgumentException("Parameter registrations cannot be null");
		}
		
		if (registrations.get(Object.class) == null) {
			registrations.put(Object.class, new AbstractBeanModelCustomizer(){});
		}

		registry = StrategyRegistry.newInstance(BeanModelCustomizer.class, registrations);

	}

	@SuppressWarnings("unchecked")
	public <T> BeanModelCustomizer<T> get(Class<T> clasz) {
		return registry.get(clasz);
	}

}
