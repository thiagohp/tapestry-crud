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

import java.util.ArrayList;
import java.util.List;

/**
 * Simple {@link TreeNode} implementation.
 * 
 * @param <T> the type of object behind this node.
 * @author Thiago H. de Paula Figueiredo
 * @todo comment methods
 */
public class SimpleTreeNode<T> implements TreeNode<T> {

	private List<TreeNode<T>> children;

	private int level;

	private Class<T> type;

	private T object;

	/**
	 * Single constructor of this class.
	 * 
	 * @para object a <code>T</code>.
	 * @param children a {@link List} of {@link TreeNode}s.
	 * @param level an <code>int</code> greater or equal to 1.
	 */
	public SimpleTreeNode(T object, int level, Class<T> type) {

		if (object == null) {
			throw new IllegalArgumentException(
					"Parameter object cannot be null");
		}

		if (level <= 0) {
			throw new IllegalArgumentException(
					"Parameter level must be greater or equal to 1");
		}

		if (type == null) {
			throw new IllegalArgumentException("Parameter type cannot be null");
		}

		this.level = level;
		this.object = object;

		children = new ArrayList<TreeNode<T>>();

	}

	public List<TreeNode<T>> getChildren() {
		return children;
	}

	public int getLevel() {
		return level;
	}

	public Class<T> getType() {
		return type;
	}

	public void add(TreeNode<T> treeNode) {
		children.add(treeNode);
	}

	public T getObject() {
		return object;
	}

	@Override
	public String toString() {
		return getObject().toString();
	}

}
