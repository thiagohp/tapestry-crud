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

import org.apache.tapestry5.PrimaryKeyEncoder;
import org.apache.tapestry5.ioc.services.ClassNameLocator;

import br.com.arsmachina.module.AbstractModule;
import br.com.arsmachina.tapestrycrud.encoder.ActivationContextEncoder;
import br.com.arsmachina.tapestrycrud.encoder.Encoder;
import br.com.arsmachina.tapestrycrud.encoder.LabelEncoder;

/**
 * Default {@link TapestryCrudModule} implementation.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public class DefaultTapestryCrudModule extends AbstractModule implements TapestryCrudModule {

	/**
	 * Single constructor of this class.
	 * 
	 * @param name a {@link String} containing the module name. It cannot be null.
	 * @param rootPackage a {@link String} containing the module parent package. It cannot be null.
	 * @param classNameLocator a {@link ClassNameLocator}. It cannot be null.
	 * @param daoImplementationSubpackage a {@link String}. It cannot be null.
	 */
	public DefaultTapestryCrudModule(String name, String rootPackage,
			ClassNameLocator classNameLocator, String daoImplementationSubpackage) {
		
		super(name, rootPackage, classNameLocator);
		
	}

	@SuppressWarnings("unchecked")
	public <T> Class<? extends ActivationContextEncoder<T>> getActivationContextEncoderClass(
			Class<T> entityClass) {

		return getClass(getActivationContextEncoderClassName(entityClass));

	}

	@SuppressWarnings("unchecked")
	public <T> Class<? extends Encoder<T, ?>> getEncoderClass(Class<T> entityClass) {

		return getClass(getEncoderClassName(entityClass));

	}

	@SuppressWarnings("unchecked")
	public <T> Class<? extends LabelEncoder<T>> getLabelEncoderClass(Class<T> entityClass) {
		return getClass(getLabelEncoderClassName(entityClass));
	}

	@SuppressWarnings("unchecked")
	public <T> Class<? extends PrimaryKeyEncoder<?, T>> getPrimaryKeyEncoderClass(
			Class<T> entityClass) {
		return getClass(getPrimaryKeyEncoderClassName(entityClass));
	}

	/**
	 * Returns the fully-qualified name of the activation context encoder for a given entity class.
	 * 
	 * @param clasz a {@link Class}. It cannot be null.
	 * @return a {@link String} or null (if no corresponding one is found).
	 */
	protected String getActivationContextEncoderClassName(Class<?> entityClass) {

		return String.format("%s.web.encoder.activationcontext.%sActivationContextEncoder",
				getRootPackage(), entityClass.getSimpleName());

	}

	/**
	 * Returns the fully-qualified name of the encoder for a given entity class.
	 * 
	 * @param clasz a {@link Class}. It cannot be null.
	 * @return a {@link String} or null (if no corresponding one is found).
	 */
	protected String getEncoderClassName(Class<?> entityClass) {

		return String.format("%s.web.encoder.%sEncoder", getRootPackage(), entityClass.getSimpleName());

	}

	/**
	 * Returns the fully-qualified name of the label encoder for a given entity class.
	 * 
	 * @param clasz a {@link Class}. It cannot be null.
	 * @return a {@link String} or null (if no corresponding one is found).
	 */
	protected String getLabelEncoderClassName(Class<?> entityClass) {

		return String.format("%s.web.encoder.label.%sLabelEncoder", getRootPackage(),
				entityClass.getSimpleName());

	}

	/**
	 * Returns the fully-qualified name of the primary key encoder for a given entity class.
	 * 
	 * @param clasz a {@link Class}. It cannot be null.
	 * @return a {@link String} or null (if no corresponding one is found).
	 */
	protected String getPrimaryKeyEncoderClassName(Class<?> entityClass) {

		return String.format("%s.web.encoder.primarykey.%sPrimaryKeyEncoder", getRootPackage(),
				entityClass.getSimpleName());

	}

	@Override
	public String toString() {
		return String.format("TapestryCrudModule %s (%s)", getName(), getRootPackage());
	}

}
