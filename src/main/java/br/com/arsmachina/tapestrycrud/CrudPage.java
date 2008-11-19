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

package br.com.arsmachina.tapestrycrud;

import java.io.Serializable;

/**
 * Interface that defines some common methods for CRUD pages, being it listing or editing.
 * 
 * @param <T> the entity class related to this encoder.
 * @param <K> the type of the class' primary key property.
 * @author Thiago H. de Paula Figueiredo
 */
public interface CrudPage<T, K extends Serializable> {

	/**
	 * Returns the value of the <code>message</code> property.
	 * 
	 * @return a {@link String}.
	 */
	String getMessage();

	/**
	 * Changes the value of the <code>message</code> property.
	 * 
	 * @param message a {@link String}.
	 */
	void setMessage(String message);

	/**
	 * Returns the entity type.
	 * 
	 * @return a {@link Class<T>}.
	 */
	Class<T> getEntityClass();

	/**
	 * Returns the type of the entity's primary key field.
	 * 
	 * @return a {@link Class<?>}.
	 */
	Class<?> getPrimaryKeyClass();

}
