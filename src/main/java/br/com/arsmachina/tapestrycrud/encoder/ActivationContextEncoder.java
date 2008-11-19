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

package br.com.arsmachina.tapestrycrud.encoder;

import org.apache.tapestry5.EventContext;

/**
 * Interface that encapsulates the Tapestry 5 activation context logic for objects of a given type.
 * 
 * @author Thiago H. de Paula Figueiredo
 * @param <T> the entity class related to this encoder.
 */
public interface ActivationContextEncoder<T> {

	/**
	 * Given an object, returns its activation context value.
	 * 
	 * @param object an {@link EventContext}.
	 * @return an {@link Object}. It must be null if <code>object</code> is null.
	 */
	public Object toActivationContext(T object);

	/**
	 * Given an activation context value, returns its object.
	 * 
	 * @param value an {@link EventContext}.
	 * @return a {@link T}.
	 */
	public T toObject(EventContext value);

}
