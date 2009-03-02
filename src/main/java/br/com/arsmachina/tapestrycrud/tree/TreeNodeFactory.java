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

/**
 * A factory of nodes of a given type.
 * 
 * @param <T> the class related to this factory.
 * @author Thiago H. de Paula Figueiredo
 */
public interface TreeNodeFactory<T> {

	/**
	 * Builds a tree node for a given object
	 * 
	 * @param object a <code>T</code> instance. It cannot be null.
	 * @return a {@link TreeNode}. It cannot be null.
	 */
	TreeNode<T> build(T object);

	/**
	 * Tells if a given object is a root node.
	 * 
	 * @param object a <code>T</code>.
	 * @return a<code>boolean</code>.
	 */
	boolean isRoot(T object);

}
