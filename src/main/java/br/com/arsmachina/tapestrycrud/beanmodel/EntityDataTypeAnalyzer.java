// Copyright 2009 Thiago H. de Paula Figueiredo
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

package br.com.arsmachina.tapestrycrud.beanmodel;

import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.ioc.services.PropertyAdapter;
import org.apache.tapestry5.services.DataTypeAnalyzer;

import br.com.arsmachina.tapestrycrud.Constants;
import br.com.arsmachina.tapestrycrud.services.TapestryCrudModuleService;

/**
 * {@link DataTypeAnalyzer} implementation that classifies any entity classes as the
 * <code>entity</code> Tapestry data type.
 * 
 * @author Thiago H. de Paula Figueiredo
 * @see BeanModel
 */
public class EntityDataTypeAnalyzer implements DataTypeAnalyzer {

	private TapestryCrudModuleService tapestryCrudModuleService;

	/**
	 * Single constructor of this class.
	 * 
	 * @param tapestryCrudModuleService a {@link TapestryCrudModuleService}. It cannot be null.
	 */
	public EntityDataTypeAnalyzer(TapestryCrudModuleService tapestryCrudModuleService) {

		if (tapestryCrudModuleService == null) {
			throw new IllegalArgumentException("Parameter tapestryCrudModuleService cannot be null");
		}

		this.tapestryCrudModuleService = tapestryCrudModuleService;

	}

	public String identifyDataType(PropertyAdapter adapter) {

		String type = null;

		Class<?> clasz = adapter.getType();

		if (tapestryCrudModuleService.contains(clasz)) {
			type = Constants.ENTITY_DATA_TYPE;
		}

		return type;

	}

}
