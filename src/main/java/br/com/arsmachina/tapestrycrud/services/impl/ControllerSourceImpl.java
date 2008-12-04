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

import org.apache.tapestry5.ioc.util.StrategyRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.arsmachina.controller.Controller;
import br.com.arsmachina.tapestrycrud.factory.DefaultControllerFactory;
import br.com.arsmachina.tapestrycrud.services.ControllerSource;

/**
 * {@link ControllerSource} implementation.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public class ControllerSourceImpl implements ControllerSource {

	final private static Logger LOGGER = LoggerFactory.getLogger(ControllerSourceImpl.class);
	
	@SuppressWarnings("unchecked")
	final private StrategyRegistry<Controller> registry;
	
	final private DefaultControllerFactory defaultControllerFactory;
	
	@SuppressWarnings("unchecked")
	final private Map<Class, Controller> additionalControllers = new HashMap<Class, Controller>();

	/**
	 * Single constructor.
	 * 
	 * @param registrations
	 */
	@SuppressWarnings("unchecked")
	public ControllerSourceImpl(Map<Class, Controller> registrations, DefaultControllerFactory defaultControllerFactory) {
		
		if (registrations == null) {
			throw new IllegalArgumentException("Parameter registrations cannot be null");
		}
		
		if (defaultControllerFactory == null) {
			throw new IllegalArgumentException("Parameter defaultControllerFactory cannot be null");
		}
		
		registry = StrategyRegistry.newInstance(Controller.class, registrations, true);
		this.defaultControllerFactory = defaultControllerFactory;
		
	}

	@SuppressWarnings("unchecked")
	public <T, K extends Serializable> Controller<T, K> get(Class<T> clasz) {
		
		Controller controller = registry.get(clasz);
		
		if (controller == null) {
			
			controller = additionalControllers.get(clasz);
			
			if (controller == null) {
				
				controller = defaultControllerFactory.build(clasz);

				if (controller != null) {
					
					additionalControllers.put(clasz, controller);
					
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("Created default controller for " + clasz.getName());
					}
					
				}
				
			}
			
		}
		
		return controller;
		
	}

}
