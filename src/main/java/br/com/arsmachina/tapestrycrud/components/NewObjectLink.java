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

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Mixin;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.corelib.mixins.DiscardBody;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import br.com.arsmachina.tapestrycrud.EditPage;
import br.com.arsmachina.tapestrycrud.services.PageUtil;

/**
 * A link to edit a new object. Created because Firefox (and maybe other browsers) sometimes keeps
 * the field values from one page, editing one object, to a new object page. One example can be
 * found in <a href="http://ars-machina.svn.sourceforge.net/viewvc/ars-machina/example/trunk/src/main/webapp/project/EditProject.tml?view=markup"
 * >Ars Machina Project Example</a>.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
@SupportsInformalParameters
public class NewObjectLink {

	private static final String LOWERCASE_CLASSNAME_MESSAGE = "lowercase.classname";

	private static final String NEW_OBJECT_MESSAGE = "link.newobject";

	private static final String NEW_OBJECT_TEMPLATE_MESSAGE = "link.newobject.template";

	@InjectContainer
	private Object page;

	@Mixin
	@SuppressWarnings("unused")
	private DiscardBody discardBody;

	@Inject
	private Messages messages;

	@Inject
	private PageUtil requestUtil;

	@Parameter(allowNull = false, defaultPrefix = BindingConstants.MESSAGE)
	@Property(write = false)
	@SuppressWarnings("unused")
	private String label;

	private Boolean lowercaseClassName = null;

	/**
	 * Single constructor of this class.
	 */
	public NewObjectLink() {

		if (lowercaseClassName == null) {

			if (messages.contains(LOWERCASE_CLASSNAME_MESSAGE)) {
				lowercaseClassName = Boolean.valueOf(messages.get(LOWERCASE_CLASSNAME_MESSAGE));
			}
			else {
				lowercaseClassName = Boolean.TRUE;
			}

		}

	}

	@SuppressWarnings("unchecked")
	String defaultLabel() {

		String returnedLabel = null;

		String pageName = requestUtil.getRequestedPageURL();

		pageName = pageName.replace('/', '.');

		final String pageSpecificLabel = NEW_OBJECT_MESSAGE + "." + pageName;

		if (messages.contains(pageSpecificLabel)) {
			returnedLabel = messages.get(pageSpecificLabel);
		}

		if (returnedLabel == null && page instanceof EditPage) {

			EditPage editPage = (EditPage) page;
			final Class entityClass = editPage.getEntityClass();
			final String entityLabelMessage = entityClass.getSimpleName() + "-label";

			if (messages.contains(entityLabelMessage)
					&& messages.contains(NEW_OBJECT_TEMPLATE_MESSAGE)) {

				String format = messages.get(NEW_OBJECT_TEMPLATE_MESSAGE);

				String className = messages.get(entityLabelMessage);

				if (lowercaseClassName != null && lowercaseClassName) {
					className = className.toLowerCase();
				}

				returnedLabel = String.format(format, className);

			}

		}
		
		if (returnedLabel == null) {
			returnedLabel = messages.get(NEW_OBJECT_MESSAGE);
		}

		return returnedLabel;

	}
	
}
