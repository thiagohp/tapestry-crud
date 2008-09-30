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

package br.com.arsmachina.tapestrycrud.encoder;

import org.apache.tapestry5.corelib.components.Palette;
import org.apache.tapestry5.corelib.components.Select;

/**
 * Interface which defines a single method, {@link #toLabel(Object)}, that returns a
 * user-presentable description (label) of a given object.
 * 
 * @author Thiago H. de Paula Figueiredo
 * @param <T> the class related to this encoder.
 */
public interface LabelEncoder<T> {

	/**
	 * Returns a user-presentable description (label) of a given object. It can be used, for
	 * example, as the object label in a {@link Select} or {@link Palette}.
	 * 
	 * @param object a {@link <T>}.
	 * @return a {@link String}.
	 */
	String toLabel(T object);

}
