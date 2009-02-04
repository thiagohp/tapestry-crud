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

package br.com.arsmachina.tapestrycrud.services;

import org.apache.tapestry5.ioc.Messages;

/**
 * Utility service that provides some useful methods related pages in Tapestry CRUD.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public interface PageUtil {
	
	/**
	 * Returns the requested page in URL form.
	 * 
	 * @return a {@link String}.
	 */
	String getRequestedPageURL();
	
	/**
	 * Returns the internationalized name of page, given its Tapestry URL 
	 * (something like <code>admin/user/list</code>). 
	 * 
	 * @param pageURL a {@link String}. It cannot be null.
	 * @param a {@link Messages}. It cannot be null.
	 * @return a {@link String}.
	 */
	String getPageTitle(String pageURL, Messages messages);
	
	/**
	 * Returns the internationalized name the requested page. 
	 * 
	 * @param a {@link Messages}. It cannot be null.
	 * @return a {@link String}.
	 */
	String getRequestedPageTitle(Messages messages);
	
}
