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
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.corelib.base.AbstractConditional;
import org.apache.tapestry5.ioc.annotations.Inject;

import br.com.arsmachina.authentication.service.UserService;

/**
 * Component tha render its tag (if such) and body only if the current user has a given permission.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
@SupportsInformalParameters
public class HasPermission extends AbstractConditional {
	
	@Parameter(required = true, allowNull = false, defaultPrefix = BindingConstants.LITERAL)
	private String permission;
	
	@Inject
	private UserService userService;

	@Override
	protected boolean test() {
		return userService.hasPermission(permission);
	}

}
