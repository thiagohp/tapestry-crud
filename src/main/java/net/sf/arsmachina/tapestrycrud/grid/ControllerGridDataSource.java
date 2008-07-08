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

package net.sf.arsmachina.tapestrycrud.grid;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sf.arsmachina.controller.Controller;
import net.sf.arsmachina.dao.SortCriterion;

import org.apache.tapestry5.grid.ColumnSort;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.SortConstraint;

/**
 * {@link GridDataSource} implementation using a {@link Controller} instance, specifically its
 * {@link Controller#findAll(int, int, SortCriterion[])} method.
 * 
 * @author Thiago H. de Paula Figueiredo (ThiagoHP)
 */
public class ControllerGridDataSource<T, K extends Serializable> implements GridDataSource {

	private static final SortCriterion[] EMPTY_SORT_CRITERION_ARRAY = new SortCriterion[0];

	final private Class<T> entityClass;

	final private Controller<T, K> controller;

	private List<T> list;

	private int startIndex;

	/**
	 * Single construtctor of this class.
	 * 
	 * @param controller a {@link Controller}. It cannot be <code>null</code>.
	 */
	@SuppressWarnings("unchecked")
	public ControllerGridDataSource(Class<T> clasz, Controller<T, K> controller) {

		if (controller == null) {
			throw new IllegalArgumentException("Parameter controller cannot be null");
		}

		this.controller = controller;
		entityClass = clasz;

		assert controller != null;
		assert entityClass != null;

	}

	/**
	 * @see org.apache.tapestry.grid.GridDataSource#getAvailableRows()
	 */
	public int getAvailableRows() {
		return controller.countAll();
	}

	/**
	 * @see org.apache.tapestry.grid.GridDataSource#getRowType()
	 */
	@SuppressWarnings("unchecked")
	public Class getRowType() {
		return entityClass;
	}

	/**
	 * @see org.apache.tapestry.grid.GridDataSource#getRowValue(int)
	 */
	public Object getRowValue(int index) {
		final int position = index - startIndex;
		return list.get(position);
	}

	/**
	 * @see org.apache.tapestry.grid.GridDataSource#prepare(int, int, java.util.List)
	 */
	public void prepare(int startIndex, int endIndex, List<SortConstraint> sortConstraints) {

		this.startIndex = startIndex;
		SortCriterion[] sortCriteria; 
		
		sortCriteria = convertSortConstraintToSortCriterion(sortConstraints);
		
		final int maxResults = (endIndex - startIndex) + 1;
		list = controller.findAll(startIndex, maxResults, sortCriteria);

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
