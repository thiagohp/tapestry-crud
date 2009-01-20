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

package br.com.arsmachina.tapestrycrud.selectmodel;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.SelectModel;

import br.com.arsmachina.controller.Controller;
import br.com.arsmachina.tapestrycrud.encoder.LabelEncoder;

/**
 * Default {@link SingleTypeSelectModelFactory} implementation for entity classes. It uses
 * {@link Controller#}
 * 
 * @author Thiago H. de Paula Figueiredo
 * @param <T> the type related to this factory.
 */
public class DefaultSingleTypeSelectModelFactory<T> implements SingleTypeSelectModelFactory<T> {

	private Controller<T, ?> controller;

	private LabelEncoder<T> labelEncoder;

	/**
	 * Single constructor of this class.
	 * 
	 * @param controller a {@link Controller}. It cannot be null.
	 * @param encoder a {@link LabelEncoder}. It cannot be null.
	 */
	public DefaultSingleTypeSelectModelFactory(Controller<T, ?> controller,
			LabelEncoder<T> labelEncoder) {

		if (controller == null) {
			throw new IllegalArgumentException("Parameter controller cannot be null");
		}

		if (labelEncoder == null) {
			throw new IllegalArgumentException("Parameter labelEncoder cannot be null");
		}

		this.controller = controller;
		this.labelEncoder = labelEncoder;

	}

	public SelectModel create(List<T> objects) {

		if (objects == null) {
			objects = controller.findAll();
		}

		List<OptionModel> options = new ArrayList<OptionModel>();

		for (T object : objects) {

			final String label = labelEncoder.toLabel(object);
			options.add(new SimpleOptionModel(object, label));

		}

		return new SimpleSelectModel(options);

	}

}
