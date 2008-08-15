package br.com.arsmachina.tapestrycrud.services;

import java.sql.Timestamp;
import java.util.Date;


import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.ioc.RegistryBuilder;
import org.easymock.EasyMock;
import org.testng.annotations.Test;

import br.com.arsmachina.controller.Controller;
import br.com.arsmachina.tapestrycrud.encoder.ActivationContextEncoder;
import br.com.arsmachina.tapestrycrud.encoder.Encoder;
import br.com.arsmachina.tapestrycrud.encoder.LabelEncoder;
import br.com.arsmachina.tapestrycrud.ioc.TapestryCrudModule;
import br.com.arsmachina.tapestrycrud.selectmodel.SingleTypeSelectModelFactory;
import br.com.arsmachina.tapestrycrud.services.ActivationContextEncoderSource;
import br.com.arsmachina.tapestrycrud.services.ControllerSource;
import br.com.arsmachina.tapestrycrud.services.EncoderSource;
import br.com.arsmachina.tapestrycrud.services.LabelEncoderSource;

/**
 * Tapestry-IoC module for Tapestry-CRUD
 * 
 * @author Thiago H. de Paula Figueiredo (ThiagoHP)
 */
@SuppressWarnings("unchecked")
public class TapestryCrudModuleTest {
	
	private static Registry registry;

	@SuppressWarnings("unchecked")
	public static ActivationContextEncoder[] activationContextEncoders;

	@SuppressWarnings("unchecked")
	public static Encoder[] encoders;

	@SuppressWarnings("unchecked")
	public static Controller[] controllers;

	public static SingleTypeSelectModelFactory stsmfs;

	@SuppressWarnings("unchecked")
	public static Class[] classes;

	static {
		
		final Controller stringController = EasyMock.createMock(Controller.class);
		final Controller integerController = EasyMock.createMock(Controller.class);
		final Controller dateController = EasyMock.createMock(Controller.class);

		controllers = new Controller[] { stringController, integerController, dateController };

		TestModule.setControllers(controllers);

		final Encoder stringEncoder = EasyMock.createMock(Encoder.class);
		final Encoder integerEncoder = EasyMock.createMock(Encoder.class);
		final Encoder dateEncoder = EasyMock.createMock(Encoder.class);

		encoders = new Encoder[] { stringEncoder, integerEncoder, dateEncoder };

		TestModule.setEncoders(encoders);

		classes = new Class[] { String.class, Integer.class, Date.class };

		TestModule.setClasses(classes);

		RegistryBuilder builder = new RegistryBuilder();
		builder.add(TestModule.class, TapestryCrudModule.class);
		registry = builder.build();

		// just to test if they deal correctly with inheritence.
		classes = new Class[] { String.class, Integer.class, Timestamp.class };

	}

//	@todo test commented out because the ValueEncoderSource service is not provided anymore 
//	@Test
//	@SuppressWarnings("unchecked")
//	public void contributeValueEncoderFactory() {
//
//		ValueEncoderSource valueEncoderSource = registry.getService(ValueEncoderSource.class);
//
//		for (int i = 0; i < classes.length; i++) {
//
//			final Class clasz = classes[i];
//			final Encoder encoder = encoders[i];
//			final String message = clasz.getSimpleName() + " : "
//					+ encoder.getClass().getSimpleName();
//
//			assert encoders[i] == valueEncoderSource.getValueEncoder(classes[i]) : message;
//
//		}
//
//	}

	@Test
	@SuppressWarnings("unchecked")
	public void testEncoderSource() {

		EncoderSource encoderSource = registry.getService(EncoderSource.class);

		for (int i = 0; i < classes.length; i++) {

			final Class clasz = classes[i];
			final Encoder encoder = encoders[i];
			final String message = clasz.getSimpleName() + " : "
					+ encoder.getClass().getSimpleName();

			assert encoders[i] == encoderSource.get(classes[i]) : message;

		}

	}

	@Test
	@SuppressWarnings("unchecked")
	public void testControllerSource() {

		ControllerSource controllerSource = registry.getService(ControllerSource.class);

		for (int i = 0; i < classes.length; i++) {

			final Class clasz = classes[i];
			final Controller controller = controllers[i];
			final String message = clasz.getSimpleName() + " : "
					+ controller.getClass().getSimpleName();

			assert controllers[i] == controllerSource.get(classes[i]) : message;

		}

	}

	@Test
	@SuppressWarnings("unchecked")
	public void testActivationEncoderSource() {

		ActivationContextEncoderSource aceSource = registry.getService(ActivationContextEncoderSource.class);

		for (int i = 0; i < classes.length; i++) {

			final Class clasz = classes[i];
			final ActivationContextEncoder encoder = encoders[i];
			final String message = clasz.getSimpleName() + " : "
					+ encoder.getClass().getSimpleName();

			assert encoders[i] == aceSource.get(classes[i]) : message;

		}

	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void testLabelEncoderSource() {

		LabelEncoderSource leSource = registry.getService(LabelEncoderSource.class);

		for (int i = 0; i < classes.length; i++) {

			final Class clasz = classes[i];
			final LabelEncoder encoder = encoders[i];
			final String message = clasz.getSimpleName() + " : "
					+ encoder.getClass().getSimpleName();

			final LabelEncoder labelEncoder = leSource.get(classes[i]);
			assert encoders[i] == labelEncoder : message;

		}

	}
	
//  @todo implement SelectModelFactory tests.
//	@Test
//	@SuppressWarnings("unchecked")
//	public void testSelectModelFactory() {
//
//		SelectModelFactory encoderSource = registry.getService(SelectModelFactory.class);
//
//		for (int i = 0; i < classes.length; i++) {
//
//			final Class clasz = classes[i];
//			
//			
//
//		}

//	}

}
