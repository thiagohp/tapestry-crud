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

package br.com.arsmachina.tapestrycrud.module;

import java.util.Set;

import org.apache.tapestry5.PrimaryKeyEncoder;

import br.com.arsmachina.controller.Controller;
import br.com.arsmachina.tapestrycrud.encoder.ActivationContextEncoder;
import br.com.arsmachina.tapestrycrud.encoder.Encoder;
import br.com.arsmachina.tapestrycrud.encoder.LabelEncoder;

/**
 * Interface that defines information about a module, whatever its conventions are.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public interface Module {

	/**
	 * Returns a {@link Set} containing all the entity classes in this module.
	 * 
	 * @return a {@link Set} of {@link Class} instances.
	 */
	public Set<Class<?>> getEntityClasses();

	/**
	 * Returns the controller class implementation corresponding to a given entity class.
	 * 
	 * @param <T> the entity type.
	 * @param entityClass a {@link Class} instance. It cannot be null.
	 * @return a {@link Controller} or null (if no corresponding one is found).
	 */
	public <T> Class<? extends Controller<T, ?>> getControllerImplementationClass(Class<T> entityClass);

	/**
	 * Returns the controller definition (interface) corresponding to a given entity class.
	 * 
	 * @param <T> the entity type.
	 * @param entityClass a {@link Class} instance. It cannot be null.
	 * @return a {@link Controller} or null (if no corresponding one is found).
	 */
	public <T> Class<? extends Controller<T, ?>> getControllerDefinitionClass(Class<T> entityClass);

	/**
	 * Returns the activation context encoder class corresponding to a given entity class.
	 * 
	 * @param <T> the entity type.
	 * @param entityClass a {@link Class} instance. It cannot be null.
	 * @return an {@link ActivationContextEncoder} or null (if no corresponding one is found).
	 */
	public <T> Class<? extends ActivationContextEncoder<T>> getActivationContextEncoderClass(
			Class<T> entityClass);

	/**
	 * Returns the encoder class corresponding to a given entity class.
	 * 
	 * @param <T> the entity type.
	 * @param entityClass a {@link Class} instance. It cannot be null.
	 * @return an {@link Encoder} or null (if no corresponding one is found).
	 */
	public <T> Class<? extends Encoder<T, ?>> getEncoderClass(
			Class<T> entityClass);

	/**
	 * Returns the label encoder class corresponding to a given entity class.
	 * 
	 * @param <T> the entity type.
	 * @param entityClass a {@link Class} instance. It cannot be null.
	 * @return a {@link LabelEncoder} or null (if no corresponding one is found).
	 */
	public <T> Class<? extends LabelEncoder<T>> getLabelEncoderClass(
			Class<T> entityClass);

	/**
	 * Returns the label encoder class corresponding to a given entity class.
	 * 
	 * @param <T> the entity type.
	 * @param entityClass a {@link Class} instance. It cannot be null.
	 * @return a {@link PrimaryKeyEncoder} or null (if no corresponding one is found).
	 */
	public <T> Class<? extends PrimaryKeyEncoder<?, T>> getPrimaryKeyEncoderClass(
			Class<T> entityClass);

	/**
	 * Returns the module name. Just used for logging and debugging purposes.
	 * 
	 * @return a {@link String}.
	 */
	public String getName();

}
