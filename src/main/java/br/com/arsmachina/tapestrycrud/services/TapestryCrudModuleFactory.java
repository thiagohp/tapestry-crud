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
package br.com.arsmachina.tapestrycrud.services;

import br.com.arsmachina.module.Module;
import br.com.arsmachina.tapestrycrud.module.TapestryCrudModule;


/**
 * Service that builds {@link TapestryCrudModule} from {@link Module} (from the Application Module
 * pacakge).
 *  
 * @author Thiago H. de Paula Figueiredo
 */
public interface TapestryCrudModuleFactory {

	/**
	 * Builds a {@link TapestryCrudModule} from a {@link Module}. This method can return null
	 * if this builder does not support the given module.
	 * 
	 * @param module a {@link Module}. It cannot be null.
	 * @return a {@link TapestryCrudModule} or null.
	 */
	TapestryCrudModule build(Module module);

}
