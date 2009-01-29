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
import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import br.com.arsmachina.tapestrycrud.services.RequestUtil;

/**
 * A generic layout component. This class is meant to be used as a superclass of layout components,
 * not by itself.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
@IncludeStylesheet("classpath:/br/com/arsmachina/tapestrycrud/components/css/main.css")
public class GenericLayout {

	final private static String PAGE_TITLE_MESSAGE_PREFIX = "page.title.";

	@Parameter(allowNull = false, defaultPrefix = BindingConstants.MESSAGE)
	@Property
	@SuppressWarnings("unused")
	private String title;
	
	/**
	 * A {@link Block} used to render the navigation div.
     */
    @SuppressWarnings("unused")
	@Parameter(value="block:notSet", allowNull = false, defaultPrefix = BindingConstants.BLOCK)
    @Property
    private Block navigation;

	@Inject
	private RequestUtil requestUtil;
	
	@Inject
	private Messages messages;
	
	/**
	 * Provides a default value for the title parameter.
	 */
	String defaultTitle() {
		
		final String string = requestUtil.getRequestedPageURL().replace('/', '.');
		return messages.get(PAGE_TITLE_MESSAGE_PREFIX + string);
		
	}

}
