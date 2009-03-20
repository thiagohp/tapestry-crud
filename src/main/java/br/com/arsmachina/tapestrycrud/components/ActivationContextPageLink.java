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
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.corelib.base.AbstractLink;
import org.apache.tapestry5.ioc.annotations.Inject;

import br.com.arsmachina.tapestrycrud.encoder.ActivationContextEncoder;
import br.com.arsmachina.tapestrycrud.encoder.Encoder;
import br.com.arsmachina.tapestrycrud.services.ActivationContextEncoderSource;

/**
 * A replacement to {@link org.apache.tapestry5.corelib.components.PageLink}
 * that, given an object passed as parameter, uses the corresponding
 * {@link Encoder} to get the context {@link Encoder#toKey(Object) } activation
 * value.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
@SupportsInformalParameters
public class ActivationContextPageLink extends AbstractLink {

	/**
	 * The logical name of the page to link to.
	 */
	@Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
	@Property
	private String page;

	/**
	 * The object from which the activation context value will be extracted.
	 */
	@Parameter(required = true, allowNull = false)
	private Object object;

	@Inject
	private ActivationContextEncoderSource activationContextEncoderSource;
	
	@Inject
	private ComponentResources resources;

	@SuppressWarnings("unchecked")
	public Object getContext() {

		ActivationContextEncoder encoder;
		encoder = activationContextEncoderSource.get(object.getClass());
		return encoder.toActivationContext(object);

	}

	void beginRender(MarkupWriter writer) {
		
		if (isDisabled())
			return;

		Link link =
			resources.createPageLink(page, true, getContext());

		writeLink(writer, link);
		
	}

	void afterRender(MarkupWriter writer) {
		if (isDisabled())
			return;

		writer.end(); // <a>
	}

}
