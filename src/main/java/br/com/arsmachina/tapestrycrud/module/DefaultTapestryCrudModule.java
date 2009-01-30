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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.arsmachina.module.Module;
import br.com.arsmachina.tapestrycrud.encoder.ActivationContextEncoder;
import br.com.arsmachina.tapestrycrud.encoder.Encoder;
import br.com.arsmachina.tapestrycrud.encoder.LabelEncoder;

/**
 * Default {@link TapestryCrudModule} implementation.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public class DefaultTapestryCrudModule implements TapestryCrudModule {

	final private Module module;

	final private TapestryCrudModuleConfiguration configuration;

	final private Logger logger = LoggerFactory.getLogger(DefaultTapestryCrudModule.class);

	/**
	 * Constructor that receives a {@link Module} and uses default configuration.
	 * 
	 * @param module a {@link Module}. It cannot be null.
	 */
	public DefaultTapestryCrudModule(Module module) {
		this(module, new TapestryCrudModuleConfiguration());
	}

	/**
	 * Constructor that receives a {@link Module} and a {@link TapestryCrudModuleConfiguration}.
	 * 
	 * @param module a {@link Module}. It cannot be null.
	 * @param configuration a {@link TapestryCrudModuleConfiguration}. It cannot be null.
	 */
	public DefaultTapestryCrudModule(Module module, TapestryCrudModuleConfiguration configuration) {

		if (module == null) {
			throw new IllegalArgumentException("Parameter module cannot be null");
		}

		if (configuration == null) {
			throw new IllegalArgumentException("Parameter configuration cannot be null");
		}

		this.configuration = configuration;
		this.module = module;

	}

	@SuppressWarnings("unchecked")
	public <T> Class<? extends ActivationContextEncoder<T>> getActivationContextEncoderClass(
			Class<T> entityClass) {

		if (contains(entityClass)) {
			return getClass(getActivationContextEncoderClassName(entityClass));
		}
		else {
			return null;
		}

	}

	@SuppressWarnings("unchecked")
	public <T> Class<? extends Encoder<T, ?>> getEncoderClass(Class<T> entityClass) {

		if (contains(entityClass)) {
			return getClass(getEncoderClassName(entityClass));
		}
		else {
			return null;
		}

	}

	@SuppressWarnings("unchecked")
	public <T> Class<? extends LabelEncoder<T>> getLabelEncoderClass(Class<T> entityClass) {

		if (contains(entityClass)) {
			return getClass(getLabelEncoderClassName(entityClass));
		}
		else {
			return null;
		}

	}

	@SuppressWarnings("unchecked")
	public <T> Class<? extends PrimaryKeyEncoder<?, T>> getPrimaryKeyEncoderClass(
			Class<T> entityClass) {

		if (contains(entityClass)) {
			return getClass(getPrimaryKeyEncoderClassName(entityClass));
		}
		else {
			return null;
		}

	}

	/**
	 * Returns the fully-qualified name of the activation context encoder for a given entity class.
	 * 
	 * @param clasz a {@link Class}. It cannot be null.
	 * @return a {@link String} or null (if no corresponding one is found).
	 */
	protected String getActivationContextEncoderClassName(Class<?> entityClass) {

		return String.format("%s.tapestry.encoder.activationcontext.%sActivationContextEncoder",
				getRootPackage(), entityClass.getSimpleName());

	}

	/**
	 * Returns the fully-qualified name of the encoder for a given entity class.
	 * 
	 * @param clasz a {@link Class}. It cannot be null.
	 * @return a {@link String} or null (if no corresponding one is found).
	 */
	protected String getEncoderClassName(Class<?> entityClass) {

		return String.format("%s.tapestry.encoder.%sEncoder", getRootPackage(),
				entityClass.getSimpleName());

	}

	/**
	 * Returns the fully-qualified name of the label encoder for a given entity class.
	 * 
	 * @param clasz a {@link Class}. It cannot be null.
	 * @return a {@link String} or null (if no corresponding one is found).
	 */
	protected String getLabelEncoderClassName(Class<?> entityClass) {

		return String.format("%s.tapestry.encoder.label.%sLabelEncoder", getRootPackage(),
				entityClass.getSimpleName());

	}

	/**
	 * Returns the fully-qualified name of the primary key encoder for a given entity class.
	 * 
	 * @param clasz a {@link Class}. It cannot be null.
	 * @return a {@link String} or null (if no corresponding one is found).
	 */
	protected String getPrimaryKeyEncoderClassName(Class<?> entityClass) {

		return String.format("%s.tapestry.encoder.primarykey.%sPrimaryKeyEncoder",
				getRootPackage(), entityClass.getSimpleName());

	}

	public String getEditPageClassName(Class<?> entityClass) {

		final String className = entityClass.getSimpleName();

		return String.format("%s.pages.%s%s.Edit%s", getTapestryPackage(), getModuleSubpackage(),
				className.toLowerCase(), className);

	}

	public String getListPageClassName(Class<?> entityClass) {

		final String className = entityClass.getSimpleName();

		return String.format("%s.pages.%s%s.List%s", getTapestryPackage(), getModuleSubpackage(),
				className.toLowerCase(), className);

	}

	@Override
	public String toString() {
		return String.format("TapestryCrudModule %s (%s)", getId(), getRootPackage());
	}

	/**
	 * Returns the name of the package where the Tapestry-related packages are located (i.e. under
	 * which the <code>pages</code> component is located). This implementation returns
	 * <code>[rootPackage].tapestry</code>.
	 * 
	 * @return a {@link String}.
	 */
	public String getTapestryPackage() {
		return getRootPackage() + ".tapestry";
	}

	private String getModuleSubpackage() {

		String moduleSubpackage = getId();

		if (moduleSubpackage != null) {
			moduleSubpackage = "." + moduleSubpackage;
		}
		else {
			moduleSubpackage = "";
		}

		return moduleSubpackage;

	}

	public boolean contains(Class<?> entityClass) {
		return module.contains(entityClass);
	}

	public Set<Class<?>> getEntityClasses() {
		return module.getEntityClasses();
	}

	public String getId() {
		return module.getId();
	}

	private String getRootPackage() {
		return module.getRootPackage();
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
	 * @param lenient a <code>boolean</code>. If <code>false</code>, it will throw an exception if
	 * the class is not found. Otherwise, this method will return null.
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

				if (logger.isDebugEnabled()) {
					logger.debug("Class not found: " + name);
				}

			}

		}

		return clasz;

	}

	public String getEditPageURL(Class<?> entityClass) {

		final String moduleSubpackage = getModuleSubpackage().replace('.', '/');

		final String className = entityClass.getSimpleName().toLowerCase();
		return String.format("%s%s/%s", moduleSubpackage, className, configuration.getEditPage());

	}

	public String getListPageURL(Class<?> entityClass) {

		final String moduleSubpackage = getModuleSubpackage().replace('.', '/');

		final String className = entityClass.getSimpleName().toLowerCase();
		return String.format("%s%s/%s", moduleSubpackage, className, configuration.getListPage());

	}

	public Class<?> getEditPageClass(Class<?> entityClass) {
		return getClass(getEditPageClassName(entityClass));
	}

	public Class<?> getListPageClass(Class<?> entityClass) {
		return getClass(getListPageClassName(entityClass));
	}

}
