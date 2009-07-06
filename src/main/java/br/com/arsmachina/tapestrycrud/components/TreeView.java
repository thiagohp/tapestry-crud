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

import java.util.List;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PageRenderLinkSource;

import br.com.arsmachina.tapestrycrud.Constants;
import br.com.arsmachina.tapestrycrud.encoder.ActivationContextEncoder;
import br.com.arsmachina.tapestrycrud.encoder.LabelEncoder;
import br.com.arsmachina.tapestrycrud.services.ActivationContextEncoderSource;
import br.com.arsmachina.tapestrycrud.services.LabelEncoderSource;
import br.com.arsmachina.tapestrycrud.services.TapestryCrudModuleService;
import br.com.arsmachina.tapestrycrud.tree.SingleTypeTreeService;
import br.com.arsmachina.tapestrycrud.tree.TreeNode;

/**
 * A component that shows a list of objects in a tree view.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
@SupportsInformalParameters
@IncludeStylesheet(Constants.TAPESTRY_CRUD_CSS_ASSET)
public class TreeView {

	/**
	 * CSS class applied to the root <code>&lt;ul&gt;</code> element.
	 */
	public static final String CSS_CLASS = "t-crud-treeview";

	/**
	 * The objects to be shown.
	 */
	@Parameter(required = true, allowNull = false)
	@Property
	@SuppressWarnings("unchecked")
	private List objects;

	@Parameter(required = true, allowNull = false)
	@Property
	@SuppressWarnings("unchecked")
	private SingleTypeTreeService treeService;

	/**
	 * Used to store the current object being rendered (for the current node).
	 */
	@Parameter
	@Property
	private Object node;

	// /**
	// * Block used to render the object label. If not provided, the corresponding {@link
	// LabelEncoder}
	// * will be used.
	// */
	// @Parameter(defaultPrefix = BindingConstants.LITERAL)
	// private Block block;

	@Inject
	private LabelEncoderSource labelEncoderSource;

	@Inject
	private PageRenderLinkSource pageRenderLinkSource;

	@Inject
	private TapestryCrudModuleService tapestryCrudModuleService;

	@Inject
	private ActivationContextEncoderSource activationContextEncoderSource;
	
	@Inject
	private ComponentResources resources;

	@SetupRender
	@SuppressWarnings("unchecked")
	public void render(MarkupWriter writer) {
		
		List<TreeNode> nodes = treeService.buildTreeNodeList(objects);
		
		if (nodes.isEmpty() == false) {

			writer.element("ul", "class", CSS_CLASS); // outer ul
			
			final String cssAttribute = resources.getInformalParameter("class", String.class);
			if (cssAttribute != null) {
				writer.getElement().addClassName(cssAttribute);
			}
			
			resources.renderInformalParameters(writer);

			for (TreeNode node : nodes) {
				if (treeService.isRoot(node.getObject())) {
					render(node, writer);
				}
			}

			writer.end(); // outer ul

		}

	}

	@SuppressWarnings("unchecked")
	private void render(TreeNode node, MarkupWriter writer) {

		this.node = node.getObject();

		final Class<?> nodeClass = this.node.getClass();
		final Class<?> viewPageClass = tapestryCrudModuleService.getViewPageClass(nodeClass);
		final ActivationContextEncoder ace = activationContextEncoderSource.get(nodeClass);
		final Object activationContext = ace.toActivationContext(this.node);

		writer.element("li");

		final Link link = pageRenderLinkSource.createPageRenderLinkWithContext(viewPageClass,
				activationContext);
		writer.element("a", "href", link);
		final LabelEncoder labelEncoder = labelEncoderSource.get(nodeClass);
		writer.write(labelEncoder.toLabel(this.node));
		
		writer.end(); // a

		if (node.getChildren().isEmpty() == false) {

			writer.element("ul"); // ul

			final List<TreeNode> children = node.getChildren();
			for (TreeNode treeNode : children) {
				render(treeNode, writer);
			}

			writer.end(); // ul

		}

		writer.end(); // li
	}

}
