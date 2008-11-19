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

package br.com.arsmachina.tapestrycrud;

import java.io.Serializable;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.corelib.components.Zone;

/**
 * Interface that defines some common methods for pages that edit entities.
 * 
 * @param <T> the entity class related to this encoder.
 * @param <K> the type of the class' primary key property.
 * @author Thiago H. de Paula Figueiredo
 */
public interface EditPage<T, K extends Serializable> extends
		CrudPage<T, K> {

	/**
	 * Returns edited object.
	 * 
	 * @return a {@link T}.
	 */
	public T getObject();

	/**
	 * Changes the edited object.
	 * 
	 * @param object a {@link T}.
	 */
	public void setObject(T object);

	/**
	 * Returns the {@link Zone} that wraps the form. It is used for AJAX form submission.
	 * Otherwise, just return <code>null</code>.
	 *  
	 * @return an {@link Object} that must be a {@link Zone} (preferred) or a  
	 * {@link Block}
	 */
	public Object getFormZone();

}
