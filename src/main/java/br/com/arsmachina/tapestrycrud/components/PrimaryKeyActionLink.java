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
import org.apache.tapestry5.PrimaryKeyEncoder;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.ioc.annotations.Inject;

import br.com.arsmachina.tapestrycrud.encoder.ActivationContextEncoder;
import br.com.arsmachina.tapestrycrud.encoder.Encoder;
import br.com.arsmachina.tapestrycrud.services.PrimaryKeyEncoderSource;

/**
 * <p>
 * An alternative to {@link org.apache.tapestry5.corelib.components.ActionLink} that, given an
 * object passed as parameter, uses the corresponding {@link Encoder} to get the context
 * {@link Encoder#toKey(Object) } activation value.
 * </p>
 * 
 * @author Thiago H. de Paula Figueiredo
 * @see org.apache.tapestry5.corelib.components.ActionLink
 * @see ActivationContextEncoder
 */
@SupportsInformalParameters
public class PrimaryKeyActionLink {

	/**
	 * The object that will be used to generate the context for the link.
	 */
	@Parameter
	private Object object;
	
    /**
     * Binding the zone parameter turns the link into a an Ajax control that causes the related zone to be updated.
     */
    @SuppressWarnings("unused")
	@Parameter(defaultPrefix = BindingConstants.LITERAL)
    @Property
    private String zone;
	
	@Inject
	private PrimaryKeyEncoderSource primaryKeyEncoderSource;
	
	@SuppressWarnings("unchecked")
	public Object getContext() {

		PrimaryKeyEncoder encoder = primaryKeyEncoderSource.get(object.getClass());
		return encoder.toKey(object);

	}

}
