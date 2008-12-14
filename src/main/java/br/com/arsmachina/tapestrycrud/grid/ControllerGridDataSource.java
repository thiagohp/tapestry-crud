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

import java.io.Serializable;
import java.util.List;

import org.apache.tapestry5.grid.GridDataSource;

import br.com.arsmachina.controller.Controller;
import br.com.arsmachina.controller.ReadableController;
import br.com.arsmachina.dao.SortCriterion;

/**
 * {@link GridDataSource} implementation using a {@link Controller} instance, specifically its
 * {@link Controller#findAll(int, int, SortCriterion[])} method.
 * 
 * @param <T> the entity class related to this controller.
 * @param <K> the type of the field that represents the entity class' primary key.
 * @author Thiago H. de Paula Figueiredo
 */
public class ControllerGridDataSource<T, K extends Serializable> extends
		PagedSearchGridDataSource<T> {

	/**
	 * Single construtctor of this class.
	 * 
	 * @param controller a {@link Controller}. It cannot be <code>null</code>.
	 */
	@SuppressWarnings("unchecked")
	public ControllerGridDataSource(Class<T> clasz, ReadableController<T, K> controller) {
		super(clasz, new ReadableControllerPagedSearch(controller));
	}

	final private static class ReadableControllerPagedSearch<T, K extends Serializable> implements
			PagedSearch<T> {

		final private ReadableController<T, K> controller;

		/**
		 * Single constructor of this class.
		 * 
		 * @param controller a {@link Controller}. It cannot be null.
		 */
		public ReadableControllerPagedSearch(ReadableController<T, K> controller) {

			if (controller == null) {
				throw new IllegalArgumentException("Parameter controller cannot be null");
			}

			this.controller = controller;

		}

		public int count() {
			return controller.countAll();
		}

		public List<T> search(int firstIndex, int maximumResults, SortCriterion... sortingConstraints) {
			return controller.findAll(firstIndex, maximumResults, sortingConstraints);
		}

	}

}
