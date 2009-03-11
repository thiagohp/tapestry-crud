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
import br.com.arsmachina.tapestrycrud.base.BaseEditPage;
import br.com.arsmachina.tapestrycrud.base.BaseViewPage;
import br.com.arsmachina.tapestrycrud.encoder.ActivationContextEncoder;
import br.com.arsmachina.tapestrycrud.services.ActivationContextEncoderSource;
import br.com.arsmachina.tapestrycrud.services.TapestryCrudModuleService;

/**
 * <p>
 * Component that creates a link to the corresponding edition page (a {@link BaseEditPage} instance,
 * typically). It must be used inside pages that subclass {@link BaseViewPage}.
 * </p>
 * <p>
 * The code of this class is largely adapted from Tapestry's {@link Label}.
 * </p>
 * 
 * @author Thiago H. de Paula Figueiredo
 */
@SupportsInformalParameters
public class EditObjectPageLink {

	private static final String EDIT_OBJECT_MESSAGE = "link.edit.object";
	
	@Inject
	private ComponentResources resources;
	
	@Inject
	private TapestryCrudModuleService tapestryCrudModuleService;
	
	@Inject
	private ActivationContextEncoderSource activationContextEncoderSource;
	
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

	private String editPageURL;

	@SuppressWarnings("unchecked")
	boolean beginRender(MarkupWriter writer) {

		Component page = resources.getPage();

		if (page instanceof BaseViewPage == false) {

			throw new RuntimeException("The EditObjectPageLink must be used inside a page "
					+ "that extends BasePage");

		}

		BaseViewPage viewPage = (BaseViewPage) page;
		Object object = viewPage.getObject();
		
		if (authorizer.canUpdate(object.getClass()) == false || authorizer.canUpdate(object) == false) {
			return false;
		}
		
		entityClass = viewPage.getEntityClass();
		editPageURL = tapestryCrudModuleService.getEditPageURL(entityClass);
		ActivationContextEncoder encoder = activationContextEncoderSource.get(entityClass);
		final Object activationContext = encoder.toActivationContext(object);
		
		Link link = resources.createPageLink(editPageURL, true, activationContext);

		element = writer.element("a", "href", link.toURI(), "class", "t-crud-edit-object");

		resources.renderInformalParameters(writer);

		return !ignoreBody;

	}

	void afterRender(MarkupWriter writer) {

		if (element != null) {
		
			boolean bodyIsBlank = InternalUtils.isBlank(element.getChildMarkup());
	
			String label;
	
			if (bodyIsBlank || ignoreBody) {
	
				final Messages messages = resources.getMessages();
	
				final String key = EDIT_OBJECT_MESSAGE + "."
						+ tapestryCrudModuleService.getEditPageURL(entityClass).replace('/', '.');
	
				if (messages.contains(key)) {
					label = messages.get(key);
				}
				else {
					label = messages.get(EDIT_OBJECT_MESSAGE);
				}
	
				writer.write(label);
	
			}
	
			writer.end(); // a
			
		}

	}

}
