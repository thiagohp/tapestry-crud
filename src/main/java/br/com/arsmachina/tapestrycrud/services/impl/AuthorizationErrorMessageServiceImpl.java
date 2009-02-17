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

package br.com.arsmachina.tapestrycrud.services.impl;

import org.apache.tapestry5.ioc.Messages;

import br.com.arsmachina.authorization.AuthorizationException;
import br.com.arsmachina.authorization.ObjectAuthorizationException;
import br.com.arsmachina.authorization.ReadObjectAuthorizationException;
import br.com.arsmachina.authorization.ReadTypeAuthorizationException;
import br.com.arsmachina.authorization.RemoveObjectAuthorizationException;
import br.com.arsmachina.authorization.RemoveTypeAuthorizationException;
import br.com.arsmachina.authorization.SearchTypeAuthorizationException;
import br.com.arsmachina.authorization.StoreTypeAuthorizationException;
import br.com.arsmachina.authorization.TypeAuthorizationException;
import br.com.arsmachina.authorization.UpdateObjectAuthorizationException;
import br.com.arsmachina.authorization.UpdateTypeAuthorizationException;
import br.com.arsmachina.tapestrycrud.services.AuthorizationErrorMessageService;

/**
 * Default {@link AuthorizationErrorMessageService} implementation.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public class AuthorizationErrorMessageServiceImpl implements AuthorizationErrorMessageService {

	final private static String MESSAGE_PREFIX = "message.authorization";
	
	final private static String DEFAULT_MESSAGE = "message.authorization.error";

	public String getErrorMessage(AuthorizationException exception, Messages messages) {

		String typeOrObject = exception instanceof TypeAuthorizationException ? "type" : "object";
		Class<?> type = null;
		
		if (exception instanceof TypeAuthorizationException) {
			TypeAuthorizationException e = (TypeAuthorizationException) exception;
			type = e.getType();
		}
		else if (exception instanceof ObjectAuthorizationException) {
			
			ObjectAuthorizationException e = (ObjectAuthorizationException) exception;
			type = e.getObject().getClass();
			
		}
		else {
			throw new IllegalArgumentException("Unknown exception: " + exception.getClass().getName());
		}
		
		String operation;
		
		if (exception instanceof ReadTypeAuthorizationException || exception instanceof ReadObjectAuthorizationException) {
			operation = "read";
		}
		else if (exception instanceof StoreTypeAuthorizationException) {
			operation = "store";
		}
		else if (exception instanceof SearchTypeAuthorizationException) {
			operation = "search";
		}
		else if (exception instanceof RemoveTypeAuthorizationException || exception instanceof RemoveObjectAuthorizationException) {
			operation = "remove";
		}
		else if (exception instanceof UpdateTypeAuthorizationException || exception instanceof UpdateObjectAuthorizationException) {
			operation = "remove";
		}
		else {
			throw new IllegalArgumentException("Unknown exception: " + exception.getClass().getName());
		}
		
		
		String className = type.getSimpleName().toLowerCase();
		String key = String.format("%s.%s.%s.%s", MESSAGE_PREFIX, typeOrObject, className, operation);
		String message;
		
		if (messages.contains(key)) {
			message = messages.get(key);
		}
		else {
			message = messages.get(DEFAULT_MESSAGE);
		}
		
		return message;
		
	}
}
