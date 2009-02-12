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

import static br.com.arsmachina.module.ioc.ApplicationModuleModule.getServiceIfExists;

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
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.services.ChainBuilder;
import org.apache.tapestry5.ioc.services.ClassNameLocator;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.services.BeanBlockContribution;
import org.apache.tapestry5.services.DataTypeAnalyzer;
import org.apache.tapestry5.services.LibraryMapping;
import org.apache.tapestry5.services.ValueEncoderFactory;
import org.apache.tapestry5.services.ValueEncoderSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.arsmachina.authentication.service.UserService;
import br.com.arsmachina.controller.Controller;
import br.com.arsmachina.module.DefaultModule;
import br.com.arsmachina.module.Module;
import br.com.arsmachina.module.factory.DAOFactory;
import br.com.arsmachina.module.ioc.ApplicationModuleModule;
import br.com.arsmachina.module.service.ControllerSource;
import br.com.arsmachina.module.service.EntitySource;
import br.com.arsmachina.module.service.ModuleService;
import br.com.arsmachina.module.service.PrimaryKeyTypeService;
import br.com.arsmachina.tapestrycrud.Constants;
import br.com.arsmachina.tapestrycrud.beanmodel.EntityDataTypeAnalyzer;
import br.com.arsmachina.tapestrycrud.encoder.ActivationContextEncoder;
import br.com.arsmachina.tapestrycrud.encoder.Encoder;
import br.com.arsmachina.tapestrycrud.encoder.LabelEncoder;
import br.com.arsmachina.tapestrycrud.factory.PrimaryKeyEncoderFactory;
import br.com.arsmachina.tapestrycrud.module.TapestryCrudModule;
import br.com.arsmachina.tapestrycrud.selectmodel.DefaultSingleTypeSelectModelFactory;
import br.com.arsmachina.tapestrycrud.selectmodel.SelectModelFactory;
import br.com.arsmachina.tapestrycrud.selectmodel.SingleTypeSelectModelFactory;
import br.com.arsmachina.tapestrycrud.selectmodel.impl.SelectModelFactoryImpl;
import br.com.arsmachina.tapestrycrud.services.ActivationContextEncoderSource;
import br.com.arsmachina.tapestrycrud.services.EncoderSource;
import br.com.arsmachina.tapestrycrud.services.FormValidationSupport;
import br.com.arsmachina.tapestrycrud.services.LabelEncoderSource;
import br.com.arsmachina.tapestrycrud.services.PageUtil;
import br.com.arsmachina.tapestrycrud.services.PrimaryKeyEncoderSource;
import br.com.arsmachina.tapestrycrud.services.TapestryCrudModuleFactory;
import br.com.arsmachina.tapestrycrud.services.TapestryCrudModuleService;
import br.com.arsmachina.tapestrycrud.services.impl.ActivationContextEncoderSourceImpl;
import br.com.arsmachina.tapestrycrud.services.impl.EncoderSourceImpl;
import br.com.arsmachina.tapestrycrud.services.impl.FormValidationSupportImpl;
import br.com.arsmachina.tapestrycrud.services.impl.LabelEncoderSourceImpl;
import br.com.arsmachina.tapestrycrud.services.impl.PageUtilImpl;
import br.com.arsmachina.tapestrycrud.services.impl.PrimaryKeyEncoderSourceImpl;
import br.com.arsmachina.tapestrycrud.services.impl.PrimaryKeyEncoderValueEncoder;
import br.com.arsmachina.tapestrycrud.services.impl.TapestryCrudModuleFactoryImpl;
import br.com.arsmachina.tapestrycrud.services.impl.TapestryCrudModuleServiceImpl;
import br.com.arsmachina.tapestrycrud.services.impl.UserServiceImpl;

/**
 * Tapestry-IoC module for Tapestry CRUD.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public class TapestryCrudIoCModule {

	/**
	 * Binds some Tapestry CRUD services.
	 * 
	 * @param binder a {@link ServiceBinder}.
	 */
	public static void bind(ServiceBinder binder) {

		binder.bind(UserService.class, UserServiceImpl.class);
		binder.bind(EntityDataTypeAnalyzer.class);
		binder.bind(PageUtil.class, PageUtilImpl.class);
		binder.bind(FormValidationSupport.class,
				FormValidationSupportImpl.class);

	}

	/**
	 * Tapestry CRUD library prefix.
	 */
	final public static String TAPESTRY_CRUD_COMPONENT_PREFIX =
		Constants.TAPESTRY_CRUD_LIBRARY_PREFIX;

	/**
	 * Tapestry CRUD version.
	 */
	final public static String TAPESTRY_CRUD_VERSION = "1.0";

	/**
	 * Path under with the Tapestry CRUDs assets will be accessed.
	 */
	final public static String TAPESTRY_CRUD_ASSET_PREFIX =
		"tapestry-crud/" + TAPESTRY_CRUD_VERSION;

	final private static Logger LOGGER =
		LoggerFactory.getLogger(TapestryCrudIoCModule.class);

	/**
	 * Builds the {@link ModuleService} service.
	 * 
	 * @param contributions a {@link Map<Class, Controller>}.
	 * @return an {@link ControllerSource}.
	 */
	public static void contributeTapestryCrudModuleService(
			Configuration<TapestryCrudModule> contributions,
			ModuleService moduleService,
			ClassNameLocator classNameLocator,
			@Inject @Symbol(ApplicationModuleModule.DAO_IMPLEMENTATION_SUBPACKAGE_SYMBOL) String daoImplementationSubpackage,
			@Inject @Symbol(InternalConstants.TAPESTRY_APP_PACKAGE_PARAM) final String tapestryRootPackage,
			TapestryCrudModuleFactory tapestryCrudModuleBuilder) {

		final Set<Module> modules = moduleService.getModules();

		for (Module module : modules) {
			contributions.add(tapestryCrudModuleBuilder.build(module));
		}

	}

	/**
	 * Builds the {@link ModuleService} service.
	 * 
	 * @param contributions a {@link Map<Class, Controller>}.
	 * @return an {@link ControllerSource}.
	 */
	@SuppressWarnings("unchecked")
	public static TapestryCrudModuleService buildTapestryCrudModuleService(
			Collection<TapestryCrudModule> contributions) {

		return new TapestryCrudModuleServiceImpl(new HashSet(contributions));

	}

	/**
	 * Contributes all ({@link Class}, {@link Encoder} pairs registered in
	 * {@link EncoderSource} to {@link ValueEncoderSource}. If no
	 * {@link Encoder} is found for a given entity class, if a
	 * {@link PrimaryKeyEncoder} is found, a {@link ValueEncoderFactory} is
	 * automatically created using the {@link PrimaryKeyEncoder}.
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
			} else {

				PrimaryKeyEncoder primaryKeyEncoder =
					primaryKeyEncoderSource.get(clasz);

				if (primaryKeyEncoder != null) {

					final Class primaryKeyType =
						primaryKeyTypeService.getPrimaryKeyType(clasz);
					ValueEncoderFactory valueEncoderFactory =
						new PrimaryKeyEncoderValueEncoder(primaryKeyType,
								primaryKeyEncoder, typeCoercer);

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
			Map<Class, ActivationContextEncoder> contributions,
			EncoderSource encoderSource,
			PrimaryKeyEncoderSource primaryKeyEncoderSource,
			PrimaryKeyTypeService primaryKeyTypeService) {

		return new ActivationContextEncoderSourceImpl(contributions,
				encoderSource, primaryKeyEncoderSource, primaryKeyTypeService);

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
			Map<Class, PrimaryKeyEncoder> contributions,
			EncoderSource encoderSource,
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
	public static EncoderSource buildEncoderSource(
			Map<Class, Encoder> contributions) {
		return new EncoderSourceImpl(contributions);
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
	 * Contributes the Tapestry CRUD components under the <code>crud</code>
	 * prefix and all the {@link TapestryCrudModule}s with their id property as
	 * the prefix.
	 * 
	 * @param configuration a {@link Configuration}.
	 */
	public static void contributeComponentClassResolver(
			Configuration<LibraryMapping> configuration,
			TapestryCrudModuleService tapestryCrudModuleService) {

		configuration.add(new LibraryMapping(
				Constants.TAPESTRY_CRUD_LIBRARY_PREFIX,
				"br.com.arsmachina.tapestrycrud"));

		final Set<TapestryCrudModule> modules =
			tapestryCrudModuleService.getModules();

		for (TapestryCrudModule module : modules) {

			final String id = module.getId();
			
			if (id != null && id.trim().length() > 0) {

				final String tapestryPackage = module.getTapestryPackage();
				configuration.add(new LibraryMapping(id, tapestryPackage));
				
			}

		}

	}

	public static void contributeClasspathAssetAliasManager(
			MappedConfiguration<String, String> configuration) {
		configuration.add(TAPESTRY_CRUD_ASSET_PREFIX,
				"br/com/arsmachina/tapestrycrud/components");
	}

	/**
	 * Contributes the main (default module) to the {@link ModuleService}
	 * service.
	 * 
	 * @param configuration a {@link Configuration} of {@link Module}s.
	 */
	public static void contributeModuleService(
			Configuration<Module> configuration,
			ClassNameLocator classNameLocator,
			@Inject @Symbol(InternalConstants.TAPESTRY_APP_PACKAGE_PARAM) final String tapestryRootPackage,
			@Inject @Symbol(ApplicationModuleModule.DAO_IMPLEMENTATION_SUBPACKAGE_SYMBOL) String daoImplementationSubpackage) {

		// The convention is that the module root is one package level above the
		// Tapestry root package

		String modulePackage =
			tapestryRootPackage.substring(0,
					tapestryRootPackage.lastIndexOf('.'));

		final DefaultModule module =
			new DefaultModule(modulePackage, classNameLocator,
					daoImplementationSubpackage);
		configuration.add(module);

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

				SingleTypeSelectModelFactory stsmf =
					new DefaultSingleTypeSelectModelFactory(controller,
							labelEncoder);

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
	public static void contributeEncoderSource(
			MappedConfiguration<Class, Encoder> contributions,
			EntitySource entitySource, ModuleService moduleService,
			TapestryCrudModuleService tapestryCrudModuleService,
			ObjectLocator objectLocator) {

		final Set<Class<?>> entityClasses = entitySource.getEntityClasses();
		Encoder encoder = null;

		for (Class<?> entityClass : entityClasses) {

			final Class<?> encoderClass =
				tapestryCrudModuleService.getEncoderClass(entityClass);

			if (encoderClass != null) {

				encoder =
					(Encoder) getServiceIfExists(encoderClass, objectLocator);

				if (encoder == null) {
					encoder = (Encoder) objectLocator.autobuild(encoderClass);
				}

				contributions.add(entityClass, encoder);

				if (LOGGER.isInfoEnabled()) {

					final String entityName = entityClass.getSimpleName();
					final String encoderClassName =
						encoder.getClass().getName();
					final String message =
						String.format("Associating entity %s with encoder %s",
								entityName, encoderClassName);

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
			EntitySource entitySource, ModuleService moduleService,
			TapestryCrudModuleService tapestryCrudModuleService,
			ObjectLocator objectLocator) {

		final Set<Class<?>> entityClasses = entitySource.getEntityClasses();
		ActivationContextEncoder encoder = null;

		for (Class<?> entityClass : entityClasses) {

			final Class<?> encoderClass =
				tapestryCrudModuleService.getActivationContextEncoderClass(entityClass);

			// If the entity class has no activation context encoder, we don't
			// register
			// a one for it for it.
			if (encoderClass != null) {

				encoder =
					(ActivationContextEncoder) getServiceIfExists(encoderClass,
							objectLocator);

				if (encoder == null) {
					encoder =
						(ActivationContextEncoder) objectLocator.autobuild(encoderClass);
				}

				contributions.add(entityClass, encoder);

				if (LOGGER.isInfoEnabled()) {

					final String entityName = entityClass.getSimpleName();
					final String encoderClassName =
						encoder.getClass().getName();
					final String message =
						String.format(
								"Associating entity %s with activation context encoder %s",
								entityName, encoderClassName);

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
			MappedConfiguration<Class, LabelEncoder> contributions,
			EntitySource entitySource, ModuleService moduleService,
			TapestryCrudModuleService tapestryCrudModuleService,
			ObjectLocator objectLocator) {

		final Set<Class<?>> entityClasses = entitySource.getEntityClasses();
		LabelEncoder encoder = null;

		for (Class<?> entityClass : entityClasses) {

			final Class<?> encoderClass =
				tapestryCrudModuleService.getLabelEncoderClass(entityClass);

			// If the entity class has no activation context encoder, we don't
			// register
			// a one for it for it.
			if (encoderClass != null) {

				encoder =
					(LabelEncoder) getServiceIfExists(encoderClass,
							objectLocator);

				if (encoder == null) {
					encoder =
						(LabelEncoder) objectLocator.autobuild(encoderClass);
				}

				contributions.add(entityClass, encoder);

				if (LOGGER.isInfoEnabled()) {

					final String entityName = entityClass.getSimpleName();
					final String encoderClassName =
						encoder.getClass().getName();
					final String message =
						String.format(
								"Associating entity %s with label encoder %s",
								entityName, encoderClassName);

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
			MappedConfiguration<Class, PrimaryKeyEncoder> contributions,
			EntitySource entitySource, ModuleService moduleService,
			TapestryCrudModuleService tapestryCrudModuleService,
			ObjectLocator objectLocator) {

		final Set<Class<?>> entityClasses = entitySource.getEntityClasses();
		PrimaryKeyEncoder encoder = null;

		for (Class<?> entityClass : entityClasses) {

			final Class<?> encoderClass =
				tapestryCrudModuleService.getPrimaryKeyEncoderClass(entityClass);

			// If the entity class has no primary key encoder, we don't register
			// a one for it for it.
			if (encoderClass != null) {

				encoder =
					(PrimaryKeyEncoder) getServiceIfExists(encoderClass,
							objectLocator);

				if (encoder == null) {
					encoder =
						(PrimaryKeyEncoder) objectLocator.autobuild(encoderClass);
				}

				contributions.add(entityClass, encoder);

				if (LOGGER.isInfoEnabled()) {

					final String entityName = entityClass.getSimpleName();
					final String encoderClassName =
						encoder.getClass().getName();
					final String message =
						String.format(
								"Associating entity %s with primary key encoder %s",
								entityName, encoderClassName);

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
	 * @return a {@link DAOFactory}.
	 */
	public PrimaryKeyEncoderFactory buildPrimaryKeyEncoderFactory(
			final List<PrimaryKeyEncoderFactory> contributions,
			ChainBuilder chainBuilder) {

		return chainBuilder.build(PrimaryKeyEncoderFactory.class, contributions);

	}

	/**
	 * Builds the {@link TapestryCrudModuleBuilderFactory} service.
	 * 
	 * @param contributions a {@link List} of {@link TapestryCrudModuleFactory}.
	 * @param chainBuilder a {@link ChainBuilder}.
	 * @return a {@link DAOFactory}.
	 */
	public static TapestryCrudModuleFactory buildTapestryCrudModuleFactory(
			final List<TapestryCrudModuleFactory> contributions,
			ChainBuilder chainBuilder) {

		// default implementation.
		contributions.add(new TapestryCrudModuleFactoryImpl());

		return chainBuilder.build(TapestryCrudModuleFactory.class,
				contributions);

	}

	/**
	 * Contributes {@link EntityDataTypeAnalyzer} to the
	 * {@link DataTypeAnalyzer} service.
	 * 
	 * @param configuration an {@link OrderedConfiguration}.
	 * @param entityDataTypeAnalyzer an {@link EntityDataTypeAnalyzer}.
	 */
	public static void contributeDataTypeAnalyzer(
			OrderedConfiguration<DataTypeAnalyzer> configuration,
			EntityDataTypeAnalyzer entityDataTypeAnalyzer) {

		configuration.add(Constants.ENTITY_DATA_TYPE, entityDataTypeAnalyzer,
				"before:Annotation");

	}

	/**
	 * Contributes
	 * 
	 * @param configuration a {@link Configuration}.
	 */
	public static void contributeBeanBlockSource(
			Configuration<BeanBlockContribution> configuration) {

		configuration.add(new BeanBlockContribution(Constants.ENTITY_DATA_TYPE,
				Constants.BEAN_MODEL_BLOCKS_PAGE, "editEntity", true));

		configuration.add(new BeanBlockContribution(Constants.ENTITY_DATA_TYPE,
				Constants.BEAN_MODEL_BLOCKS_PAGE, "viewEntity", false));

	}

}
