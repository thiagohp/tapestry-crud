// Copyright 2009 Thiago H. de Paula Figueiredo
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

package br.com.arsmachina.tapestrycrud.mixins;

import org.apache.tapestry5.annotations.IncludeJavaScriptLibrary;
import org.apache.tapestry5.annotations.MixinAfter;

import br.com.arsmachina.tapestrycrud.Constants;
import br.com.arsmachina.tapestrycrud.components.Message;

/**
 * Mixin that hides the confirmation message ({@link Message} component) after a number of seconds.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
@MixinAfter
@IncludeJavaScriptLibrary(Constants.JAVASCRIPT_ASSET_ROOT + "hideConfirmationMessage.js")
public class HideConfirmationMessage {

}
