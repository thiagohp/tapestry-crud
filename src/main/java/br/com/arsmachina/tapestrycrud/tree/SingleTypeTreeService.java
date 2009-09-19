// Copyright 2008-2009 Thiago H. de Paula Figueiredo
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
package br.com.arsmachina.tapestrycrud.tree;

import java.util.List;

/**
 * A tree service for a given type.
 * 
 * @param <T> the class related to this factory.
 * @author Thiago H. de Paula Figueiredo
 */
public interface SingleTypeTreeService<T> {

	/**
	 * Builds a tree node for a given object.
	 * 
	 * @param object a <code>T</code> instance. It cannot be null.
	 * @return a {@link TreeNode}. It cannot be null.
	 */
	TreeNode<T> buildTreeNode(T object);

	/**
	 * Searches the tree node of a given object in a tree (node).  
	 * 
	 * @param object a <code>T</code> instance. It cannot be null.
	 * @param treeNode a {@link TreeNode} of <code>T</code>. It cannot be null.
	 * @return a {@link TreeNode} or null.
	 */
	TreeNode<T> find(T object, TreeNode<T> treeNode);

	/**
	 * Convenience method to searches the tree node of a given object in a list of tree nodes. 
	 * 
	 * @param object a <code>T</code> instance. It cannot be null.
	 * @param treeNode a {@link List} of {@link TreeNode} of <code>T</code>. It cannot be null.
	 * @return a {@link TreeNode} or null.
	 */
	TreeNode<T> find(T object, List<TreeNode<T>> treeNodes);

	/**
	 * Tells if a given object is a root node.
	 * 
	 * @param object a <code>T</code>.
	 * @return a<code>boolean</code>. It cannot be null.
	 */
	boolean isRoot(T object);
	
	/**
	 * Returns the parent of a given object.
	 * 
	 * @param object a <code>T</code>. It cannot be null.
	 * @return a <code>T</code> or null.
	 */
	T getParent(T object);
	
	/**
	 * Sorts a list in tree order.
	 * 
	 * @param objects a {@link List} of <code>T</code>. It cannot be null.
	 */
	void treeOrder(List<T> objects);
	
	/**
	 * Builds a {@link TreeSelectNode} for a given {@link TreeNode}.
	 * 
	 * @param node a {@link TreeNode}. It cannot be null.
	 * @return {@link TreeSelectNode}.
	 */
	TreeSelectNode buildTreeSelectNode(TreeNode<T> node);
	
	/**
	 * Convenience method to create a list of {@link TreeSelectNode}s out of a list of 
	 * {@link TreeNode}s. 
	 * 
	 * @param node a {@link List} of  {@link TreeNode}s. It cannot be null.
	 * @return a {@link List} of {@link TreeSelectNode}.
	 */
	List<TreeSelectNode> buildTreeSelectNodeListFromTreeNodes(List<TreeNode<T>> nodes);

	/**
	 * Convenience method to create a list of {@link TreeNode}s out of a list of 
	 * <code>T</code> instances. 
	 * 
	 * @param node a {@link TreeNode}. It cannot be null.
	 * @return a {@link List} of <code>T</code>.
	 */
	List<TreeNode<T>> buildTreeNodeList(List<T> objects);

	/**
	 * Convenience method to create a list of {@link TreeSelectNode}s out of a list of 
	 * <code>T</code> instances. 
	 * 
	 * @param node a {@link List} of <code>T</code>. It cannot be null.
	 * @return a {@link List} of {@link TreeSelectNode}.
	 */
	List<TreeSelectNode> buildTreeSelectNodeList(List<T> objects);

}
