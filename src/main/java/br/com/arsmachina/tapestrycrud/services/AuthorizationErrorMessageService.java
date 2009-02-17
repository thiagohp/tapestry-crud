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

package br.com.arsmachina.tapestrycrud.services;

import org.apache.tapestry5.ioc.Messages;

import br.com.arsmachina.authorization.AuthorizationException;

/**
 * Service that provides methods related to authorization error messages.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public interface AuthorizationErrorMessageService {
	
	/**
	 * Returns the error message for a given authorization exception.
	 * 
	 * @param exception an {@link AuthorizationException}. It cannot be null.
	 * @param messages a {@link Messages} instance. It cannot be null.
	 * @return a {@link String}.
	 */
	String getErrorMessage(AuthorizationException exception, Messages messages);

}
