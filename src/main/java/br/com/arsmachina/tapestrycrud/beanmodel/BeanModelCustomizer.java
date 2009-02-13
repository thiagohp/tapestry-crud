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

package br.com.arsmachina.tapestrycrud.beanmodel;

import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.corelib.components.BeanEditForm;
import org.apache.tapestry5.corelib.components.BeanEditor;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.services.BeanModelSource;

/**
 * <p>
 * Interface that defines a {@link BeanModel} customizer for one given class.
 * This is meant to be used as a single place to customize bean models, instead
 * of changing it programatically or through the <code>add</code>,
 * <code>exclude</code>, and <code>include</code> methods of the {@link Grid},
 * {@link BeanEditForm}, and {@link BeanEditor} components.
 * </p>
 * <p>
 * All {@link BeanModel}s created by {@link BeanModelSource} are passed to
 * corresponding instances of this interface before they can be used. Each
 * method in this class This method can return the received model or even return
 * a completely different one. When a display model is created, it is passed to
 * {@link #customizeModel(BeanModel)} and then to
 * {@link #customizeDisplayModel(BeanModel)}. When an edit model is created, it
 * is passed to {@link #customizeModel(BeanModel)} and then to
 * {@link #customizeEditModel(BeanModel)}
 * </p>
 * 
 * @param <T> the type whose {@link BeanModel}s are being customized by this
 *            object.
 * @author Thiago H. de Paula Figueiredo
 */
public interface BeanModelCustomizer<T> {

	/**
	 * Customizes a model created through {@link BeanModelSource}, regardless of
	 * the method used. This method is intended to make changes that are common
	 * to both display and edit models and it is invoked before the
	 * {@link #customizeDisplayModel(BeanModel)} and
	 * {@link #customizeEditModel(BeanModel)}.
	 * 
	 * @param model a {@link BeanModel}.
	 * @return a {@link BeanModel}. It cannot be null.
	 */
	BeanModel<T> customizeModel(BeanModel<T> model);

	/**
	 * Customizes a model for edition.
	 * 
	 * @param model a {@link BeanModel}.
	 * @return a {@link BeanModel}. It cannot be null.
	 */
	BeanModel<T> customizeEditModel(BeanModel<T> model);

	/**
	 * Customizes a model for viewing.
	 * 
	 * @param model a {@link BeanModel}.
	 * @return a {@link BeanModel}. It cannot be null.
	 */
	BeanModel<T> customizeDisplayModel(BeanModel<T> model);

}
