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

package br.com.arsmachina.tapestrycrud.selectmodel;

import java.util.List;

import org.apache.tapestry5.OptionGroupModel;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.util.AbstractSelectModel;

/**
 * Simple {@link SelectModel} implementation. It doesnt't provide any {@link OptionGroupModel}.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public class SimpleSelectModel extends AbstractSelectModel {

	private List<OptionModel> optionModels;

	/**
	 * Single constructor of this class.
	 * 
	 * @param optionModels a {@link List} of {@link OptionModel}s. It cannot be null.
	 */
	public SimpleSelectModel(List<OptionModel> optionModels) {

		if (optionModels == null) {
			throw new IllegalArgumentException("Parameter optionModels cannot be null");
		}

		this.optionModels = optionModels;

	}

	/**
	 * @see org.apache.tapestry.SelectModel#getOptionGroups()
	 */
	public List<OptionGroupModel> getOptionGroups() {
		return null;
	}

	/**
	 * @see org.apache.tapestry.SelectModel#getOptions()
	 */
	public List<OptionModel> getOptions() {
		return optionModels;
	}

}
