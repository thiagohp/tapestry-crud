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

package net.sf.arsmachina.tapestrycrud.services;

import net.sf.arsmachina.controller.Controller;
import net.sf.arsmachina.tapestrycrud.encoder.ActivationContextEncoder;
import net.sf.arsmachina.tapestrycrud.encoder.Encoder;
import net.sf.arsmachina.tapestrycrud.ioc.TapestryCrudModule;
import net.sf.arsmachina.tapestrycrud.selectmodel.SelectModelFactory;

import org.apache.tapestry5.ioc.MappedConfiguration;

/**
 * Tapestry-IoC module used to test {@link TapestryCrudModule}.
 * 
 * @author Thiago H. de Paula Figueiredo (ThiagoHP)
 */
public class TestModule {

	@SuppressWarnings("unchecked")
	public static Encoder[] encoders;

	@SuppressWarnings("unchecked")
	public static Controller[] controllers;

	@SuppressWarnings("unchecked")
	public static SelectModelFactory[] selectModelFactories;

	@SuppressWarnings("unchecked")
	public static Class[] classes;

	@SuppressWarnings("unchecked")
	public static void contributeActivationContextEncoderSource(
			MappedConfiguration<Class, ActivationContextEncoder> configuration) {

//		for (int i = 0; i < classes.length; i++) {
//			configuration.add(classes[i], activationContextEncoders[i]);
//		}

	}

	@SuppressWarnings("unchecked")
	public static void contributeSelectModelFactory(
			MappedConfiguration<Class, SelectModelFactory> configuration) {

//		for (int i = 0; i < classes.length; i++) {
//			configuration.add(classes[i], selectModelFactories[i]);
//		}

	}

	@SuppressWarnings("unchecked")
	public static void contributeEncoderSource(MappedConfiguration<Class, Encoder> configuration) {

		for (int i = 0; i < classes.length; i++) {
			configuration.add(classes[i], encoders[i]);
		}

	}

	@SuppressWarnings("unchecked")
	public static void contributeControllerSource(
			MappedConfiguration<Class, Controller> configuration) {

		for (int i = 0; i < classes.length; i++) {
			configuration.add(classes[i], controllers[i]);
		}

	}

	/**
	 * @param encoders the encoders to set
	 */
	@SuppressWarnings("unchecked")
	public static void setEncoders(Encoder... encoders) {
		TestModule.encoders = encoders;
	}

	/**
	 * @param classes the classes to set
	 */
	@SuppressWarnings("unchecked")
	public static void setClasses(Class... classes) {
		TestModule.classes = classes;
	}

	/**
	 * @param controllers the controllers to set
	 */
	@SuppressWarnings("unchecked")
	public static void setControllers(Controller[] controllers) {
		TestModule.controllers = controllers;
	}

}
