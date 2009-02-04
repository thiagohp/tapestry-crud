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

package br.com.arsmachina.tapestrycrud.pages;

import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.BeanEditForm;
import org.apache.tapestry5.corelib.components.BeanEditor;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.corelib.components.Select;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PropertyEditContext;
import org.apache.tapestry5.services.PropertyOutputContext;
import org.apache.tapestry5.services.ValueEncoderSource;

import br.com.arsmachina.tapestrycrud.encoder.LabelEncoder;
import br.com.arsmachina.tapestrycrud.selectmodel.SelectModelFactory;
import br.com.arsmachina.tapestrycrud.services.LabelEncoderSource;

/**
 * <p>
 * A page that holds the editing and viewing blocks provided by Tapestry CRUD for
 * {@link BeanEditor}, {@link BeanEditForm}, and {@link Grid}.
 * </p>
 * <p>
 * The <code>entity</code> view and edit blocks were written in a 
 * <a href="http://www.silexsistemas.com.br/">Sílex Sistemas Ltda.</a> project
 * and kindly donated to the Ars Machina Project. 
 * </p> 
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public class BeanModelBlocks {
	
	@Inject
	private LabelEncoderSource labelEncoderSource;
	
	@Inject
	private SelectModelFactory selectModelFactory;

	@Environmental
	@Property
	private PropertyOutputContext outputContext;
	
	@Inject
	private ValueEncoderSource valueEncoderSource;

	@Environmental
	@Property
	private PropertyEditContext editContext;
	
	/**
	 * Redirects to the root page if some user requests this pseudo-page.
	 * 
	 * @return the empty string.
	 */
	@OnEvent(EventConstants.ACTIVATE)
	public String redirectToIndex() {
		return "";
	}

	@Component(parameters = { 
			"value=editContext.propertyValue",
			"label=prop:editContext.label",
			"model=prop:entityModel",
			"clientId=prop:editContext.propertyId",
			"encoder=prop:entityEncoder"
	})
	@SuppressWarnings("unused")
	private Select entityField;
	
	/**
	 * Returns the entity converted in a user-presentable string.
	 * 
	 * @return a {@link String}.
	 */
	@SuppressWarnings("unchecked")
	public String getEntity() {
		
		Object propertyValue = outputContext.getPropertyValue();
		
		if (propertyValue == null) {
			return "";
		}
		
		LabelEncoder labelEncoder = labelEncoderSource.get(propertyValue.getClass());
		
		return labelEncoder.toLabel(propertyValue);
		
	}
	
	/**
	 * Returns the {@link SelectModel} used to select an entity.
	 * 
	 * @return a {@link SelectModel}.
	 */
	@SuppressWarnings("unchecked")
	public SelectModel getEntityModel() {
		
		Class propertyType = editContext.getPropertyType();
		return selectModelFactory.create(propertyType);
		
	}
	
	/**
	 * Returns the {@link ValueEncoder} used to select an entity.
	 * @param <T> the entity type.
	 * @return a {@link ValueEncoder}.
	 */
	@SuppressWarnings("unchecked")
	public <T> ValueEncoder<T> getEntityEncoder() {
		
		Class<T> type = editContext.getPropertyType();
		final ValueEncoder<T> valueEncoder = valueEncoderSource.getValueEncoder(type);
		return valueEncoder;
		
	}

}
