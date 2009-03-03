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

import br.com.arsmachina.tapestrycrud.services.TreeServiceSource;
import br.com.arsmachina.tapestrycrud.tree.SingleTypeTreeService;

/**
 * {@link TreeServiceSource} implementation.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public class TreeServiceSourceImpl implements TreeServiceSource {
	
	@SuppressWarnings("unchecked")
	final private StrategyRegistry<SingleTypeTreeService> registry;

	/**
	 * Single constructor.
	 * 
	 * @param registrations
	 */
	@SuppressWarnings("unchecked")
	public TreeServiceSourceImpl(Map<Class, SingleTypeTreeService> registrations) {

		if (registrations == null) {
			throw new IllegalArgumentException("Parameter registrations cannot be null");
		}

		registry = StrategyRegistry.newInstance(SingleTypeTreeService.class, registrations);

	}

	/**
	 * @see br.com.arsmachina.tapestrycrud.services.TreeServiceSource#get(java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public <T> SingleTypeTreeService<T> get(Class<T> clasz) {
		return registry.get(clasz);
	}

}
