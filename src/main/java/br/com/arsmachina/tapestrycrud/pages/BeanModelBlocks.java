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

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.FieldValidator;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.BeanEditForm;
import org.apache.tapestry5.corelib.components.BeanEditor;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.corelib.components.Select;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PropertyEditContext;
import org.apache.tapestry5.services.PropertyOutputContext;
import org.apache.tapestry5.services.ValueEncoderSource;

import br.com.arsmachina.authorization.Authorizer;
import br.com.arsmachina.tapestrycrud.components.ActionLinks;
import br.com.arsmachina.tapestrycrud.encoder.LabelEncoder;
import br.com.arsmachina.tapestrycrud.selectmodel.SelectModelFactory;
import br.com.arsmachina.tapestrycrud.services.LabelEncoderSource;
import br.com.arsmachina.tapestrycrud.services.TapestryCrudModuleService;

/**
 * <p>
 * A page that holds the editing and viewing blocks provided by Tapestry CRUD
 * for {@link BeanEditor}, {@link BeanEditForm}, and {@link Grid}.
 * </p>
 * <p>
 * The <code>entity</code> view and edit blocks were written in a <a
 * href="http://www.silexsistemas.com.br/">Sílex Sistemas Ltda.</a> project and
 * kindly donated to the Ars Machina Project.
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

	@Inject
	private Authorizer authorizer;

	@Inject
	private TapestryCrudModuleService tapestryCrudModuleService;

	@Parameter(defaultPrefix = BindingConstants.ASSET, value = ActionLinks.DEFAULT_EDIT_ICON_ASSET)
	@Property
	@SuppressWarnings("unused")
	private Asset editIcon;

	private Boolean canView;

	private Boolean canEdit;

	/**
	 * Redirects to the root page if some user requests this pseudo-page.
	 * 
	 * @return the empty string.
	 */
	@OnEvent(EventConstants.ACTIVATE)
	public String redirectToIndex() {
		return "";
	}

	@Component(parameters = { "value=editContext.propertyValue",
			"label=prop:editContext.label", "model=prop:entityModel",
			"clientId=prop:editContext.propertyId", "validate=prop:entityFieldValidator",
			"encoder=prop:entityEncoder" })
	private Select entityField;

	/**
	 * Returns the entity converted in a user-presentable string.
	 * 
	 * @return a {@link String}.
	 */
	@SuppressWarnings("unchecked")
	public String getEntityAsString() {

		Object propertyValue = getEntity();

		if (propertyValue == null) {
			return "";
		}

		LabelEncoder labelEncoder =
			labelEncoderSource.get(propertyValue.getClass());

		return labelEncoder.toLabel(propertyValue);

	}
	
	@SuppressWarnings("unchecked")
	public FieldValidator getEntityFieldValidator()
    {
        return editContext.getValidator(entityField);
    }


	public boolean isEntityEditable() {

		final Object entity = getEntity();
		return authorizer.canUpdate(entity.getClass())
				&& authorizer.canUpdate(entity);

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
	 * 
	 * @param <T> the entity type.
	 * @return a {@link ValueEncoder}.
	 */
	@SuppressWarnings("unchecked")
	public <T> ValueEncoder<T> getEntityEncoder() {

		Class<T> type = editContext.getPropertyType();
		final ValueEncoder<T> valueEncoder =
			valueEncoderSource.getValueEncoder(type);
		return valueEncoder;

	}

	/**
	 * Returns the value of the <code>canView</code> property.
	 * 
	 * @return a <code>boolean</code>.
	 */
	public boolean isCanView() {

		Object object = getEntity();
		canView =
			object != null && authorizer.canRead(object.getClass())
					&& authorizer.canRead(object);

		return canView;

	}

	/**
	 * Returns the value of the <code>canUpdate</code> property.
	 * 
	 * @return a <code>boolean</code>.
	 */
	public boolean isCanEdit() {

		Object object = getEntity();
		canEdit =
			object != null && authorizer.canUpdate(object.getClass())
					&& authorizer.canUpdate(object);
		
		return canEdit;

	}

	/**
	 * Returns the value of the <code>canViewAndEdit</code> property.
	 * 
	 * @return a <code>boolean</code>.
	 */
	public boolean isCanViewAndEdit() {
		return isCanView() && isCanEdit();
	}

	/**
	 * Returns the value of the <code>canViewAndEdit</code> property.
	 * 
	 * @return a <code>boolean</code>.
	 */
	public boolean isCanOnlyEdit() {
		return isCanView() == false && isCanEdit();
	}

	/**
	 * Returns the value of the <code>cannnotViewNorEdit</code> property.
	 * 
	 * @return a <code>boolean</code>.
	 */
	public boolean isCannotViewNorEdit() {
		return isCanView() == false && isCanEdit() == false;
	}

	/**
	 * Returns the edited entity class being edited.
	 * 
	 * @return an {@link Object}.
	 */
	public Object getEntity() {
		return outputContext.getPropertyValue();
	}

	public String getViewPage() {
		return tapestryCrudModuleService.getViewPageURL(getEntity().getClass());
	}

	public String getEditPage() {
		return tapestryCrudModuleService.getEditPageURL(getEntity().getClass());
	}

}
