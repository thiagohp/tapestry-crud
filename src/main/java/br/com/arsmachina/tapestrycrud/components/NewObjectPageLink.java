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

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.corelib.components.Label;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.runtime.Component;

import br.com.arsmachina.tapestrycrud.base.AbstractNewObjectLink;
import br.com.arsmachina.tapestrycrud.base.BaseEditPage;
import br.com.arsmachina.tapestrycrud.base.BaseListPage;
import br.com.arsmachina.tapestrycrud.services.TapestryCrudModuleService;

/**
 * <p>
 * Component that creates a link to the corresponding edition page (a {@link BaseEditPage} instance,
 * typically). It must be used inside pages that subclass {@link BaseListPage}.
 * </p>
 * <p>
 * The code of this class is largely adapted from Tapestry's {@link Label}.
 * </p>
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public class NewObjectPageLink extends AbstractNewObjectLink {

	@Inject
	private TapestryCrudModuleService tapestryCrudModuleService;
	
	@Inject
	private ComponentResources resources;

	@SuppressWarnings("unchecked")
	protected Link createLink() {
		
		Component page = resources.getPage();

		if (page instanceof BaseListPage == false) {

			throw new RuntimeException("The NewObjectPageLink must be used inside a page "
					+ "that subclasses BaseListPage");

		}

		BaseListPage listPage = (BaseListPage) page;
		Class entityClass = listPage.getEntityClass();
		String editPageURL = tapestryCrudModuleService.getEditPageURL(entityClass);

		return getLinkFactory().createPageRenderLink(editPageURL, true);

	}

}
