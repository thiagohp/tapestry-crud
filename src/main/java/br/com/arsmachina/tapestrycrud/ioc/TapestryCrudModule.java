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
import java.util.Map;

import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.services.LibraryMapping;
import org.apache.tapestry5.services.ValueEncoderFactory;

import br.com.arsmachina.controller.Controller;
import br.com.arsmachina.tapestrycrud.encoder.ActivationContextEncoder;
import br.com.arsmachina.tapestrycrud.encoder.Encoder;
import br.com.arsmachina.tapestrycrud.encoder.LabelEncoder;
import br.com.arsmachina.tapestrycrud.selectmodel.SelectModelFactory;
import br.com.arsmachina.tapestrycrud.selectmodel.SingleTypeSelectModelFactory;
import br.com.arsmachina.tapestrycrud.selectmodel.impl.SelectModelFactoryImpl;
import br.com.arsmachina.tapestrycrud.services.ActivationContextEncoderSource;
import br.com.arsmachina.tapestrycrud.services.ControllerSource;
import br.com.arsmachina.tapestrycrud.services.EncoderSource;
import br.com.arsmachina.tapestrycrud.services.LabelEncoderSource;
import br.com.arsmachina.tapestrycrud.services.impl.ActivationContextEncoderSourceImpl;
import br.com.arsmachina.tapestrycrud.services.impl.ControllerSourceImpl;
import br.com.arsmachina.tapestrycrud.services.impl.EncoderSourceImpl;
import br.com.arsmachina.tapestrycrud.services.impl.LabelEncoderSourceImpl;

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
	final public static String TAPESTRY_CRUD_VERSION = "0.9";

	/**
	 * Path under with the Tapestry CRUDs assets will be accessed.
	 */
	final public static String TAPESTRY_CRUD_ASSET_PREFIX = "tapestry-crud/"
			+ TAPESTRY_CRUD_VERSION;

	/**
	 * Contributes all ({@link Class}, {@link Encoder} pairs registered in {@link EncoderSource}
	 * to {@link ValueEncoderFactory}.
	 * 
	 * @param configuration
	 * @param encoderSource
	 */
	@SuppressWarnings("unchecked")
	public void contributeValueEncoderFactory(
			MappedConfiguration<Class, ValueEncoderFactory> configuration,
			EncoderSource encoderSource) {

		Collection<Class> classes = encoderSource.getClasses();

		for (Class clasz : classes) {
			configuration.add(clasz, encoderSource.get(clasz));
		}

	}

	/**
	 * Contributes a {@link ValueEncoderFactory}s for each registered {@link Encoder}.
	 */
	@SuppressWarnings("unchecked")
	public static void contributeValueEncoderSource(
			MappedConfiguration<Class, ValueEncoderFactory> configuration,
			EncoderSource encoderSource) {

		Collection<Class> classes = encoderSource.getClasses();

		for (Class clasz : classes) {
			configuration.add(clasz, encoderSource.get(clasz));
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
			Map<Class, ActivationContextEncoder> contributions, EncoderSource encoderSource) {
		return new ActivationContextEncoderSourceImpl(contributions, encoderSource);
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
	public static ControllerSource buildControllerSource(Map<Class, Controller> contributions) {
		return new ControllerSourceImpl(contributions);
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
	 * Contributes the tapestry-crud components under the <code>crud</code> prefix.
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

}
