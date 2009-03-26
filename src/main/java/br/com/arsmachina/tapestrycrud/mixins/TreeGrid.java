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

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.MixinAfter;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.dom.Node;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.SortConstraint;
import org.apache.tapestry5.ioc.annotations.Inject;

import br.com.arsmachina.tapestrycrud.Constants;
import br.com.arsmachina.tapestrycrud.services.TreeServiceSource;
import br.com.arsmachina.tapestrycrud.tree.SingleTypeTreeService;
import br.com.arsmachina.tapestrycrud.tree.TreeNode;

/**
 * Mixin that provides a tree table-like functionality to {@link Grid}s.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
@MixinAfter
@IncludeStylesheet(Constants.TAPESTRY_CRUD_CSS_ASSET)
public class TreeGrid {

	/**
	 * {@link SingleTypeTreeService} corresponding to the type of the objects in the
	 * grid. If not provided, it attempts to get one from the
	 * {@link TreeServiceSource} service.
	 */
	@Parameter(allowNull = false)
	@SuppressWarnings("unchecked")
	private SingleTypeTreeService treeService;
	
	@Parameter(defaultPrefix = BindingConstants.ASSET, value = Constants.NO_CHILDREN_NODE_IMAGE)
	private Asset noChildrenIcon;

	@Parameter(defaultPrefix = BindingConstants.ASSET, value = Constants.HAS_CHILDREN_NODE_IMAGE)
	private Asset hasChildrenIcon;

	@InjectContainer
	private Grid grid;

	@Inject
	private TreeServiceSource treeServiceSource;

	/**
	 * Default value for the <code>treeService</code> parameter.
	 * 
	 * @return a {@link SingleTypeTreeService}.
	 */
	@SuppressWarnings("unchecked")
	SingleTypeTreeService<?> defaultTreeService() {

		final Class beanType = grid.getDataModel().getBeanType();
		return treeServiceSource.get(beanType);

	}
	
	private List<Element> childElements(Element element) {
		
		List<Element> elements = new ArrayList<Element>();
		
		for (Node node : element.getChildren()) {
			
			if (node instanceof Element) {
				elements.add((Element) node);
			}
			
		}
		
		return elements;
		
	}

	/**
	 * Generates the JavaScript code that will add CSS classes to the grid's
	 * rows.
	 */
	@AfterRender
	@SuppressWarnings("unchecked")
	void rewriteDOM(MarkupWriter writer) {

		if (treeService == null) {
			throw new IllegalArgumentException("No SingleTypeTreeService found");
		}
		
		final List<SortConstraint> sortConstraints = grid.getSortModel().getSortConstraints();
		
		// we don't change grids that are not sorted in tree order. 
		if (sortConstraints.isEmpty()) {
		
			final Element outerDiv = writer.getElement();
			final Element div = childElements(outerDiv).get(0);
			final Element table = childElements(div).get(0);
			final Element tbody = childElements(table).get(1);
			final List<Element> rows = childElements(tbody);

			final GridDataSource dataSource = grid.getDataSource();
			int rowNumber = dataSource.getAvailableRows();
			List<Object> objects = new ArrayList<Object>();
			
			for (int i = 0; i < rowNumber; i++) {
				objects.add(dataSource.getRowValue(i));
			}
			
			List<TreeNode<?>> treeNodes = treeService.buildTreeNodeList(objects);
			
			for (int i = 0; i < rowNumber; i++) {
				
				Object object = dataSource.getRowValue(i);
				Element tr = rows.get(i);
				Element firstTd = childElements(tr).get(0);
				
				final TreeNode node = treeService.find(object, treeNodes);
				String level = "level" + node.getLevel();
				
				firstTd.addClassName(level);
				
				Asset asset = node.getChildren().size() == 0 ? noChildrenIcon : hasChildrenIcon;
				firstTd.elementAt(0, "img", "src", asset.toClientURL());
				
			}
			
		}

	}

}
