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

import java.util.Map;

import org.apache.tapestry5.OptionModel;

/**
 * Simple {@link OptionModel} implementation.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public class SimpleOptionModel implements OptionModel {

	private Map<String, String> attributes;

	private String label;

	private Object value;

	private boolean disabled;

	/**
	 * Constructor that takes all the possible parameters.
	 * 
	 * @param value an {@link Object} containing the value of this option.
	 * @param label a {@link String} containing the label.
	 * @param disabled a <code>boolean</code> defining if this option will be disabled.
	 * @param attributes a {@link Map}<code>&lt;String, String&gt;<code>.
	 */
	public SimpleOptionModel(Object value, String label, boolean disabled,
			Map<String, String> attributes) {
		this.attributes = attributes;
		this.label = label;
		this.value = value;
		this.disabled = disabled;
	}

	/**
	 * The same as <code>SimpleOptionModel(value, label, disabled, null)</code>.
	 * 
	 * @param value an {@link Object} containing the value of this option.
	 * @param label a {@link String} containing the label.
	 * @param disabled a <code>boolean</code> defining if this option will be disabled.
	 */
	public SimpleOptionModel(Object value, String label, boolean disabled) {
		this(value, label, disabled, null);
	}

	/**
	 * The same as <code>SimpleOptionModel(value, label, false, null)</code>.
	 * 
	 * @param value an {@link Object} containing the value of this option.
	 * @param label a {@link String} containing the label.
	 */
	public SimpleOptionModel(Object value, String label) {
		this(value, label, false, null);
	}

	/**
	 * The same as <code>SimpleOptionModel(null, label, false, null)</code>.
	 * 
	 * @param value an {@link Object} containing the value of this option.
	 * @param label a {@link String} containing the label.
	 */
	public SimpleOptionModel(String label) {
		this(null, label, false, null);
	}

	/**
	 * @see org.apache.tapestry.OptionModel#getAttributes()
	 */
	public Map<String, String> getAttributes() {
		return attributes;
	}

	/**
	 * @see org.apache.tapestry.OptionModel#getLabel()
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @see org.apache.tapestry.OptionModel#getValue()
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * @see org.apache.tapestry.OptionModel#isDisabled()
	 */
	public boolean isDisabled() {
		return disabled;
	}

}
