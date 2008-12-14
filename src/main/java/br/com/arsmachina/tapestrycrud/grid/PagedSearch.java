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

package br.com.arsmachina.tapestrycrud.grid;

import java.util.List;

import br.com.arsmachina.dao.SortCriterion;

/**
 * Interface that defines a search that can be carried on in a paged fashion.
 *
 * @param <T> the type of the elements returned.
 * @author Thiago H. de Paula Figueiredo
 */
public interface PagedSearch<T> {

	/**
	 * Performs the search.
	 *
	 * @param firstIndex an <code>int</code> with the index of the first object to be returned.
	 * The first object has index 0.
	 * @param maximumResults an <code>int</code> with the maximum number of objects to be returned.
	 * @param sortingConstraints an {@link SortCriterion} array used to define how the returned
	 * list will be sorted.
	 * @return a {@link List} of <code>T</code>.
	 */
	List<T> search(int firstIndex, int maximumResults, SortCriterion... sortingConstraints);
	
	/**
	 * Return the number of available objects to be returned by the search.
	 * 
	 * @return an <code>int</code>.
	 */
	int count();

}
