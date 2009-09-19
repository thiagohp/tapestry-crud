// Copyright 2008-2009 Thiago H. de Paula Figueiredo
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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.tapestry5.ioc.ObjectLocator;
import org.apache.tapestry5.ioc.util.StrategyRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.arsmachina.tapestrycrud.beanmodel.AbstractBeanModelCustomizer;
import br.com.arsmachina.tapestrycrud.beanmodel.BeanModelCustomizer;
import br.com.arsmachina.tapestrycrud.services.BeanModelCustomizerSource;
import br.com.arsmachina.tapestrycrud.services.TapestryCrudModuleService;

/**
 * {@link BeanModelCustomizerSource} implementation.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public class BeanModelCustomizerSourceImpl implements BeanModelCustomizerSource {
	
	@SuppressWarnings("unchecked")
	private static final AbstractBeanModelCustomizer DUMMY_BEAN_MODEL_CUSTOMIZER =
		new AbstractBeanModelCustomizer(){};

	final private static Logger LOGGER = LoggerFactory.getLogger(BeanModelCustomizerSource.class);
	
	@SuppressWarnings("unchecked")
	final private StrategyRegistry<BeanModelCustomizer> registry;
	
	@SuppressWarnings("unchecked")
	final private Map<Class, BeanModelCustomizer> additional;
	
	@SuppressWarnings("unchecked")
	final private Set<Class> noMatch;
	
	final private TapestryCrudModuleService tapestryCrudModuleService;
	
	final private ObjectLocator objectLocator;

	/**
	 * Single constructor.
	 * 
	 * @param registrations
	 */
	@SuppressWarnings("unchecked")
	public BeanModelCustomizerSourceImpl(Map<Class, BeanModelCustomizer> registrations, 
			TapestryCrudModuleService tapestryCrudModuleService, ObjectLocator objectLocator) {

		if (registrations == null) {
			throw new IllegalArgumentException("Parameter registrations cannot be null");
		}
		
		this.tapestryCrudModuleService = tapestryCrudModuleService;
		this.objectLocator = objectLocator;

		registry = StrategyRegistry.newInstance(BeanModelCustomizer.class, registrations, true);
		additional = new HashMap<Class, BeanModelCustomizer>();
		noMatch = new HashSet<Class>();

	}

	@SuppressWarnings("unchecked")
	public <T> BeanModelCustomizer<T> get(Class<T> clasz) {

		// we try the registered ones first
		BeanModelCustomizer customizer = registry.get(clasz);
		
		// the additional ones (created by this class) second
		if (customizer == null) {
			customizer = additional.get(clasz);
		}
		
		// customizer still not found, but the class insn't in the list of ones that don't have
		// a customizer
		if (customizer == null && noMatch.contains(clasz) == false) {
			
			Class<? extends BeanModelCustomizer<T>> customizerClass = 
				tapestryCrudModuleService.getBeanModelCustomizerClass(clasz);
			
			if (customizerClass != null) {
				
				customizer = objectLocator.autobuild(customizerClass);
				additional.put(clasz, customizer);
				
				if (LOGGER.isDebugEnabled()) {
	
					final String entityName = clasz.getSimpleName();
					final String customizerClassName =
						customizerClass.getName();
					final String message =
						String.format("Associating entity %s with bean model customizer %s",
								entityName, customizerClassName);
	
					LOGGER.debug(message);
					
				}
				
			}
			// we couldn't find a customizer for this class by any means
			else {
				noMatch.add(clasz);
			}
			
		}
		
		// no customizer found, so we use a dummy one
		if (customizer == null) {
			customizer = DUMMY_BEAN_MODEL_CUSTOMIZER;
		}
		
		return customizer;
		
	}

}