// Copyright 2009 Thiago H. de Paula Figueiredo
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

package br.com.arsmachina.tapestrycrud.services;

import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.services.BeanModelSource;

import br.com.arsmachina.tapestrycrud.beanmodel.BeanModelCustomizer;

/**
 * Internal service that provides {@link BeanModelCustomizer}. It is then used
 * to applies customizations to bean models created by {@link BeanModelSource}
 * by decorating it.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public interface BeanModelCustomizerSource {

	/**
	 * Returns the bean model customizer for a given type.
	 * 
	 * @param clasz a {@link Class} instance.
	 * @param <T> the type whose {@link BeanModel} is being customized.
	 * @return a {@link BeanModelCustomizer}.
	 */
	<T> BeanModelCustomizer<T> get(Class<T> clasz);

}
