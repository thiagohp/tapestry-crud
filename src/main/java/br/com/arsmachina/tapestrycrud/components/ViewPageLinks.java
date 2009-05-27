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
package br.com.arsmachina.tapestrycrud.components;

import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;

/**
 * Simple component that just aggregates the edit object, remove object, new
 * object and back to listing components.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public class ViewPageLinks {

	/**
	 * Show the remove link?
	 */
	@Parameter(value = "true")
	@Property
	@SuppressWarnings("unused")
	private boolean remove;

	/**
	 * Show the edit link?
	 */
	@Parameter(value = "true")
	@Property
	@SuppressWarnings("unused")
	private boolean edit;

	/**
	 * Show the list link?
	 */
	@Parameter(value = "true")
	@Property
	@SuppressWarnings("unused")
	private boolean list;

	/**
	 * Show the new object link?
	 */
	@Parameter(value = "true")
	@Property
	@SuppressWarnings("unused")
	private boolean newObject;

}
