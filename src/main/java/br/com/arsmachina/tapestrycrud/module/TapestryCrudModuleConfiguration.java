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

package br.com.arsmachina.tapestrycrud.module;

/**
 * Class that encapsulates the configurations of a {@link TapestryCrudModule}.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public class TapestryCrudModuleConfiguration {

	private String editPage = "edit";

	private String listPage = "list";

	private String viewPage = "view";

	/**
	 * Returns the value of the <code>editPage</code> property.
	 * 
	 * @return a {@link String}.
	 */
	public String getEditPage() {
		return editPage;
	}

	/**
	 * Changes the value of the <code>editPage</code> property.
	 * 
	 * @param editPage a {@link String}.
	 */
	public void setEditPage(String editPage) {
		this.editPage = editPage;
	}

	/**
	 * Returns the value of the <code>listPage</code> property.
	 * 
	 * @return a {@link String}.
	 */
	public String getListPage() {
		return listPage;
	}

	/**
	 * Changes the value of the <code>listPage</code> property.
	 * 
	 * @param listPage a {@link String}.
	 */
	public void setListPage(String listPage) {
		this.listPage = listPage;
	}

	/**
	 * Returns the value of the <code>viewPage</code> property.
	 * 
	 * @return a {@link String}.
	 */
	public String getViewPage() {
		return viewPage;
	}

	/**
	 * Changes the value of the <code>viewPage</code> property.
	 * 
	 * @param viewPage a {@link String}.
	 */
	public void setViewPage(String viewPage) {
		this.viewPage = viewPage;
	}

}
