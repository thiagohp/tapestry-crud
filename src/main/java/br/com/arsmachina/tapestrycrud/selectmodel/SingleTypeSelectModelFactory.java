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
 * Interface that defines a factory of {@link SelectModel} instances for a given type.
 * 
 * @author Thiago H. de Paula Figueiredo
 * @param <T> the type related to this factory.
 * @see SelectModel
 * @see SingleTypeSelectModelFactory
 */
public interface SingleTypeSelectModelFactory<T> {

	/**
	 * Creates a {@link SelectModel} with given options.
	 * 
	 * @param objects a {@link List} containing the objects used as options. It it is
	 * <code>null</code>, it means that all instances must be used as options.
	 * @return a {@link SelectModel}.
	 */
	SelectModel create(List<T> objects);

}
