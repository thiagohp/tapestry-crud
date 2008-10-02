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

import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.BeforeRenderTemplate;
import org.apache.tapestry5.annotations.Parameter;

/**
 * Component that only shows a message if a given text is not null and not empty.
 * It puts the message inside a <code>div</code> with class <code>t-crud-message</code>.
 * One example can be found in
 * <a href="http://ars-machina.svn.sourceforge.net/viewvc/ars-machina/example/trunk/src/main/webapp/project/EditProject.tml?view=markup"
 * 		>Ars Machina Project Example</a>.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public class Message {

	/**
	 * Generated <code>&lt;div&gt;</code> CSS class. 
	 */
	private static final String CSS_CLASS = "t-crud-message";
	
	/**
	 * Message to be shown.
	 */
	@Parameter(required = true)
	private String message;
	
	@BeforeRenderTemplate
	public boolean render(MarkupWriter writer) {
		
		if (message != null && message.trim().length() > 0) {
			
			writer.element("div", "class", CSS_CLASS);
			
			writer.element("p");
			writer.write(message);
			writer.end(); // p
			
			writer.end(); // div
			
		}
		
		return false;
		
	}
	
	/**
	 * Returns the value of the <code>message</code> property.
	 * 
	 * @return a {@link String}.
	 */
	final public String getMessage() {
		return message;
	}

	/**
	 * Changes the value of the <code>message</code> property.
	 * 
	 * @param message a {@link String}.
	 */
	final public void setMessage(String message) {
		this.message = message;
	}

}
