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

package br.com.arsmachina.tapestrycrud.mixins;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.RenderSupport;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.MixinAfter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.ioc.annotations.Inject;

import br.com.arsmachina.tapestrycrud.Constants;
import br.com.arsmachina.tapestrycrud.services.TreeServiceSource;
import br.com.arsmachina.tapestrycrud.tree.TreeNode;
import br.com.arsmachina.tapestrycrud.tree.SingleTypeTreeService;

/**
 * Mixin that provides a tree table-like functionality to {@link Grid}s.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
@MixinAfter
@IncludeJavaScriptLibrary("classpath:/br/com/arsmachina/tapestrycrud/javascript/treegrid.js")
@IncludeStylesheet(Constants.TAPESTRY_CRUD_CSS_ASSET)
public class TreeGrid {

	/**
	 * {@link SingleTypeTreeService} corresponding to the type of the objects in the
	 * grid. If not provided, it attempts to get one from the
	 * {@link TreeServiceSource} service.
	 */
	@Parameter(allowNull = false)
	@SuppressWarnings("unchecked")
	private SingleTypeTreeService treeNodeFactory;

	@InjectContainer
	private Grid grid;

	@Inject
	private TreeServiceSource treeNodeFactorySource;

	@Inject
	private RenderSupport renderSupport;

	/**
	 * Default value for the <code>treeNodeFactory</code> paramenter.
	 * 
	 * @return a {@link SingleTypeTreeService}.
	 */
	@SuppressWarnings("unchecked")
	SingleTypeTreeService<?> defaultTreeNodeFactory() {

		final Class beanType = grid.getDataModel().getBeanType();
		return treeNodeFactorySource.get(beanType);

	}

	/**
	 * Generates the JavaScript code that will add CSS classes to the grid's
	 * rows.
	 */
	@AfterRender
	@SuppressWarnings("unchecked")
	void initializeJavascript(MarkupWriter writer) {

		if (treeNodeFactory == null) {
			throw new IllegalArgumentException("No TreeNodeFactory found");
		}

		int i = 0;

		final int currentPage = grid.getCurrentPage();
		final int rowsPerPage = grid.getRowsPerPage();
		final int availableRows = grid.getDataSource().getAvailableRows();

		final int first = (currentPage - 1) * rowsPerPage;
		final int last = Math.min(currentPage * rowsPerPage, availableRows);

		List objects = new ArrayList();

		for (int j = first; j < last; j++) {
			objects.add(grid.getDataSource().getRowValue(j));
		}

		List<TreeNode> nodes = new ArrayList<TreeNode>(objects.size());

		for (Object object : objects) {

			if (treeNodeFactory.isRoot(object)) {
				nodes.add(treeNodeFactory.buildTreeNode(object));
			}

		}

		final Element element = writer.getElement();
		String gridId = element.getAttribute("id");
		
		if (gridId == null) {
			gridId = "grid";
		}
		
		final String hashVariable = gridId + "_hash";

		renderSupport.addScript("var %s = new Hash();", hashVariable);

		for (Object object : objects) {

			TreeNode node = findNode(object, nodes);
			
			renderSupport.addScript("%s.set(%s, %s);", hashVariable, i,
					node.getLevel());
			i++;
			
		}

		renderSupport.addScript("TreeGrid.initialize('%s', %s);", gridId,
				hashVariable);

	}

	@SuppressWarnings("unchecked")
	private TreeNode findNode(Object object, List<TreeNode> nodes) {
		
		TreeNode returnedNode = null;
		
		for (TreeNode node : nodes) {
			
			if (node.getObject().equals(object)) {
				returnedNode = node;
			}
			else {
				returnedNode = findNode(object, node.getChildren());
			}
			
			if (returnedNode != null) {
				break;
			}
			
		}
		
		return returnedNode;
		
	}

}
