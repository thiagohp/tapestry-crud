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
package br.com.arsmachina.tapestrycrud.tree;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.arsmachina.tapestrycrud.encoder.LabelEncoder;
import br.com.arsmachina.tapestrycrud.services.LabelEncoderSource;

/**
 * A partial implementation of {@link SingleTypeTreeService}.
 * 
 * @param <T> the class related to this factory.
 * @author Thiago H. de Paula Figueiredo
 */
public abstract class AbstractSingleTypeTreeService<T> implements
		SingleTypeTreeService<T> {

	final protected List<TreeNode<T>> EMPTY_LIST =
		Collections.unmodifiableList(new ArrayList<TreeNode<T>>());

	final Class<T> type;

	final LabelEncoder<T> labelEncoder;

	/**
	 * Single constructor of this class.
	 */
	@SuppressWarnings("unchecked")
	protected AbstractSingleTypeTreeService(
			LabelEncoderSource labelEncoderSource) {

		final Type genericSuperclass = getClass().getGenericSuperclass();
		final ParameterizedType parameterizedType =
			((ParameterizedType) genericSuperclass);
		type = (Class<T>) parameterizedType.getActualTypeArguments()[0];

		if (labelEncoderSource == null) {
			throw new IllegalArgumentException(
					"O parâmetro labelEncoderSource não pode ser nulo");
		}

		labelEncoder = labelEncoderSource.get(type);

	}

	public TreeNode<T> buildTreeNode(T object) {
		return build(object, 1);
	}

	/**
	 * Builds a {@link TreeNode} for a given object at a given level.
	 * 
	 * @param object a <code>T</code> instance.
	 * @param level an <code>int</code>.
	 * @return a {@link TreeNode}.
	 */
	protected TreeNode<T> build(T object, int level) {

		SimpleTreeNode<T> treeNode = new SimpleTreeNode<T>(object, level, type);
		List<T> children = getChildren(object);

		if (children != null) {

			for (T child : children) {
				treeNode.add(build(child, level + 1));
			}

		}

		return treeNode;

	}

	/**
	 * Returns the list of the children of a given object.
	 * 
	 * @param object a <code>T</code> instance.
	 * @return a {@link List} of <code>T</code>.
	 */
	protected abstract List<T> getChildren(T object);

	public void treeOrder(List<T> objects) {

		List<T> list = new ArrayList<T>();

		for (T object : objects) {

			if (isRoot(object)) {
				add(object, list);
			}

		}

		objects.clear();
		objects.addAll(list);

	}

	/**
	 * Adds an object and its children to a list.
	 * 
	 * @param object a <code>T</code>.
	 * @param list a {@link List} of <code>T</code>.
	 */
	void add(T object, List<T> list) {

		list.add(object);

		for (T child : getChildren(object)) {
			add(child, list);
		}

	}

	public TreeSelectNode buildTreeSelectNode(TreeNode<T> node) {

		final List<TreeSelectNode> childrenTSN =
			new ArrayList<TreeSelectNode>();

		final T object = node.getObject();
		final String label = labelEncoder.toLabel(object);
		final SimpleTreeSelectNode treeSelectNode =
			new SimpleTreeSelectNode(object, childrenTSN, label);

		final List<TreeNode<T>> children = node.getChildren();

		for (TreeNode<T> childNode : children) {
			childrenTSN.add(buildTreeSelectNode(childNode));
		}

		return treeSelectNode;

	}

	public List<TreeSelectNode> buildTreeSelectNodeListFromTreeNodes(
			List<TreeNode<T>> nodes) {

		final List<TreeSelectNode> childrenTSN =
			new ArrayList<TreeSelectNode>(nodes.size());

		for (TreeNode<T> node : nodes) {
			
			if (isRoot(node.getObject())) {
				childrenTSN.add(buildTreeSelectNode(node));
			}
			
		}

		return childrenTSN;

	}

	public List<TreeNode<T>> buildTreeNodeList(List<T> objects) {

		final List<TreeNode<T>> nodes =
			new ArrayList<TreeNode<T>>(objects.size());

		for (T object : objects) {
			nodes.add(buildTreeNode(object));
		}

		return nodes;

	}

	public List<TreeSelectNode> buildTreeSelectNodeList(List<T> objects) {
		return buildTreeSelectNodeListFromTreeNodes(buildTreeNodeList(objects));
	}

}
