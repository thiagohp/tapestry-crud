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

import br.com.arsmachina.dao.DAO;
import br.com.arsmachina.tapestrycrud.factory.DefaultDAOFactory;
import br.com.arsmachina.tapestrycrud.services.DAOSource;
import br.com.arsmachina.tapestrycrud.services.EntitySource;

/**
 * {@link DAOSource} implementation.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public class DAOSourceImpl implements DAOSource {
	
	final private static Logger LOGGER = LoggerFactory.getLogger(DAOSourceImpl.class);

	@SuppressWarnings("unchecked")
	final private StrategyRegistry<DAO> registry;
	
	@SuppressWarnings("unchecked")
	final private Map<Class, DAO> defaultDAOs = new HashMap<Class, DAO>();
	
	final private DefaultDAOFactory defaultDAOFactory;

	/**
	 * Single constructor.
	 * 
	 * @param registrations a {@link Map}. It cannot be null.
	 * @param entitySource an {@link EntitySource}. It cannot be null.
	 * @param defaultDAOFactory a {@link DefaultDAOFactory}. It cannot be null.
	 * 
	 */
	@SuppressWarnings("unchecked")
	public DAOSourceImpl(Map<Class, DAO> registrations, EntitySource entitySource, DefaultDAOFactory defaultDAOFactory) {

		if (registrations == null) {
			throw new IllegalArgumentException("Parameter registrations cannot be null");
		}
		
		if (entitySource == null) {
			throw new IllegalArgumentException("Parameter entitySource cannot be null");
		}
		
		if (defaultDAOFactory == null) {
			throw new IllegalArgumentException("Parameter defaultDAOFactory cannot be null");
		}
		
		registry = StrategyRegistry.newInstance(DAO.class, registrations, true);
		this.defaultDAOFactory = defaultDAOFactory;
		
	}

	@SuppressWarnings("unchecked")
	public <T, K extends Serializable> DAO<T, K> get(Class<T> clasz) {
		
		DAO dao = registry.get(clasz);

		if (dao == null) {
			
			dao = defaultDAOs.get(clasz); 

			if (dao == null) {
				
				dao = defaultDAOFactory.build(clasz);
				
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Creating a default DAO for entity " + clasz.getName());
				}
				
				if (dao != null) {
					defaultDAOs.put(clasz, dao);
				}
				
			}
			
		}
		
		return dao;
		
	}

}