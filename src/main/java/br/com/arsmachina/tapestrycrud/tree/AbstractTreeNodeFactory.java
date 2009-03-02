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

/**
 * A partial implementation of {@link TreeNodeFactory}.
 * 
 * @param <T> the class related to this factory.
 * @author Thiago H. de Paula Figueiredo
 */
public abstract class AbstractTreeNodeFactory<T> implements TreeNodeFactory<T> {

	final protected List<TreeNode<T>> EMPTY_LIST =
		Collections.unmodifiableList(new ArrayList<TreeNode<T>>());

	final Class<T> type;

	/**
	 * Single constructor of this class.
	 */
	@SuppressWarnings("unchecked")
	protected AbstractTreeNodeFactory() {

		final Type genericSuperclass = getClass().getGenericSuperclass();
		final ParameterizedType parameterizedType =
			((ParameterizedType) genericSuperclass);
		type = (Class<T>) parameterizedType.getActualTypeArguments()[0];

	}

	public TreeNode<T> build(T object) {
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

}
