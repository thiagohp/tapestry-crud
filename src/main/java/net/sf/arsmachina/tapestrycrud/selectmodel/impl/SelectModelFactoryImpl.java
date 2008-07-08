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

package net.sf.arsmachina.tapestrycrud.selectmodel.impl;

import java.util.List;
import java.util.Map;

import net.sf.arsmachina.tapestrycrud.selectmodel.SelectModelFactory;
import net.sf.arsmachina.tapestrycrud.selectmodel.SingleTypeSelectModelFactory;

import org.apache.tapestry5.Link;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ioc.util.StrategyRegistry;

/**
 * Default {@link SelectModelFactory} implementation. It delegates all its methods for
 * {@link SelectModelFactory} instances.
 * 
 * @author Thiago H. de Paula Figueiredo (ThiagoHP)
 * @see SelectModel
 * @see SingleTypeSelectModelFactory
 */
public class SelectModelFactoryImpl implements SelectModelFactory {

	@SuppressWarnings("unchecked")
	final private org.apache.tapestry5.ioc.util.StrategyRegistry<SingleTypeSelectModelFactory> registry;

	/**
	 * Single constructor.
	 * 
	 * @param registrations a {@link Map}&lt;{@link Class}, {@link Link}{@link SingleTypeSelectModelFactory}&gt;.
	 */
	@SuppressWarnings("unchecked")
	public SelectModelFactoryImpl(Map<Class, SingleTypeSelectModelFactory> registrations) {
		registry = StrategyRegistry.newInstance(SingleTypeSelectModelFactory.class, registrations);
	}

	/**
	 * @see net.sf.arsmachina.tapestrycrud.selectmodel.SelectModelFactory#create(java.lang.Class)
	 */
	public SelectModel create(Class<?> clasz) {
		return create(clasz, null);
	}

	/**
	 * @see net.sf.arsmachina.tapestrycrud.selectmodel.SelectModelFactory#create(java.lang.Class,
	 * java.util.List)
	 */
	public <T> SelectModel create(Class<T> clasz, List<T> objects) {
		return get(clasz).create(objects);
	}

	@SuppressWarnings("unchecked")
	private <T> SingleTypeSelectModelFactory<T> get(Class<T> clasz) {
		return registry.get(clasz);
	}

}
