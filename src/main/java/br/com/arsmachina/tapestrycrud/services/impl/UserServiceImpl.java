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

package br.com.arsmachina.tapestrycrud.services.impl;

import org.apache.tapestry5.services.ApplicationStateManager;

import br.com.arsmachina.authentication.controller.UserController;
import br.com.arsmachina.authentication.entity.User;
import br.com.arsmachina.authentication.service.UserService;

/**
 * {@link UserService} implementation using Tapestry CRUD.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public class UserServiceImpl implements br.com.arsmachina.authentication.service.UserService {

	final private ApplicationStateManager applicationStateManager;

	final private UserController userController;

	/**
	 * Single constructor of this class.
	 * 
	 * @param userController an {@link UserController}. It cannot be null.
	 * @param applicationStateManager an {@link ApplicationStateManager}. It
	 *            cannot be null.
	 */
	public UserServiceImpl(UserController userController,
			ApplicationStateManager applicationStateManager) {

		if (userController == null) {
			throw new IllegalArgumentException(
					"Parameter userController cannot be null");
		}

		if (applicationStateManager == null) {
			throw new IllegalArgumentException(
					"Parameter ApplicationStateManager cannot be null");
		}

		this.userController = userController;
		this.applicationStateManager = applicationStateManager;

	}

	/**
	 * Returns <code>applicationStateManager.getIfExists(User.class)</code>.
	 * 
	 * @return an {@link User} or null.
	 */
	public User getUser() {

		final User user = applicationStateManager.getIfExists(User.class);

		if (user != null) {
			userController.reattach(user);
		}

		return user;

	}

	/**
	 * Returns <code>getUser() != null</code>
	 */
	public boolean isLoggedIn() {
		return getUser() != null;
	}

}
