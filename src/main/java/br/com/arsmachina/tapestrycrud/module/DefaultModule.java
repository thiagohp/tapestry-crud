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

import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.tapestry5.PrimaryKeyEncoder;
import org.apache.tapestry5.ioc.services.ClassNameLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.arsmachina.controller.Controller;
import br.com.arsmachina.dao.DAO;
import br.com.arsmachina.tapestrycrud.encoder.ActivationContextEncoder;
import br.com.arsmachina.tapestrycrud.encoder.Encoder;
import br.com.arsmachina.tapestrycrud.encoder.LabelEncoder;

/**
 * Default {@link Module} implementation.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public class DefaultModule implements Module {

	final private String name;

	final private String rootPackage;

	final private static Logger LOGGER = LoggerFactory.getLogger(DefaultModule.class);

	final private ClassNameLocator classNameLocator;

	final private Set<Class<?>> entityClasses = new HashSet<Class<?>>();

	/**
	 * Single constructor of this class.
	 * 
	 * @param name a {@link String} containing the module name. It cannot be null.
	 * @param rootPackage a {@link String} containing the module parent package. It cannot be
	 * null.
	 * @param classNameLocator a {@link ClassNameLocator}. It cannot be null.
	 */
	public DefaultModule(String name, String rootPackage, ClassNameLocator classNameLocator) {

		if (name == null) {
			throw new IllegalArgumentException("Parameter name cannot be null");
		}

		if (rootPackage == null) {
			throw new IllegalArgumentException("Parameter rootPackage cannot be null");
		}

		if (classNameLocator == null) {
			throw new IllegalArgumentException("Parameter classNameLocator cannot be null");
		}

		this.name = name;
		this.rootPackage = rootPackage;
		this.classNameLocator = classNameLocator;

		fillEntityClasses();

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
	public <T> Class<? extends Controller<T, ?>> getControllerImplementationClass(
			Class<T> entityClass) {
		return getClass(getControllerImplementationClassName(entityClass));
	}

	@SuppressWarnings("unchecked")
	public <T> Class<? extends Controller<T, ?>> getControllerDefinitionClass(Class<T> entityClass) {
		return getClass(getControllerDefinitionClassName(entityClass));
	}

	@SuppressWarnings("unchecked")
	public <T> Class<? extends DAO<T, ?>> getDAOImplementationClass(
			Class<T> entityClass) {
		return getClass(getDAOImplementationClassName(entityClass));
	}

	@SuppressWarnings("unchecked")
	public <T> Class<? extends DAO<T, ?>> getDAODefinitionClass(Class<T> entityClass) {
		return getClass(getDAODefinitionClassName(entityClass));
	}

	public Set<Class<?>> getEntityClasses() {
		return entityClasses;
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

	public String getName() {
		return name;
	}

	/**
	 * Returns the package that contains the entity classes.
	 * 
	 * @return a {@link String}.
	 */
	protected String getEntityPackage() {
		return rootPackage + ".entity";
	}

	/**
	 * Returns the fully-qualified name of the controller class implementation for a given entity
	 * class.
	 * 
	 * @param clasz a {@link Class}. It cannot be null.
	 * @return a {@link String}.
	 */
	protected String getControllerImplementationClassName(Class<?> entityClass) {

		return String.format("%s.controller.impl.%sControllerImpl", rootPackage,
				entityClass.getSimpleName());

	}

	/**
	 * Returns the fully-qualified name of the controller definition (interface) for a given entity
	 * class.
	 * 
	 * @param clasz a {@link Class}. It cannot be null.
	 * @return a {@link String}.
	 */
	protected String getControllerDefinitionClassName(Class<?> entityClass) {

		return String.format("%s.controller.%sController", rootPackage,
				entityClass.getSimpleName());

	}

	/**
	 * Returns the fully-qualified name of the DAO class implementation for a given entity
	 * class.
	 * 
	 * @param clasz a {@link Class}. It cannot be null.
	 * @return a {@link String}.
	 */
	protected String getDAOImplementationClassName(Class<?> entityClass) {

		return String.format("%s.dao.impl.%sDAOImpl", rootPackage,
				entityClass.getSimpleName());

	}

	/**
	 * Returns the fully-qualified name of the DAO definition (interface) for a given entity
	 * class.
	 * 
	 * @param clasz a {@link Class}. It cannot be null.
	 * @return a {@link String}.
	 */
	protected String getDAODefinitionClassName(Class<?> entityClass) {

		return String.format("%s.dao.%sDAO", rootPackage,
				entityClass.getSimpleName());

	}

	/**
	 * Returns the fully-qualified name of the activation context encoder for a given entity class.
	 * 
	 * @param clasz a {@link Class}. It cannot be null.
	 * @return a {@link String} or null (if no corresponding one is found).
	 */
	protected String getActivationContextEncoderClassName(Class<?> entityClass) {

		return String.format("%s.web.encoder.activationcontext.%sActivationContextEncoder",
				rootPackage, entityClass.getSimpleName());

	}

	/**
	 * Returns the fully-qualified name of the encoder for a given entity class.
	 * 
	 * @param clasz a {@link Class}. It cannot be null.
	 * @return a {@link String} or null (if no corresponding one is found).
	 */
	protected String getEncoderClassName(Class<?> entityClass) {

		return String.format("%s.web.encoder.%sEncoder", rootPackage,
				entityClass.getSimpleName());

	}

	/**
	 * Returns the fully-qualified name of the label encoder for a given entity class.
	 * 
	 * @param clasz a {@link Class}. It cannot be null.
	 * @return a {@link String} or null (if no corresponding one is found).
	 */
	protected String getLabelEncoderClassName(Class<?> entityClass) {

		return String.format("%s.web.encoder.label.%sLabelEncoder", rootPackage,
				entityClass.getSimpleName());

	}

	/**
	 * Returns the fully-qualified name of the primary key encoder for a given entity class.
	 * 
	 * @param clasz a {@link Class}. It cannot be null.
	 * @return a {@link String} or null (if no corresponding one is found).
	 */
	protected String getPrimaryKeyEncoderClassName(Class<?> entityClass) {

		return String.format("%s.web.encoder.primarykey.%sPrimaryKeyEncoder", rootPackage,
				entityClass.getSimpleName());

	}

	/**
	 * Returns <code>getClass(name, false)</code>.
	 * 
	 * @param name a {@link String}. It cannot be null.
	 * @return a {@link Class}.
	 */
	@SuppressWarnings("unchecked")
	protected Class getClass(String name) {
		return getClass(name, true);
	}

	/**
	 * Returns the {@link Class} instance given a class name.
	 * 
	 * @param name a {@link String}. It cannot be null.
	 * @param lenient a <code>boolean</code>. If <code>false</code>, it will throw an
	 * exception if the class is not found. Otherwise, this method will return null.
	 * @return a {@link Class} or null.
	 */
	@SuppressWarnings("unchecked")
	protected Class getClass(String name, boolean lenient) {

		Class<?> clasz = null;

		try {
			clasz = Thread.currentThread().getContextClassLoader().loadClass(name);
		}
		catch (ClassNotFoundException e) {

			if (lenient == false) {
				throw new RuntimeException(e);
			}
			else {

				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("Class not found: " + name);
				}

			}

		}

		return clasz;

	}

	final private void fillEntityClasses() {

		String entityPackage = getEntityPackage();
		final Collection<String> classNames = classNameLocator.locateClassNames(entityPackage);

		for (String className : classNames) {

			final Class<?> entityClass = getClass(className);

			if (accept(entityClass)) {
				entityClasses.add(entityClass);
			}

		}

	}

	/**
	 * Tells if a given class must be considered an entity. This method will return false if the
	 * class is an interface, an enum or an abstract class.
	 * 
	 * @param clasz a {@link Class}. It cannot be null.
	 * @return a <code>boolean</code>.
	 */
	protected boolean accept(Class<?> clasz) {

		boolean ok = true;

		if (clasz.isInterface() || clasz.isEnum() || Modifier.isAbstract(clasz.getModifiers())) {
			ok = false;
		}

		return ok;

	}

	@Override
	public String toString() {
		return String.format("Module %s (%s)", name, rootPackage);
	}

}
