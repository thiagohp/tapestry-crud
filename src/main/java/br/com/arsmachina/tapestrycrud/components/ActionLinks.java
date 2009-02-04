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

package br.com.arsmachina.tapestrycrud.components;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.ioc.annotations.Inject;

import br.com.arsmachina.tapestrycrud.Constants;
import br.com.arsmachina.tapestrycrud.services.TapestryCrudModuleService;

/**
 * Component that renders the action links in a listing page. It is meant to be used in a
 * {@link Grid} column. <a href="http://ars-machina.svn.sourceforge.net/viewvc/ars-machina/example/trunk/src/main/webapp/project/ListProject.tml?view=markup"
 * >Ars Machina Project Example</a>. The default icons used are taken from the <a
 * href="http://www.famfamfam.com/lab/icons/silk/">Silk</a> icon set (Creative Commons Attribution
 * 2.5 License).
 * 
 * @author Thiago H. de Paula Figueiredo
 */
@IncludeStylesheet(Constants.TAPESTRY_CRUD_CSS_ASSET)
public class ActionLinks {

	private static final String IMAGES_ASSET_ROOT = "asset:classpath:/br/com/arsmachina/tapestrycrud/components/images/";

	private static final String DEFAULT_EDIT_ICON_ASSET = IMAGES_ASSET_ROOT + "edit.png";

	private static final String DEFAULT_DELETE_ICON_ASSET = IMAGES_ASSET_ROOT + "delete.png";

	private static final String DEFAULT_VIEW_ICON_ASSET = IMAGES_ASSET_ROOT + "view.png";

	/**
	 * Name of the page that is used to edits objects listed in this page.
	 */
	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String editPage;

	/**
	 * Name of the page that is used to edits objects listed in this page.
	 */
	@Parameter(defaultPrefix = BindingConstants.LITERAL)
	private String viewPage;

	/**
	 * Show the edit link?
	 */
	@Parameter(value = "true")
	@Property
	private boolean edit;

	/**
	 * Show the remove link?
	 */
	@Parameter(value = "true")
	@Property
	@SuppressWarnings("unused")
	private boolean remove;

	/**
	 * Show the view link?
	 */
	@Parameter(value = "false")
	@Property
	private boolean view;

	/**
	 * The object that the links will refer to.
	 */
	@Parameter(required = true, allowNull = false)
	@Property
	private Object object;

	@Parameter(defaultPrefix = BindingConstants.ASSET, value = DEFAULT_EDIT_ICON_ASSET)
	@Property
	@SuppressWarnings("unused")
	private Asset editIcon;

	@Parameter(defaultPrefix = BindingConstants.ASSET, value = DEFAULT_VIEW_ICON_ASSET)
	@Property
	@SuppressWarnings("unused")
	private Asset viewIcon;

	@Parameter(defaultPrefix = BindingConstants.ASSET, value = DEFAULT_DELETE_ICON_ASSET)
	@Property
	@SuppressWarnings("unused")
	private Asset deleteIcon;

	@Inject
	private TapestryCrudModuleService tapestryCrudModuleService;

	/**
	 * Returns the value of the <code>editPage</code> property.
	 * 
	 * @return a {@link String}.
	 */
	public String getEditPage() {

		if (editPage == null) {
		
			final Class<? extends Object> clasz = object.getClass();
			editPage = tapestryCrudModuleService.getEditPageURL(clasz);
			
		}
		
		if (edit && (editPage == null || editPage.trim().length() == 0)) {

			throw new IllegalArgumentException(
					"Could not find an edit page for " + object.getClass().getName());

		}

		return editPage;
		
	}

	/**
	 * Returns the value of the <code>viewPage</code> property.
	 * 
	 * @return a {@link String}.
	 */
	public String getViewPage() {

		if (view && viewPage == null || viewPage.trim().length() == 0) {

			throw new IllegalArgumentException(
					"When parameter view is true, parameter viewPage must have a non-empty value");

		}

		return viewPage;

	}

}
