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

package br.com.arsmachina.tapestrycrud.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Grid;


/**
 * Component that renders the action links in a listing page.
 * It is meant to be used in a {@link Grid} column.
 * <a href="http://ars-machina.svn.sourceforge.net/viewvc/ars-machina/example/trunk/src/main/webapp/project/ListProject.tml?view=markup"
 * 		>Ars Machina Project Example</a>.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public class ActionLinks {
	
	/**
	 * Name of the page that is used to edits objects listed in  this page.
	 */
	@Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
	@Property
	@SuppressWarnings("unused")
	private String editPage;
	
	/**
	 * Show the edit link?
	 */
	@Parameter(value = "true")
	@Property
	@SuppressWarnings("unused")
	private boolean edit;

	/**
	 * Show the remove link?
	 */
	@Parameter(value = "true")
	@Property
	@SuppressWarnings("unused")
	private boolean remove;
	
	/**
	 * The object that the links will refer to.
	 */
	@Parameter(required = true)
	@Property
	@SuppressWarnings("unused")
	private Object object;
	
}
