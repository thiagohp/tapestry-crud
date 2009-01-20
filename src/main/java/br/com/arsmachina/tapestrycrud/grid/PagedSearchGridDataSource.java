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

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.grid.ColumnSort;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.SortConstraint;

import br.com.arsmachina.controller.Controller;
import br.com.arsmachina.dao.SortCriterion;

/**
 * {@link GridDataSource} implementation using a {@link Controller} instance, specifically its
 * {@link Controller#findAll(int, int, SortCriterion[])} method.
 * 
 * @param <T> the type of the elements returned.
 * @author Thiago H. de Paula Figueiredo
 */
public class PagedSearchGridDataSource<T> implements GridDataSource {

	private static final SortCriterion[] EMPTY_SORT_CRITERION_ARRAY = new SortCriterion[0];

	final private Class<T> entityClass;

	final private PagedSearch<T> pagedSearch;

	private List<T> list;

	private int firstIndex;

	/**
	 * Single construtctor of this class.
	 * 
	 * @param clasz the type of the returned objects. It cannot be null.
	 * @param pagedSearch a {@link Controller}. It cannot be null.
	 */
	public PagedSearchGridDataSource(Class<T> clasz, PagedSearch<T> pagedSearch) {

		if (pagedSearch == null) {
			throw new IllegalArgumentException("Parameter pagedSearch cannot be null");
		}

		this.pagedSearch = pagedSearch;
		entityClass = clasz;

		assert pagedSearch != null;
		assert entityClass != null;

	}

	public int getAvailableRows() {
		return pagedSearch.count();
	}

	@SuppressWarnings("unchecked")
	public Class getRowType() {
		return entityClass;
	}

	public Object getRowValue(int index) {
		final int position = index - firstIndex;
		return list.get(position);
	}

	public void prepare(int firstIndex, int endIndex, List<SortConstraint> sortConstraints) {

		this.firstIndex = firstIndex;
		SortCriterion[] sortCriteria; 
		
		sortCriteria = convertSortConstraintToSortCriterion(sortConstraints);
		
		final int maximumResults = (endIndex - firstIndex) + 1;
		list = pagedSearch.search(firstIndex, maximumResults, sortCriteria);

	}

	/**
	 * Converts a {@link List} of {@link SortConstraint} to a {@link SortCriterion} array.
	 * @param sortConstraints a {@link List} of {@link SortConstraint}.
	 * @return a {@link SortCriterion} array.
	 */
	private SortCriterion[] convertSortConstraintToSortCriterion(
			List<SortConstraint> sortConstraints) {
		
		SortCriterion[] sortCriteria;
		if (sortConstraints.size() > 0) {
			
			List<SortCriterion> list = new ArrayList<SortCriterion>();
	
			for (SortConstraint sortConstraint : sortConstraints) {
	
				final ColumnSort columnSort = sortConstraint.getColumnSort();
	
				if (columnSort != ColumnSort.UNSORTED) {
	
					final String propertyName = sortConstraint.getPropertyModel().getPropertyName();
	
					final boolean ascending = columnSort == ColumnSort.ASCENDING;
					list.add(new SortCriterion(propertyName, ascending));
	
				}
	
			}
			
			sortCriteria = list.toArray(new SortCriterion[sortConstraints.size()]);
			
		}
		else {
			sortCriteria = EMPTY_SORT_CRITERION_ARRAY;
		}
		
		return sortCriteria;
		
	}

}
