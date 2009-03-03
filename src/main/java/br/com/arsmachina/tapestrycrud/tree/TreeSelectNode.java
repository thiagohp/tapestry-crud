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

import org.apache.tapestry5.OptionModel;

/**
 * Interface that represents a tree node for the TreeSelect component.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public interface TreeSelectNode extends OptionModel {

	/**
	 * Returns the children nodes of this node.
	 * 
	 * @return a {@link List} of {@link TreeSelectNode}s. It cannot be null, but
	 *         can be empty.
	 */
	List<TreeSelectNode> getChildren();

}
