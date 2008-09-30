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

import java.util.List;

import org.apache.tapestry5.SelectModel;

/**
 * Interface that defines a factory of {@link SelectModel} instances for any given type, provided it
 * has a configured {@link SingleTypeSelectModelFactory} for it.
 * 
 * @author Thiago H. de Paula Figueiredo
 * @see SelectModel
 */
public interface SelectModelFactory {

	/**
	 * Creates a {@link SelectModel} containing all the instances of a given type.
	 * 
	 * @param clasz the {@link Class} that represents the wanted type. It cannot be
	 * <code>null</code>.
	 * @return a {@link SelectModel}.
	 */
	SelectModel create(Class<?> clasz);

	/**
	 * Creates a {@link SelectModel} containing some given instances of a given type.
	 * 
	 * @param clasz the {@link Class} that represents the wanted type. It cannot be
	 * <code>null</code>.
	 * @param objects a {@link List} containing the objects used as options.
	 */
	<T> SelectModel create(Class<T> clasz, List<T> objects);

}
