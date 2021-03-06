// Copyright 2008-2009 Thiago H. de Paula Figueiredo
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

import br.com.arsmachina.tapestrycrud.encoder.ActivationContextEncoder;


/**
 * Service that provides {@link ActivationContextEncoder}s.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public interface ActivationContextEncoderSource {

	/**
	 * Returns the {@link ActivationContextEncoder} of a given type.
	 * 
	 * @param <T> a type.
	 * @param clasz a {@link Class}.
	 * @return an {@link ActivationContextEncoder}.
	 */
	<T> ActivationContextEncoder<T> get(Class<T> clasz);

}
