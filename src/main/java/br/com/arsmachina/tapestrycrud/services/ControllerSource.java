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

package br.com.arsmachina.tapestrycrud.services;

import java.io.Serializable;

import br.com.arsmachina.controller.Controller;


/**
 * Service that mathes entity classes to their controller classes ({@link Controller} instances).
 * 
 * @author Thiago H. de Paula Figueiredo
 * @see Controller
 */
public interface ControllerSource {

	/**
	 * Returns the {@link Controller} of a given type.
	 * 
	 * @param <T> a type.
	 * @param <K> the type's primary key field type.
	 * @param clasz a {@link Class}.
	 * @return a {@link GenericController}.
	 */
	<T, K extends Serializable> Controller<T, K> get(Class<T> clasz);

}
