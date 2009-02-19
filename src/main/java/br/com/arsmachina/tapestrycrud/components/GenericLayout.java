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

package br.com.arsmachina.tapestrycrud.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import br.com.arsmachina.tapestrycrud.Constants;
import br.com.arsmachina.tapestrycrud.services.PageUtil;

/**
 * A generic layout component. This class is meant to be used as a superclass of layout components,
 * not by itself.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
@IncludeStylesheet({"classpath:/br/com/arsmachina/tapestrycrud/components/css/main.css",
	Constants.TAPESTRY_CRUD_CSS_ASSET})
public class GenericLayout {

	@Parameter(allowNull = false, defaultPrefix = BindingConstants.MESSAGE)
	@Property
	@SuppressWarnings("unused")
	private String title;
	
	@Inject
	private PageUtil pageUtil;
	
	@Inject
	private ComponentResources resources;
	
	@Inject
	private Messages messages;
	
	/**
	 * Provides a default value for the title parameter.
	 */
	String defaultTitle() {
		return pageUtil.getRequestedPageTitle(messages);
	}
	
	/**
	 * Returns the CSS class that will be put at the <code>html</code> tag.
	 * 
	 * @return a {@link String}.
	 */
	protected String getCssClass() {
		return resources.getPageName().toLowerCase().replace('/', '-');
	}

}
