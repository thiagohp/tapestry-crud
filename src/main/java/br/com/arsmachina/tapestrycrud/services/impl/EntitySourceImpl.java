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

package br.com.arsmachina.tapestrycrud.services.impl;

import java.util.Collection;
import java.util.Set;

import br.com.arsmachina.tapestrycrud.services.EntitySource;

/**
 * Default {@link EntitySource} implementation.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public class EntitySourceImpl implements EntitySource {

	final private Set<Class<?>> entityClasses;

	/**
	 * Single constructor of this class.
	 * 
	 * @param entityClasses a {@link Collection} of {@link Class} instances. It cannot be null.
	 */
	public EntitySourceImpl(Set<Class<?>> entityClasses) {

		if (entityClasses == null) {
			throw new IllegalArgumentException("Parameter entityClasses cannot be null");
		}

		this.entityClasses = entityClasses;

	}

	public Set<Class<?>> getEntityClasses() {
		return entityClasses;
	}

}
