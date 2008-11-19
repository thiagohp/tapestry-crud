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

import java.io.Serializable;

import org.apache.tapestry5.PrimaryKeyEncoder;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.services.ValueEncoderFactory;

/**
 * Single interface used to define many encoding-related services. This interface extends
 * {@link LabelEncoder}, {@link PrimaryKeyEncoder}, {@link ValueEncoder},
 * {@link ValueEncoderFactory}, and {@link ActivationContextEncoder}.
 * 
 * @param <T> the entity class related to this encoder.
 * @param <K> the type of the class' primary key property.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public interface Encoder<T, K extends Serializable> extends
		PrimaryKeyEncoder<K, T>, ValueEncoder<T>, ValueEncoderFactory<T>,
		ActivationContextEncoder<T>, LabelEncoder<T> {

}
