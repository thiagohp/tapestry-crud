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
import org.apache.tapestry5.annotations.BeforeRenderBody;
import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.annotations.Parameter;

import br.com.arsmachina.tapestrycrud.Constants;

/**
 * Component that only shows a message if a given text is not null and not empty.
 * It puts the message inside a <code>div</code> with class <code>t-crud-emptygrid</code>.
 * 
 * @author Thiago H. de Paula Figueiredo (ThiagoHP)
 */
@IncludeStylesheet(Constants.TAPESTRY_CRUD_CSS_ASSET)
public class EmptyGridMessage {

	/**
	 * Key of the empty grid message.
	 */
	public static final String MESSAGE_GRID_EMPTY = "message.grid.empty";

	/**
	 * Generated <code>&lt;p&gt;</code> CSS class.
	 */
	public static final String CSS_CLASS = "t-crud-emptygrid";

	@Parameter(defaultPrefix = BindingConstants.MESSAGE, value = MESSAGE_GRID_EMPTY)
	private String message;

	/**
	 * Returns the value of the <code>message</code> property.
	 * 
	 * @return a {@link String}.
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Changes the value of the <code>message</code> property.
	 * 
	 * @param message a {@link String}.
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	/**
	 * Discards the tag body.
	 * @return <code>false</code>.
	 */
	@BeforeRenderBody
	public boolean discardBody() {
		return false;
	}

}
