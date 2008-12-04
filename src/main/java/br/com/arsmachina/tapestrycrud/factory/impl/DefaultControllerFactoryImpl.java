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

package br.com.arsmachina.tapestrycrud.factory.impl;

import java.io.Serializable;

import br.com.arsmachina.controller.Controller;
import br.com.arsmachina.controller.impl.ControllerImpl;
import br.com.arsmachina.dao.DAO;
import br.com.arsmachina.tapestrycrud.factory.DefaultControllerFactory;
import br.com.arsmachina.tapestrycrud.services.DAOSource;

/**
 * Default {@link DefaultControllerFactory} implementation.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public class DefaultControllerFactoryImpl implements DefaultControllerFactory {

	final DAOSource daoSource;

	/**
	 * @param defaultDAOFactory
	 */
	public DefaultControllerFactoryImpl(DAOSource daoSource) {
		
		if (daoSource == null) {
			throw new IllegalArgumentException("Parameter daSource cannot be null");
		}
		
		this.daoSource = daoSource;
		
	}

	@SuppressWarnings("unchecked")
	public <T> Controller<T, ?> build(Class<T> entityClass) {
		
		Controller<T, ?> controller = null;
		final DAO<T, Serializable> dao = daoSource.get(entityClass);

		if (dao != null) {
			controller = new ConcreteControllerImpl(dao);
		}
		
		return controller;
		
	}

	@SuppressWarnings("unused")
	final private static class ConcreteControllerImpl<T, K extends Serializable> extends
			ControllerImpl<T, K> {

		/**
		 * @param dao
		 */
		public ConcreteControllerImpl(DAO<T, K> dao) {
			super(dao);
		}

	}

}
