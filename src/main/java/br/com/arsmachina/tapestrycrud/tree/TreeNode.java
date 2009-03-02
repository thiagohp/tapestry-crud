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

import java.util.List;

/**
 * Interface that represents a tree node.
 * 
 * @param <T> the type of object behind this node.
 * @author Thiago H. de Paula Figueiredo
 */
public interface TreeNode<T> {

	/**
	 * Returns the list of children of this node
	 * 
	 * @return a {@link List} of {@link TreeNode}s.
	 */
	List<TreeNode<T>> getChildren();

	/**
	 * Returns the level of this node. Root nodes have level 1.
	 * 
	 * @return an <code>int</code>.
	 */
	int getLevel();

	/**
	 * Returns the type of the object represented by this node.
	 * 
	 * @return a {@link Class}.
	 */
	Class<T> getType();

	/**
	 * Returns the object that this node represents.
	 * 
	 * @return a <code>T</code>.
	 */
	T getObject();

}
