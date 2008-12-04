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

package br.com.arsmachina.tapestrycrud.ioc;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.tapestry5.PrimaryKeyEncoder;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.internal.InternalConstants;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.ObjectLocator;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.services.ChainBuilder;
import org.apache.tapestry5.ioc.services.ClassNameLocator;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.services.LibraryMapping;
import org.apache.tapestry5.services.ValueEncoderFactory;
import org.apache.tapestry5.services.ValueEncoderSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.arsmachina.controller.Controller;
import br.com.arsmachina.dao.DAO;
import br.com.arsmachina.tapestrycrud.encoder.ActivationContextEncoder;
import br.com.arsmachina.tapestrycrud.encoder.Encoder;
import br.com.arsmachina.tapestrycrud.encoder.LabelEncoder;
import br.com.arsmachina.tapestrycrud.factory.DefaultControllerFactory;
import br.com.arsmachina.tapestrycrud.factory.DefaultDAOFactory;
import br.com.arsmachina.tapestrycrud.factory.PrimaryKeyEncoderFactory;
import br.com.arsmachina.tapestrycrud.factory.impl.DefaultControllerFactoryImpl;
import br.com.arsmachina.tapestrycrud.module.DefaultModule;
import br.com.arsmachina.tapestrycrud.module.Module;
import br.com.arsmachina.tapestrycrud.selectmodel.DefaultSingleTypeSelectModelFactory;
import br.com.arsmachina.tapestrycrud.selectmodel.SelectModelFactory;
import br.com.arsmachina.tapestrycrud.selectmodel.SingleTypeSelectModelFactory;
import br.com.arsmachina.tapestrycrud.selectmodel.impl.SelectModelFactoryImpl;
import br.com.arsmachina.tapestrycrud.services.ActivationContextEncoderSource;
import br.com.arsmachina.tapestrycrud.services.ControllerSource;
import br.com.arsmachina.tapestrycrud.services.DAOSource;
import br.com.arsmachina.tapestrycrud.services.EncoderSource;
import br.com.arsmachina.tapestrycrud.services.EntitySource;
import br.com.arsmachina.tapestrycrud.services.LabelEncoderSource;
import br.com.arsmachina.tapestrycrud.services.ModuleService;
import br.com.arsmachina.tapestrycrud.services.PrimaryKeyEncoderSource;
import br.com.arsmachina.tapestrycrud.services.PrimaryKeyTypeService;
import br.com.arsmachina.tapestrycrud.services.impl.ActivationContextEncoderSourceImpl;
import br.com.arsmachina.tapestrycrud.services.impl.ControllerSourceImpl;
import br.com.arsmachina.tapestrycrud.services.impl.DAOSourceImpl;
import br.com.arsmachina.tapestrycrud.services.impl.EncoderSourceImpl;
import br.com.arsmachina.tapestrycrud.services.impl.EntitySourceImpl;
import br.com.arsmachina.tapestrycrud.services.impl.LabelEncoderSourceImpl;
import br.com.arsmachina.tapestrycrud.services.impl.ModuleServiceImpl;
import br.com.arsmachina.tapestrycrud.services.impl.PrimaryKeyEncoderSourceImpl;
import br.com.arsmachina.tapestrycrud.services.impl.PrimaryKeyEncoderValueEncoder;

/**
 * Tapestry-IoC module for Tapestry CRUD.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public class TapestryCrudModule {

	/**
	 * Tapestry CRUD component prefix.
	 */
	final public static String TAPESTRY_CRUD_COMPONENT_PREFIX = "crud";

	/**
	 * Tapestry CRUD version.
	 */
	final public static String TAPESTRY_CRUD_VERSION = "1.0";

	/**
	 * Path under with the Tapestry CRUDs assets will be accessed.
	 */
	final public static String TAPESTRY_CRUD_ASSET_PREFIX = "tapestry-crud/"
			+ TAPESTRY_CRUD_VERSION;

	final private static Logger LOGGER = LoggerFactory.getLogger(TapestryCrudModule.class);

	/**
	 * Contributes all ({@link Class}, {@link Encoder} pairs registered in {@link EncoderSource}
	 * to {@link ValueEncoderSource}. If no {@link Encoder} is found for a given entity class, if a
	 * {@link PrimaryKeyEncoder} is found, a {@link ValueEncoderFactory} is automatically created
	 * using the {@link PrimaryKeyEncoder}.
	 * 
	 * @param configuration
	 * @param encoderSource
	 */
	@SuppressWarnings("unchecked")
	public static void contributeValueEncoderSource(
			MappedConfiguration<Class, ValueEncoderFactory> configuration,
			EncoderSource encoderSource, EntitySource entitySource,
			PrimaryKeyEncoderSource primaryKeyEncoderSource,
			PrimaryKeyTypeService primaryKeyTypeService, TypeCoercer typeCoercer) {

		Set<Class<?>> classes = entitySource.getEntityClasses();

		for (Class clasz : classes) {

			final Encoder encoder = encoderSource.get(clasz);

			if (encoder != null) {
				configuration.add(clasz, encoder);
			}
			else {

				PrimaryKeyEncoder primaryKeyEncoder = primaryKeyEncoderSource.get(clasz);

				if (primaryKeyEncoder != null) {

					final Class primaryKeyType = primaryKeyTypeService.getPrimaryKeyType(clasz);
					ValueEncoderFactory valueEncoderFactory = new PrimaryKeyEncoderValueEncoder(
							primaryKeyType, primaryKeyEncoder, typeCoercer);

					configuration.add(clasz, valueEncoderFactory);

				}

			}

		}

	}

	/**
	 * Builds the {@link ActivationContextEncoderSource} service.
	 * 
	 * @param contributions a {@link Map<Class, ActivationContextEncoder>}.
	 * @return an {@link ActivationContextEncoderSource}.
	 */
	@SuppressWarnings("unchecked")
	public static ActivationContextEncoderSource buildActivationContextEncoderSource(
			Map<Class, ActivationContextEncoder> contributions, EncoderSource encoderSource,
			PrimaryKeyEncoderSource primaryKeyEncoderSource,
			PrimaryKeyTypeService primaryKeyTypeService) {

		return new ActivationContextEncoderSourceImpl(contributions, encoderSource,
				primaryKeyEncoderSource, primaryKeyTypeService);

	}

	/**
	 * Builds the {@link LabelSource} service.
	 * 
	 * @param contributions a {@link Map<Class, ActivationContextEncoder>}.
	 * @return an {@link ActivationContextEncoderSource}.
	 */
	@SuppressWarnings("unchecked")
	public static LabelEncoderSource buildLabelEncoderSource(
			Map<Class, LabelEncoder> contributions, EncoderSource encoderSource) {
		return new LabelEncoderSourceImpl(contributions, encoderSource);
	}

	/**
	 * Builds the {@link PrimaryKeyEncoderSource} service.
	 * 
	 * @param contributions a {@link Map<Class, PrimaryKeyEncoder>}.
	 * @param encoderSource an {@link EncoderSource}.
	 * @param primaryKeyEncoderFactory a {@link PrimaryKeyEncoderFactory}.
	 * @return an {@link PrimaryKeyEncoderSource}.
	 */
	@SuppressWarnings("unchecked")
	public static PrimaryKeyEncoderSource buildPrimaryKeyEncoderSource(
			Map<Class, PrimaryKeyEncoder> contributions, EncoderSource encoderSource,
			PrimaryKeyEncoderFactory primaryKeyEncoderFactory) {
		return new PrimaryKeyEncoderSourceImpl(contributions, encoderSource,
				primaryKeyEncoderFactory);
	}

	/**
	 * Builds the {@link EncoderSource} service.
	 * 
	 * @param contributions a {@link Map<Class, Encoder>}.
	 * @return an {@link EncoderSource}.
	 */
	@SuppressWarnings("unchecked")
	public static EncoderSource buildEncoderSource(Map<Class, Encoder> contributions) {
		return new EncoderSourceImpl(contributions);
	}

	/**
	 * Builds the {@link ControllerSource} service.
	 * 
	 * @param contributions a {@link Map<Class, Controller>}.
	 * @return an {@link ControllerSource}.
	 */
	@SuppressWarnings("unchecked")
	public static ControllerSource buildControllerSource(Map<Class, Controller> contributions,
			DefaultControllerFactory defaultControllerFactory) {
		return new ControllerSourceImpl(contributions, defaultControllerFactory);
	}

	/**
	 * Builds the {@link DAOSource} service.
	 * 
	 * @param contributions a {@link Map<Class, DAO>}.
	 * @param entitySource an {@link EntitySource}.
	 * @param defaultDAOFactory a {@link DefaultDAOFactory}.
	 * @return an {@link DAOSource}.
	 */
	@SuppressWarnings("unchecked")
	public static DAOSource buildDAOSource(Map<Class, DAO> contributions,
			EntitySource entitySource, DefaultDAOFactory defaultDAOFactory) {
		return new DAOSourceImpl(contributions, entitySource, defaultDAOFactory);
	}

	/**
	 * Builds the {@link EntitySource} service.
	 * 
	 * @param contributions a {@link Map<Class, Controller>}.
	 * @return an {@link ControllerSource}.
	 */
	@SuppressWarnings("unchecked")
	public static EntitySource buildEntitySource(Collection<Class> contributions) {
		return new EntitySourceImpl(new HashSet(contributions));
	}

	/**
	 * Builds the {@link ModuleService} service.
	 * 
	 * @param contributions a {@link Map<Class, Controller>}.
	 * @return an {@link ControllerSource}.
	 */
	@SuppressWarnings("unchecked")
	public static ModuleService buildModuleSource(Collection<Module> contributions) {
		return new ModuleServiceImpl(new HashSet(contributions));
	}

	/**
	 * Builds the {@link SelectModelFactory} service.
	 * 
	 * @param contributions a {@link Map<Class, SingleTypeSelectModelFactory>}.
	 * @return a {@link SelectModelFactory}.
	 */
	@SuppressWarnings("unchecked")
	public static SelectModelFactory buildSelectModelFactory(
			Map<Class, SingleTypeSelectModelFactory> contributions) {
		return new SelectModelFactoryImpl(contributions);
	}

	/**
	 * Contributes the Tapestry CRUD components under the <code>crud</code> prefix.
	 * 
	 * @param configuration a {@link Configuration}.
	 */
	public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration) {

		configuration.add(new LibraryMapping(TAPESTRY_CRUD_COMPONENT_PREFIX,
				"br.com.arsmachina.tapestrycrud"));

	}

	public static void contributeClasspathAssetAliasManager(
			MappedConfiguration<String, String> configuration) {
		configuration.add(TAPESTRY_CRUD_ASSET_PREFIX, "br/com/arsmachina/tapestrycrud/components");
	}

	/**
	 * Contributes the main (default module) to the {@link ModuleService} service.
	 * 
	 * @param configuration a {@link Configuration} of {@link Module}s.
	 */
	public static void contributeModuleSource(Configuration<Module> configuration,
			ClassNameLocator classNameLocator, @Inject
			@Symbol(InternalConstants.TAPESTRY_APP_PACKAGE_PARAM)
			final String tapestryRootPackage) {

		// The convention is that the module root is one package level above the
		// Tapestry root package

		String modulePackage = tapestryRootPackage.substring(0,
				tapestryRootPackage.lastIndexOf('.'));

		configuration.add(new DefaultModule("Default module", modulePackage, classNameLocator));

	}

	/**
	 * Contributes the {@link Module}s entity classes to the {@link EntitySource} service.
	 * 
	 * @param configuration a {@link Configuration} of {@link Class} instances.
	 * @param moduleService a {@link ModuleService}.
	 */
	public static void contributeEntitySource(Configuration<Class<?>> configuration,
			ModuleService moduleService) {

		for (Class<?> entityClass : moduleService.getEntityClasses()) {
			configuration.add(entityClass);
		}

	}

	/**
	 * Associates entity classes with their {@link SelectModel}s.
	 * 
	 * @param contributions a {@link MappedConfiguration}.
	 */
	@SuppressWarnings("unchecked")
	public static void contributeSelectModelFactory(
			MappedConfiguration<Class, SingleTypeSelectModelFactory> contributions,
			ControllerSource controllerSource, EntitySource entitySource,
			LabelEncoderSource labelEncoderSource) {

		final Set<Class<?>> entityClasses = entitySource.getEntityClasses();

		for (Class<?> entityClass : entityClasses) {

			Controller controller = controllerSource.get(entityClass);
			LabelEncoder labelEncoder = labelEncoderSource.get(entityClass);

			if (controller != null && labelEncoder != null) {

				SingleTypeSelectModelFactory stsmf = new DefaultSingleTypeSelectModelFactory(
						controller, labelEncoder);

				contributions.add(entityClass, stsmf);

			}

		}

	}

	/**
	 * Associates entity classes with their {@link Encoder}s.
	 * 
	 * @param contributions a {@link MappedConfiguration}.
	 */
	@SuppressWarnings("unchecked")
	public static void contributeEncoderSource(MappedConfiguration<Class, Encoder> contributions,
			EntitySource entitySource, ModuleService moduleService, ObjectLocator objectLocator) {

		final Set<Class<?>> entityClasses = entitySource.getEntityClasses();
		Encoder encoder = null;

		for (Class<?> entityClass : entityClasses) {

			final Class<?> encoderClass = moduleService.getEncoderClass(entityClass);

			if (encoderClass != null) {

				encoder = (Encoder) getServiceIfExists(encoderClass, objectLocator);

				if (encoder == null) {
					encoder = (Encoder) objectLocator.autobuild(encoderClass);
				}

				contributions.add(entityClass, encoder);

				if (LOGGER.isInfoEnabled()) {

					final String entityName = entityClass.getSimpleName();
					final String encoderClassName = encoder.getClass().getName();
					final String message = String.format("Associating entity %s with encoder %s",
							entityName, encoderClassName);

					LOGGER.info(message);

				}

			}

		}

	}

	/**
	 * Returns a service if it exists. Otherwise, this method returns null.
	 * 
	 * @param <T> type of service.
	 * @param serviceInterface a {@link Class}.
	 * @param objectLocator an {@link ObjectLocator}.
	 * @return aa <code>T</code> or null.
	 */
	final private static <T> T getServiceIfExists(final Class<T> serviceInterface,
			ObjectLocator objectLocator) {

		try {
			return objectLocator.getService(serviceInterface);
		}
		catch (RuntimeException e) {
			return null;
		}

	}

	/**
	 * Associates entity classes with their {@link Controller}s.
	 * 
	 * @param contributions a {@link MappedConfiguration}.
	 */
	@SuppressWarnings("unchecked")
	public static void contributeControllerSource(
			MappedConfiguration<Class, Controller> contributions, EntitySource entitySource,
			ModuleService moduleService, ObjectLocator objectLocator) {

		final Set<Class<?>> entityClasses = entitySource.getEntityClasses();
		Controller controller = null;

		for (Class<?> entityClass : entityClasses) {

			final Class<?> controllerDefinitionClass = moduleService.getControllerDefinitionClass(entityClass);
			final Class<?> controllerImplementationClass = moduleService.getControllerImplementationClass(entityClass);

			// If the entity class has no controller definition (interface), we don't register
			// a controller for it.
			if (controllerDefinitionClass != null) {

				controller = (Controller) getServiceIfExists(controllerDefinitionClass,
						objectLocator);

				if (controller == null) {
					controller = (Controller) objectLocator.autobuild(controllerImplementationClass);
				}

				contributions.add(entityClass, controller);

				if (LOGGER.isInfoEnabled()) {

					final String entityName = entityClass.getSimpleName();
					final String controllerClassName = controller.getClass().getName();
					final String message = String.format(
							"Associating entity %s with controller %s", entityName,
							controllerClassName);

					LOGGER.info(message);

				}

			}

		}

	}

	/**
	 * Associates entity classes with their {@link Controller}s.
	 * 
	 * @param contributions a {@link MappedConfiguration}.
	 */
	@SuppressWarnings("unchecked")
	public static void contributeActivationContextEncoderSource(
			MappedConfiguration<Class, ActivationContextEncoder> contributions,
			EntitySource entitySource, ModuleService moduleService, ObjectLocator objectLocator) {

		final Set<Class<?>> entityClasses = entitySource.getEntityClasses();
		ActivationContextEncoder encoder = null;

		for (Class<?> entityClass : entityClasses) {

			final Class<?> encoderClass = moduleService.getActivationContextEncoderClass(entityClass);

			// If the entity class has no activation context encoder, we don't register
			// a one for it for it.
			if (encoderClass != null) {

				encoder = (ActivationContextEncoder) getServiceIfExists(encoderClass, objectLocator);

				if (encoder == null) {
					encoder = (ActivationContextEncoder) objectLocator.autobuild(encoderClass);
				}

				contributions.add(entityClass, encoder);

				if (LOGGER.isInfoEnabled()) {

					final String entityName = entityClass.getSimpleName();
					final String encoderClassName = encoder.getClass().getName();
					final String message = String.format(
							"Associating entity %s with activation context encoder %s", entityName,
							encoderClassName);

					LOGGER.info(message);

				}

			}

		}

	}

	/**
	 * Associates entity classes with their {@link LabelEncoder}s.
	 * 
	 * @param contributions a {@link MappedConfiguration}.
	 */
	@SuppressWarnings("unchecked")
	public static void contributeLabelEncoderSource(
			MappedConfiguration<Class, LabelEncoder> contributions, EntitySource entitySource,
			ModuleService moduleService, ObjectLocator objectLocator) {

		final Set<Class<?>> entityClasses = entitySource.getEntityClasses();
		LabelEncoder encoder = null;

		for (Class<?> entityClass : entityClasses) {

			final Class<?> encoderClass = moduleService.getLabelEncoderClass(entityClass);

			// If the entity class has no activation context encoder, we don't register
			// a one for it for it.
			if (encoderClass != null) {

				encoder = (LabelEncoder) getServiceIfExists(encoderClass, objectLocator);

				if (encoder == null) {
					encoder = (LabelEncoder) objectLocator.autobuild(encoderClass);
				}

				contributions.add(entityClass, encoder);

				if (LOGGER.isInfoEnabled()) {

					final String entityName = entityClass.getSimpleName();
					final String encoderClassName = encoder.getClass().getName();
					final String message = String.format(
							"Associating entity %s with label encoder %s", entityName,
							encoderClassName);

					LOGGER.info(message);

				}

			}

		}

	}

	/**
	 * Associates entity classes with their {@link PrimaryKeyEncoder}s.
	 * 
	 * @param contributions a {@link MappedConfiguration}.
	 */
	@SuppressWarnings("unchecked")
	public static void contributePrimaryKeyEncoderSource(
			MappedConfiguration<Class, PrimaryKeyEncoder> contributions, EntitySource entitySource,
			ModuleService moduleService, ObjectLocator objectLocator) {

		final Set<Class<?>> entityClasses = entitySource.getEntityClasses();
		PrimaryKeyEncoder encoder = null;

		for (Class<?> entityClass : entityClasses) {

			final Class<?> encoderClass = moduleService.getPrimaryKeyEncoderClass(entityClass);

			// If the entity class has no primary key encoder, we don't register
			// a one for it for it.
			if (encoderClass != null) {

				encoder = (PrimaryKeyEncoder) getServiceIfExists(encoderClass, objectLocator);

				if (encoder == null) {
					encoder = (PrimaryKeyEncoder) objectLocator.autobuild(encoderClass);
				}

				contributions.add(entityClass, encoder);

				if (LOGGER.isInfoEnabled()) {

					final String entityName = entityClass.getSimpleName();
					final String encoderClassName = encoder.getClass().getName();
					final String message = String.format(
							"Associating entity %s with primary key encoder %s", entityName,
							encoderClassName);

					LOGGER.info(message);

				}

			}

		}

	}

	/**
	 * Associantes entity classes with their {@link DAO}s.
	 * 
	 * @param contributions a {@link MappedConfiguration}.
	 */
	@SuppressWarnings("unchecked")
	public static void contributeDAOSource(MappedConfiguration<Class, DAO> contributions,
			EntitySource entitySource, ModuleService moduleService, ObjectLocator objectLocator) {

		final Set<Class<?>> entityClasses = entitySource.getEntityClasses();
		DAO dao = null;

		for (Class<?> entityClass : entityClasses) {

			final Class<?> daoDefinitionClass = moduleService.getDAODefinitionClass(entityClass);
			final Class<?> daoImplementationClass = moduleService.getDAOImplementationClass(entityClass);

			// If the entity class has no dao definition (interface), we don't register
			// a dao for it.
			if (daoDefinitionClass != null) {

				dao = (DAO) getServiceIfExists(daoDefinitionClass, objectLocator);

				if (dao == null) {
					dao = (DAO) objectLocator.autobuild(daoImplementationClass);
				}

				contributions.add(entityClass, dao);

				if (LOGGER.isInfoEnabled()) {

					final String entityName = entityClass.getSimpleName();
					final String daoClassName = dao.getClass().getName();
					final String message = String.format("Associating entity %s with DAO %s",
							entityName, daoClassName);

					LOGGER.info(message);

				}

			}

		}

	}

	/**
	 * Builds the {@link PrimaryKeyEncoderFactory} service.
	 * 
	 * @param contributions a {@link List} of {@link PrimaryKeyEncoderFactory}.
	 * @param chainBuilder a {@link ChainBuilder}.
	 * @return a {@link DefaultDAOFactory}.
	 */
	public PrimaryKeyEncoderFactory buildPrimaryKeyEncoderFactory(
			final List<PrimaryKeyEncoderFactory> contributions, ChainBuilder chainBuilder) {

		return chainBuilder.build(PrimaryKeyEncoderFactory.class, contributions);

	}

	/**
	 * Builds the {@link DefaultDAOFactory} service.
	 * 
	 * @param contributions a {@link List} of {@link DefaultDAOFactory}.
	 * @param chainBuilder a {@link ChainBuilder}.
	 * @return a {@link DefaultDAOFactory}.
	 */
	public DefaultDAOFactory buildDefaultDAOFactory(final List<DefaultDAOFactory> contributions,
			ChainBuilder chainBuilder) {

		return chainBuilder.build(DefaultDAOFactory.class, contributions);

	}

	/**
	 * Builds the {@link DefaultLabelEncoderFactory} service.
	 * 
	 * @param contributions a {@link List} of {@link DefaultControllerFactory}.
	 * @param chainBuilder a {@link ChainBuilder}.
	 * @return a {@link DefaultControllerFactory}.
	 */
	public DefaultControllerFactory buildDefaultControllerFactory(
			final List<DefaultControllerFactory> contributions, ChainBuilder chainBuilder) {

		return chainBuilder.build(DefaultControllerFactory.class, contributions);

	}

	/**
	 * Builds the {@link DefaultLabelEncoderFactory} service.
	 * 
	 * @param contributions a {@link List} of {@link PrimaryKeyTypeService}.
	 * @param chainBuilder a {@link ChainBuilder}.
	 * @return a {@link PrimaryKeyTypeService}.
	 */
	public PrimaryKeyTypeService buildPrimaryKeyTypeService(
			final List<PrimaryKeyTypeService> contributions, ChainBuilder chainBuilder) {

		return chainBuilder.build(PrimaryKeyTypeService.class, contributions);

	}

	/**
	 * Contributes to the {@link DefaultControllerFactory} service.
	 * 
	 * @param contributions a {@link List} of {@link DefaultControllerFactory}.
	 */
	public static void contributeDefaultControllerFactory(
			OrderedConfiguration<DefaultControllerFactory> configuration, DAOSource daoSource) {

		configuration.add("default", new DefaultControllerFactoryImpl(daoSource), "after:*");

	}

}
