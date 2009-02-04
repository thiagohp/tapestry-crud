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

package br.com.arsmachina.tapestrycrud.services.impl;

import javax.servlet.http.HttpServletRequest;

import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.ComponentClassResolver;

import br.com.arsmachina.tapestrycrud.Constants;
import br.com.arsmachina.tapestrycrud.services.PageUtil;

/**
 * Default {@link PageUtil} implementation.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public class PageUtilImpl implements PageUtil {

	@Inject
	private HttpServletRequest request;

	@Inject
	private ComponentClassResolver componentClassResolver;

	public String getRequestedPageURL() {

		String pageName = "";
		String path = request.getPathInfo();

		if (path == null) {
			path = request.getServletPath();
		}

		path = path.trim();
		path = path.length() <= 1 ? "" : path;

		if (path.equals("") == false) {

			String extendedName = path.length() == 0 ? path : path.substring(1);

			// Copied and adapted from Tapestry's PageRenderDispatcher

			// Ignore trailing slashes in the path.
			while (extendedName.endsWith("/")) {
				extendedName = extendedName.substring(0, extendedName.length() - 1);
			}

			int slashx = extendedName.length();

			while (slashx > 0) {

				String page = extendedName.substring(0, slashx);

				if (componentClassResolver.isPageName(page)) {

					pageName = page;
					break;

				}

				// Work backwards, splitting at the next slash.
				slashx = extendedName.lastIndexOf('/', slashx - 1);

			}

		}

		return pageName;

	}

	public String getPageTitle(String pageURL, Messages messages) {

		final String string = pageURL.replace('/', '.');
		return messages.get(Constants.PAGE_TITLE_MESSAGE_PREFIX + string);

	}

	public String getRequestedPageTitle(Messages messages) {
		return getPageTitle(getRequestedPageURL(), messages);
	}

}
