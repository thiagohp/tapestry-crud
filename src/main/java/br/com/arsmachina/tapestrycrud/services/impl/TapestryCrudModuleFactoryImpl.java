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

import br.com.arsmachina.module.Module;
import br.com.arsmachina.tapestrycrud.module.DefaultTapestryCrudModule;
import br.com.arsmachina.tapestrycrud.module.TapestryCrudModule;
import br.com.arsmachina.tapestrycrud.services.TapestryCrudModuleFactory;

/**
 * Default {@link TapestryCrudModuleFactory} implementation.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public class TapestryCrudModuleFactoryImpl implements TapestryCrudModuleFactory {

	public TapestryCrudModule build(Module module) {
		return new DefaultTapestryCrudModule(module);
	}

}
