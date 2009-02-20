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
import org.apache.tapestry5.PrimaryKeyEncoder;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.corelib.components.Label;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.runtime.Component;

import br.com.arsmachina.authorization.Authorizer;
import br.com.arsmachina.tapestrycrud.Constants;
import br.com.arsmachina.tapestrycrud.base.BaseEditPage;
import br.com.arsmachina.tapestrycrud.base.BaseViewPage;
import br.com.arsmachina.tapestrycrud.services.PrimaryKeyEncoderSource;

/**
 * <p>
 * Component that creates a link that removes the object being show or edited. It must be used
 * inside pages that subclass {@link BaseViewPage} or {@link BaseEditPage}.
 * </p>
 * <p>
 * The code of this class is largely adapted from Tapestry's {@link Label}.
 * </p>
 * 
 * @author Thiago H. de Paula Figueiredo
 */
@SupportsInformalParameters
public class RemoveObjectEventLink {

	private static final String REMOVE_OBJECT_MESSAGE = "link.remove.object";

	@Inject
	private ComponentResources resources;

	@Inject
	private PrimaryKeyEncoderSource primaryKeyEncoderSource;

	private Class<?> entityClass;

	/**
	 * If false (default value), the body of the tag will be ignored and the intertionalized name of
	 * thelisting page is used. If true, then the body of the label element (in the template) is not
	 * ignored.
	 */
	@Parameter(value = "true")
	private boolean ignoreBody;
	
	@Inject
	private Authorizer authorizer;

	private Element element;

	@SuppressWarnings("unchecked")
	boolean beginRender(MarkupWriter writer) {

		Object object;
		Component page = resources.getPage();

		if (page instanceof BaseViewPage) {
			BaseViewPage viewPage = (BaseViewPage) page;
			object = viewPage.getObject();
			entityClass = viewPage.getEntityClass();
		}
		else if (page instanceof BaseEditPage) {
			BaseEditPage editPage = (BaseEditPage) page;
			object = editPage.getObject();
			entityClass = editPage.getEntityClass();
		}
		else {
			 throw new RuntimeException("The RemoveObjectEventLink must be used inside a page "
					+ "that extends BaseViewPage or BaseEditPage");
		}

		if (authorizer.canRemove(entityClass) == false || authorizer.canRemove(object) == false) {
			return false;
		}

		final PrimaryKeyEncoder encoder = primaryKeyEncoderSource.get(object.getClass());
		final Object context = encoder.toKey(object);

		Link link = resources.createEventLink(Constants.REMOVE_OBJECT_ACTION, context);

		element = writer.element("a", "href", link.toURI(), "class", "t-crud-remove-object");

		resources.renderInformalParameters(writer);

		return !ignoreBody;

	}

	void afterRender(MarkupWriter writer) {
		
		if (element != null) {

			boolean bodyIsBlank = InternalUtils.isBlank(element.getChildMarkup());
	
			String label;
	
			if (bodyIsBlank || ignoreBody) {
	
				final Messages messages = resources.getMessages();
	
				final String key = REMOVE_OBJECT_MESSAGE + "." + entityClass.getSimpleName();
	
				if (messages.contains(key)) {
					label = messages.get(key);
				}
				else {
					label = messages.get(REMOVE_OBJECT_MESSAGE);
				}
	
				writer.write(label);
	
			}
	
			writer.end(); // a
			
		}

	}

}
