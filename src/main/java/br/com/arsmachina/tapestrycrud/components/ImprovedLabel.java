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
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Field;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.ValidationDecorator;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.annotations.Mixin;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.corelib.components.Label;
import org.apache.tapestry5.corelib.mixins.DiscardBody;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Heartbeat;

import br.com.arsmachina.tapestrycrud.Constants;

/**
 * <p>
 * {@link Label} subclass that always ignores its body and generates the label name from the
 * corresponding field id.
 * </p>
 * <p>
 * This class' code was initially copied from {@link Label}, as one of its methods (<code>afterRender</code>)
 * has package visibility and thus cannot be overriden.
 * </p>
 * 
 * @author Thiago H. de Paula Figueiredo
 */
@SupportsInformalParameters
@IncludeStylesheet(Constants.TAPESTRY_CRUD_CSS_ASSET)
public class ImprovedLabel {

	/**
	 * Discards the body of this class.
	 */
	@SuppressWarnings("unused")
	@Mixin
	private DiscardBody discardBody;

	/**
	 * The for parameter is used to identify the {@link Field} linked to this label (it is named
	 * this way because it results in the for attribute of the label element).
	 */
	@Parameter(name = "for", required = true, defaultPrefix = BindingConstants.COMPONENT)
	private Field field;

	@Environmental
	private Heartbeat heartbeat;

	@Environmental
	private ValidationDecorator decorator;

	@Inject
	private ComponentResources resources;

	private Element labelElement;

	@BeginRender
	void begin(MarkupWriter writer) {
		final Field field = this.field;

		decorator.beforeLabel(field);

		labelElement = writer.element("label");

		resources.renderInformalParameters(writer);

		// Since we don't know if the field has rendered yet, we need to defer writing the for and
		// id
		// attributes until we know the field has rendered (and set its clientId property). That's
		// exactly what Heartbeat is for.

		Runnable command = new Runnable() {

			public void run() {
				String fieldId = field.getClientId();

				labelElement.forceAttributes("for", fieldId, "id", fieldId + ":label");

				decorator.insideLabel(field, labelElement);
			}
		};

		heartbeat.defer(command);
	}

	@AfterRender
	void after(MarkupWriter writer) {
		// If the Label element has a body that renders some non-blank output, that takes
		// precendence
		// over the label string provided by the field.

		// removed code from label. the rest was not modified.

		// boolean bodyIsBlank = InternalUtils.isBlank(labelElement.getChildMarkup());
		//
		// if (bodyIsBlank)
		// writer.write(field.getLabel());

		writer.write(field.getLabel());

		writer.end(); // label

		decorator.afterLabel(field);

	}
}
