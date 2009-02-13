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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;

import org.apache.tapestry5.beaneditor.BeanModel;

/**
 * {@link BeanModelCustomizer} implementation which has all methods just
 * returning the received {@link BeanModel} parameter unchanged. It is meant to
 * be used by bean model customizer implementations that only need to implement
 * one or two of the {@link BeanModelCustomizer} methods, something like
 * {@link MouseAdapter} is to {@link MouseListener}.
 * 
 * * @param <T> the type whose {@link BeanModel}s are being customized by this
 * object.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public abstract class AbstractBeanModelCustomizer<T> implements
		BeanModelCustomizer<T> {

	/**
	 * Returns <code>model</code>.
	 * 
	 * @return <code>model</code>.
	 * @see BeanModelCustomizer#customizeDisplayModel(BeanModel)
	 */
	public BeanModel<T> customizeDisplayModel(BeanModel<T> model) {
		return model;
	}

	/**
	 * Returns <code>model</code>.
	 * 
	 * @return <code>model</code>.
	 * @see BeanModelCustomizer#customizeEditModel(BeanModel)
	 */
	public BeanModel<T> customizeEditModel(BeanModel<T> model) {
		return model;
	}

	/**
	 * Returns <code>model</code>.
	 *
	 * @return <code>model</code>.
	 * @see BeanModelCustomizer#customizeModel(BeanModel)
	 */
	public BeanModel<T> customizeModel(BeanModel<T> model) {
		return model;
	}

}
