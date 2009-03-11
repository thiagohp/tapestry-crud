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

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.corelib.components.Label;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.runtime.Component;

import br.com.arsmachina.authorization.Authorizer;
import br.com.arsmachina.tapestrycrud.EditPage;
import br.com.arsmachina.tapestrycrud.base.BaseEditPage;
import br.com.arsmachina.tapestrycrud.base.BaseListPage;
import br.com.arsmachina.tapestrycrud.base.BasePage;
import br.com.arsmachina.tapestrycrud.services.TapestryCrudModuleService;

/**
 * <p>
 * Component that creates a link to the corresponding listing page (a {@link BaseListPage} instance,
 * typically). It must be used inside pages that implement {@link EditPage} or subclass
 * {@link BaseEditPage}.
 * </p>
 * <p>
 * The code of this class is largely adapted from Tapestry's {@link Label}.
 * </p>
 * 
 * @author Thiago H. de Paula Figueiredo
 */
@SupportsInformalParameters
public class ListPageLink {

	private static final String BACK_TO_LISTING_MESSAGE = "link.list.class";

	@Inject
	private ComponentResources resources;

	@Inject
	private TapestryCrudModuleService tapestryCrudModuleService;
	
	@Inject
	private Authorizer authorizer;

	private Class<?> entityClass;
	
	/**
	 * If false (default value), the body of the tag will be ignored and the intertionalized name of
	 * thelisting page is used. If true, then the body of the label element (in the template) is not
	 * ignored.
	 */
	@Parameter(value = "true")
	private boolean ignoreBody;

	private Element element;

	@SuppressWarnings("unchecked")
	boolean beginRender(MarkupWriter writer) {
		
		Component page = resources.getPage();

		if (page instanceof BasePage == false) {

			throw new RuntimeException("The crud/ListPageLink must be used inside a page "
					+ "that extends BasePage");

		}

		BasePage basePage = (BasePage) page;
		entityClass = basePage.getEntityClass();
		
		if (authorizer.canSearch(entityClass) == false) {
			return false;
		}
		
		String listPageURL = tapestryCrudModuleService.getListPageURL(entityClass);
		Link link = resources.createPageLink(listPageURL, true);

		element = writer.element("a", "href", link.toURI(), "class", "t-crud-listing-link");

		resources.renderInformalParameters(writer);

		return !ignoreBody;

	}

	void afterRender(MarkupWriter writer) {

		if (element != null) {
		
			boolean bodyIsBlank = InternalUtils.isBlank(element.getChildMarkup());
	
			String label;
	
			if (bodyIsBlank || ignoreBody) {
	
				final Messages messages = resources.getMessages();
	
				final String key = BACK_TO_LISTING_MESSAGE + "."
						+ tapestryCrudModuleService.getListPageURL(entityClass).replace('/', '.');
	
				if (messages.contains(key)) {
					label = messages.get(key);
				}
				else {
					label = messages.get(BACK_TO_LISTING_MESSAGE);
				}
	
				writer.write(label);
	
			}
	
			writer.end(); // a
			
		}

	}

}
