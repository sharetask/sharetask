/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
'use strict';

/* Controllers */
angular.module('shareTaskApp.controllers', ['ui', 'ngDragDrop']).
	controller('AuthCtrl', ['$scope', '$location', '$rootScope', 'User', 'LocalStorage', function($scope, $location, $rootScope, User, LocalStorage) {
		
		$scope.errorStatus = 0;
		
		// get logged user from local storage
		$rootScope.loggedUser = LocalStorage.get('logged-user');
		console.log("Logged user: %o", $rootScope.loggedUser);
		
		// redirect to tasks page if user is already logged in
		if (!jQuery.isEmptyObject($rootScope.loggedUser)) {
			console.log("User (user: %o) is already logged in. Redirect to tasks page.", $rootScope.loggedUser);
			$location.path("/tasks");
		}
		
		/**
		 * Login user.
		 * User is redirected to tasks page.
		 */
		$scope.login = function() {
			console.log("Login user (username: %s) with password (password: %s)", $scope.user.username, $scope.user.password);
			User.authenticate({username: $scope.user.username, password: $scope.user.password}, function(data, status) {
					console.log("Auth success! data: %o, status: %o", data, status);
					$rootScope.loggedUser = {username: $scope.user.username};
					LocalStorage.store('logged-user', $rootScope.loggedUser);
					$location.path("/tasks");
				}, function(data, status) {
					console.log("Auth error! data: %o, status: %o", data, status);
					$rootScope.loggedUser = {};
					LocalStorage.remove('logged-user');
					$scope.errorStatus = status;
			});
		};
		
	}])
	.controller('AppCtrl', ['$scope', '$location', '$rootScope', '$filter', 'Workspace', 'Task', 'User', 'LocalStorage', function($scope, $location, $rootScope, $filter, Workspace, Task, User, LocalStorage) {
		
		$scope.viewPanelTaskFilter = true;
		$scope.selectedWorkspace;
		$scope.selectedTask;
		$scope.taskEditMode = '';
		$scope.filter = {'queue': 'MY_PENDING', 'tag': '', 'searchString': '', 'orderBy': 'TASK_DUE_DATE'};
		$scope.tags = [];
		$scope.dateOptions = {format: 'dd/mm/yyyy'};
		var taskFilter = $filter('filterTasks');
		
		/**
		 * Logout user.
		 * User is redirected to login page.
		 */
		$scope.logout = function() {
			console.log("Logout user: %s", $rootScope.loggedUser.username);
			$rootScope.loggedUser = {};
			LocalStorage.remove('logged-user');
			$location.path("/");
		};
		
		/**
		 * Setting active workspace.
		 * Event is propagated into Workspace controller for loading workspace tasks from server.
		 * @param {number} id - Workspace ID.
		 */
		/*
		$scope.setActiveWorkspace = function(id) {
			console.log("Setting active workspace (id: %s)", id);
			$scope.$broadcast('EVENT_SET_ACTIVE_WORKSPACE', {workspaceId: id});
		};
		*/
		/**
		 * Receive event 'EVENT_SET_ACTIVE_WORKSPACE' for setting active workspace.
		 * After receiving of event all workspace tasks are loaded from server.
		 */
		/*
		$scope.$on('EVENT_SET_ACTIVE_WORKSPACE', function(event, data) {
			console.log("Received broadcast message 'EVENT_SET_ACTIVE_WORKSPACE' (data: %o)", data);
			$scope.selectedWorkspaceId = data.workspaceId;
			$scope.loadTasks();
		});
		*/
		
		/**
		 * Loading all workspaces from server.
		 */
		$scope.loadWorkspaces = function() {
			console.log("Load all workspaces");
			Workspace.find({type: 'MEMBER'}, function(workspaces) {
				console.log("Loaded workspaces from server: %o", workspaces);
				if (workspaces.length) {
					$scope.workspaces = workspaces;
					$scope.selectedWorkspace = workspaces[0];
					$scope.loadTasks();
				}
			}, function(response) {
				console.log("error response: %o", response);
				if (response.status == 403) {
					$scope.logout();
				}
			});
		};
		
		/**
		 * Add new workspace.
		 * User adds workspace title only. All others attributes are set to default values.
		 * Workspace data are stored to server.
		 */
		$scope.addWorkspace = function() {
			console.log("Add new workspace (workspaceTitle: %s)", $scope.newWorkspaceTitle);
			var workspace = {title: $scope.newWorkspaceTitle, owner: {username: $rootScope.loggedUser.username}};
			console.log("Workspace: %o", workspace);
			Workspace.create({workspace: workspace}, function(data, status) {
					console.log("Workspace create success! data: %o, status: %o", data, status);
					$scope.workspaces.push(data);
					$scope.newWorkspaceTitle = '';
					$scope.setEditMode('');
				}, function(data, status) {
					console.log("Workspace create error! data: %o, status: %o", data, status);
				});
		};
		
		/**
		 * Setting selected workspace.
		 * @param {object} workspace - Workspace.
		 */
		$scope.setSelectedWorkspace = function(workspace) {
			console.log("Set selected workspace (workspace: %o)", workspace);
			$scope.selectedWorkspace = workspace;
			$scope.loadTasks();
		};
		
		/**
		 * Load all active tasks for selected workspace.
		 */
		$scope.loadTasks = function() {
			console.log("Load all active tasks for workspace (id: %s)", $scope.selectedWorkspace.id);
			Workspace.getActiveTasks({workspaceId: $scope.selectedWorkspace.id}, function(tasks) {
					console.log("Loaded workspace tasks from server: %o", tasks);
					$scope.allTasks = tasks;
					$scope.setTaskFilterQueue($scope.filter.queue);
				}, function(response) {
					console.log("Error getting all active tasks for workspace(id: %s): %o", $scope.selectedWorkspace.id, response);
					if (response.status == 403) {
						$scope.logout();
					}
			});
		};
		
		/**
		 * Setting filter queue.
		 * Presented tasks are filtered and ordered according current filter settings.
		 * @param {string} queue - Queue name.
		 */
		$scope.setTaskFilterQueue = function(queue) {
			console.log("Set filter queue: %s", queue);
			$scope.filter.queue = queue;
			$scope.filterTasks();
			if (!jQuery.isEmptyObject($scope.tasks)) {
				// set selected task
				$scope.setSelectedTask($scope.tasks[0].id);
				// set tags
				$scope.setTags();
				/*
				// parse tags
				var taskTags = new Array();
				angular.forEach($scope.tasks, function(task) {
					if (!jQuery.isEmptyObject(task.tags)) {
						angular.forEach(task.tags, function(tag) {
							if (taskTags.indexOf(tag) == -1) {
								taskTags.push(tag);
							}
						});
					}
				});
				$scope.tags = taskTags;
				console.log("Tags parsed for queue: %o", $scope.tags);
				*/
			}
			else {
				$scope.selectedTask = null;
			}
		};
		
		$scope.setTags = function() {
			var taskTags = new Array();
			angular.forEach($scope.tasks, function(task) {
				if (!jQuery.isEmptyObject(task.tags)) {
					angular.forEach(task.tags, function(tag) {
						if (taskTags.indexOf(tag) == -1) {
							taskTags.push(tag);
						}
					});
				}
			});
			$scope.tags = taskTags;
			console.log("Tags parsed for queue: %o", $scope.tags);
		};
		
		/**
		 * Setting filter tag.
		 * If input tag is same as current filter tag then current filter tag is removed.
		 * Presented tasks are filtered and ordered according current filter settings.
		 * @param {string} tag - Tag name.
		 */
		$scope.setTaskFilterTag = function(tag) {
			console.log("Set filter tag: %s", tag);
			if (tag == $scope.filter.tag) {
				$scope.filter.tag = '';
			}
			else {
				$scope.filter.tag = tag;
			}
			$scope.filterTasks();
			// set selected task
			if (!jQuery.isEmptyObject($scope.tasks)) {
				$scope.setSelectedTask($scope.tasks[0].id);
			}
			else {
				$scope.selectedTask = null;
			}
		};
		
		/**
		 * Filter tasks.
		 * Presented tasks are filtered and ordered according current filter settings.
		 */
		$scope.filterTasks = function() {
			console.log("Filter tasks: %o", $scope.filter);
			$scope.tasks = taskFilter($scope.filter, this.allTasks);
			$scope.tasks = $filter('orderBy')(this.tasks, this.orderTasks);
			// set selected task
			if (!jQuery.isEmptyObject($scope.tasks)) {
				$scope.setSelectedTask($scope.tasks[0].id);
			}
			else {
				$scope.selectedTask = null;
			}
		};
		
		/**
		 * Setting task filter ordering.
		 * Presented tasks are filtered and ordered according current filter settings.
		 * @param {string} orderBy - Order by name.
		 */
		$scope.setTaskOrdering = function(orderBy) {
			console.log("Set task ordering to: %s", orderBy);
			$scope.filter.orderBy = orderBy;
			$scope.filterTasks();
		};
		
		/**
		 * Setting selected task.
		 * @param {number} taskId - Task ID.
		 */
		$scope.setSelectedTask = function(taskId) {
			console.log("Set selected task (id: %s)", taskId);
			var tasks = $.grep($scope.tasks, function(e) {
				return e.id == taskId;
			});
			$scope.selectedTask = tasks[0];
			console.log("Selected task: %o", $scope.selectedTask);
			// get task comments
			Task.getComments({workspaceId: $scope.selectedWorkspace.id, taskId: $scope.selectedTask.id}, function(comments) {
				console.log("Loaded task comments from server: %o", comments);
				$scope.selectedTask.comments = comments;
			}, function(response) {
				console.log("error response: %o", response);
				if (response.status == 403) {
					$scope.logout();
				}
			});
			
			
			/*
			$scope.selectedTask = Task.findById({id: taskId}, function(data) {
					console.log("Task.findById: %o", data);
			}, function(response) {
					console.log("response: %o", response);
			});
			*/
			//console.log("Selected task: %o", $scope.selectedTask);
		};
		
		/**
		 * Function for ordering tasks.
		 * @param {object} task - Task.
		 */
		$scope.orderTasks = function(task) {
			if ($scope.filter.orderBy == "TASK_DUE_DATE") { return task.dueDate; }
			else if ($scope.filter.orderBy == "TASK_TITLE") { return task.title; }
			else if ($scope.filter.orderBy == "TASK_AUTHOR") { return task.createdBy.name; }
		};
		
		/**
		 * Adding tag to the task.
		 * Task is stored to server.
		 */
		$scope.addTaskTag = function() {
			console.log("Add new tag to task, id: %s, tag: %s", $scope.selectedTask.id, $scope.newTag);
			$scope.selectedTask.tags.push($scope.newTag);
			$scope.updateTask($scope.selectedTask);
			$scope.setTags();
			$scope.taskEditMode = '';
			$scope.newTag = '';
		};
		
		/**
		 * Adding tag to the task via drag&drop.
		 * Task is stored to server.
		 */
		$scope.addTaskTagDragDrop = function(event, ui) {
			console.log("Add new tag to task, id: %s, tag: %s", this.task.id, ui.draggable.context.textContent);
			console.log("onDropFunc (event: %o, ui: %o)", event, ui);
			console.log("onDropFunc (draggable context: %o)", ui.draggable.context.textContent);
			console.log("onDropFunc (task.tags: %o)", this.task);
			// add tag
			this.task.tags.push(ui.draggable.context.textContent);
			// update task
			$scope.updateTask(this.task);
		};
		
		/**
		 * Removing tag from the task.
		 * Task is stored to server.
		 * @param {number} taskId - Task ID.
		 * @param {string} tag - Tag name.
		 */
		$scope.removeTag = function(taskId, tag) {
			console.log("Remove task tag, id: %s, tag: %s", taskId, tag);
		};
		
		/**
		 * Setting edit mode.
		 * Represents attribute which is currently in editing mode.
		 * @param {string} mode - Edit mode.
		 */
		$scope.setEditMode = function(mode) {
			console.log("Switch edit mode to: %s", mode);
			if (mode == this.taskEditMode) {
				$scope.taskEditMode = "";
			}
			else {
				$scope.taskEditMode = mode;
			}
		};
		
		/**
		 * Setting view flag.
		 * Flag represent if element is displayed.
		 * @param {string} element - Element name.
		 */
		$scope.setView = function(element) {
			console.log("Set view for element: %s", element);
			if (element == 'PANEL-TASK-FILTER') {
				$scope.viewPanelTaskFilter === true ? $scope.viewPanelTaskFilter = false : $scope.viewPanelTaskFilter = true;
				console.log("viewPanelTaskFilter set to: %s", $scope.viewPanelTaskFilter);
			}
		};
		
		/**
		 * Updating task data.
		 * Task data are stored to server.
		 * @param {object} task - Task.
		 */
		$scope.updateTask = function(task) {
			console.log("Update task data (task: %o)", task);
			$scope.taskEditMode = '';
			$scope.selectedTask = task;
			var result = $.grep(this.tasks, function(e) {
				return e.id == task.id;
			});
			result[0].title = task.title;
			result[0].description = task.description;
			result[0].tags = task.tags;
			Task.update({workspaceId: $scope.selectedWorkspace.id, task: $scope.selectedTask}, function(data, status) {
					console.log("Task update success! data: %o, status: %o", data, status);
				}, function(data, status) {
					console.log("Task update error! data: %o, status: %o", data, status);
				});
			LocalStorage.store('workspace-' + $scope.selectedWorkspace.id, $scope.allTasks);
		};
		
		/**
		 * Changing task queue filter.
		 */
		$scope.changeTaskQueue = function() {
			console.log("Change task filter queue (queue: %s)", $scope.filter.queue);
			if ($scope.filter.queue == 'MY_PENDING') { $scope.setTaskFilterQueue('MY_TODAY'); }
			else if ($scope.filter.queue == 'MY_TODAY') { $scope.setTaskFilterQueue('MY_OVERDUE'); }
			else if ($scope.filter.queue == 'MY_OVERDUE') { $scope.setTaskFilterQueue('MY_HIGH_PRIORITY'); }
			else if ($scope.filter.queue == 'MY_HIGH_PRIORITY') { $scope.setTaskFilterQueue('MY_COMPLETED'); }
			else if ($scope.filter.queue == 'MY_COMPLETED') { $scope.setTaskFilterQueue('MY_PENDING'); }
		};
		
		/**
		 * Changing active task priority.
		 * Task data are stored to server.
		 */
		$scope.changeTaskPriority = function() {
			console.log("Change task priority (id: %s, priority: %s)", $scope.selectedTask.id, $scope.selectedTask.priority);
			if ($scope.selectedTask.priority == 'LOW') { $scope.selectedTask.priority = 'MEDIUM'; }
			else if ($scope.selectedTask.priority == 'MEDIUM') { $scope.selectedTask.priority = 'HIGH'; }
			else if ($scope.selectedTask.priority == 'HIGH') { $scope.selectedTask.priority = 'LOW'; }
			$scope.updateTask($scope.selectedTask);
		};
		
		/**
		 * Add new task.
		 * User adds task title only. All others attributes are set to default values.
		 * Task data are stored to server.
		 */
		$scope.addTask = function() {
			console.log("Add new task (taskTitle: %s)", $scope.newTaskTitle);
			// FIXME Remove setting comments after solving issue #2
			var task = {title: $scope.newTaskTitle, createdBy: $rootScope.loggedUser.username, createdOn: new Date(), priority: 'MEDIUM', comments: []};
			console.log("Task: %o", task);
			Task.create({workspaceId: $scope.selectedWorkspace.id, task: task}, function(data, status) {
					console.log("Task create success! data: %o, status: %o", data, status);
					$scope.allTasks.push(data);
					LocalStorage.store('workspace-' + $scope.selectedWorkspace.id, $scope.allTasks);
					$scope.filterTasks();
					$scope.newTaskTitle = '';
					$scope.setEditMode('');
				}, function(data, status) {
					console.log("Task create error! data: %o, status: %o", data, status);
				});
		};
		
		/**
		 * Forward active task to another workspace member.
		 * Task data are stored to server.
		 */
		$scope.forwardTask = function(user) {
			console.log("Forward task (id: %s) to user (user: %o)", $scope.selectedTask.id, user);
			$scope.updateTask($scope.selectedTask);
		};
		
		/**
		 * Delete active task.
		 * Task data are stored to server.
		 */
		$scope.deleteTask = function() {
			console.log("Delete task (id: %s)", $scope.selectedTask.id);
			$scope.updateTask($scope.selectedTask);
		};
		
		/**
		 * Complete task.
		 * @param {boolean} bulk - If true then all checked tasks are completed. Else only selected task is completed.
		 * Task data are stored to server.
		 */
		$scope.completeTask = function(bulk) {
			console.log("Complete task (bulk: %s)", bulk);
			if (!bulk) {
				// complete selected task
				console.log("Complete task (id: %s)", $scope.selectedTask.id);
				Task.complete({workspaceId: $scope.selectedWorkspace.id, taskId: $scope.selectedTask.id}, function(data, status) {
						console.log("Task complete success! data: %o, status: %o", data, status);
						var task = $.grep($scope.tasks, function(e) {
							return e.id == $scope.selectedTask.id;
						});
						task[0].state = 'FINISHED';
						$scope.selectedTask.state = 'FINISHED';
						$scope.taskEditMode = '';
						LocalStorage.store('workspace-' + $scope.selectedWorkspace.id, $scope.allTasks);
						$scope.filterTasks();
						$scope.setSelectedTask($scope.tasks[0].id);
						$scope.setEditMode('');
					}, function(data, status) {
						console.log("Task complete error! data: %o, status: %o", data, status);
					});
			}
			else {
				// complete all checked tasks
				var checkedTasks = $.grep($scope.tasks, function(e) {
					return e.checked == true;
				});
				angular.forEach(checkedTasks, function(value, key) {
					if (value.state != 'FINISHED') {
						console.log("Complete task (id: %s)", value.id);
						Task.complete({workspaceId: $scope.selectedWorkspace.id, taskId: value.id}, function(data, status) {
								console.log("Task complete success! data: %o, status: %o", data, status);
								var task = $.grep($scope.tasks, function(e) {
									return e.id == value.id;
								});
								task[0].state = 'FINISHED';
								$scope.selectedTask.state = 'FINISHED';
								$scope.taskEditMode = '';
								LocalStorage.store('workspace-' + $scope.selectedWorkspace.id, $scope.allTasks);
								$scope.filterTasks();
								if (!jQuery.isEmptyObject($scope.tasks)) {
									$scope.setSelectedTask($scope.tasks[0].id);
								}
								$scope.setEditMode('');
							}, function(data, status) {
								console.log("Task complete error! data: %o, status: %o", data, status);
							});
					}
					value.checked = false;
				});
			}
		};
		
		// get logged user from local storage
		$rootScope.loggedUser = LocalStorage.get('logged-user');
		console.log("Logged user: %o", $rootScope.loggedUser);
		
		// redirect to login page if user is not logged in
		if (jQuery.isEmptyObject($rootScope.loggedUser) || jQuery.isEmptyObject($rootScope.loggedUser.username)) {
			console.log("Unauthenticated access. Redirect to login page.");
			$location.path("/");
		}
		else {
			// Loading all workspaces from server.
			$scope.loadWorkspaces();
		}
	}])
	.controller('AdminCtrl', ['$scope', '$location', '$rootScope', 'Workspace', 'LocalStorage', function($scope, $location, $rootScope, Workspace, LocalStorage) {
		
		/**
		 * Loading all workspaces from server.
		 */
		$scope.loadWorkspaces = function() {
			console.log("Load all workspaces");
			Workspace.find({type: 'OWNER'}, function(workspaces) {
				console.log("Loaded workspaces from server: %o", workspaces);
				if (workspaces.length) {
					$scope.workspaces = workspaces;
					$scope.selectedWorkspace = workspaces[0];
				}
			}, function(response) {
				console.log("error response: %o", response);
				if (response.status == 403) {
					$scope.logout();
				}
			});
		};
		
		/**
		 * Setting selected workspace.
		 * @param {number} id - Workspace ID.
		 */
		$scope.setSelectedWorkspace = function(id) {
			console.log("Set selected workspace (id: %s)", id);
			var workspace = $.grep($scope.workspaces, function(e) {
				return e.id == id;
			});
			$scope.selectedWorkspace = workspace[0];
			$scope.setEditMode('');
			console.log("Selected workspace: %o", $scope.selectedWorkspace);
		};
		
		/**
		 * Add new workspace.
		 * Workspace data are stored to server.
		 */
		$scope.addWorkspace = function() {
			console.log("Add new workspace (workspace: %o)", $scope.newWorkspace);
			$scope.newWorkspace.owner = {username: $rootScope.loggedUser.username};
			//var workspace = {title: $scope.newWorkspaceTitle, owner: {username: $rootScope.loggedUser.username}};
			console.log("Workspace: %o", $scope.newWorkspace);
			Workspace.create({workspace: $scope.newWorkspace}, function(data, status) {
					console.log("Workspace create success! data: %o, status: %o", data, status);
					$scope.workspaces.push(data);
					$scope.setSelectedWorkspace(data.id);
					$scope.newWorkspace = null;
					$scope.setEditMode('');
				}, function(data, status) {
					console.log("Workspace create error! data: %o, status: %o", data, status);
				});
		};
		
		/**
		 * Update workspace.
		 * Workspace data are stored to server.
		 */
		$scope.updateWorkspace = function() {
			console.log("Update workspace (workspace: %o)", $scope.selectedWorkspace);
			Workspace.update({workspace: $scope.selectedWorkspace}, function(data, status) {
					console.log("Workspace update success! data: %o, status: %o", data, status);
				}, function(data, status) {
					console.log("Workspace update error! data: %o, status: %o", data, status);
				});
		};
		
		/**
		 * Setting edit mode.
		 * Represents attribute which is currently in editing mode.
		 * @param {string} mode - Edit mode.
		 */
		$scope.setEditMode = function(mode) {
			console.log("Switch edit mode to: %s", mode);
			if (mode == this.attrEditMode) {
				$scope.attrEditMode = "";
			}
			else {
				$scope.attrEditMode = mode;
			}
		};
		
		/**
		 * Invite new member to workspace.
		 * New member data are stored to server.
		 */
		$scope.inviteMember = function() {
			console.log("Invite new member (member: %o) to workspace (id: %s)", $scope.newMember, $scope.selectedWorkspace.id);
			// FIXME Change call after implementation of back-end operation.
			/*
			Workspace.inviteMember({workspaceId: $scope.selectedWorkspace.id, user: $scope.newMember}, function(data, status) {
					console.log("Workspace inviteMember success! data: %o, status: %o", data, status);
					$scope.newMember = '';
					$scope.setEditMode('');
				}, function(data, status) {
					console.log("Workspace inviteMember error! data: %o, status: %o", data, status);
				});
			*/
		};
		
		/**
		 * Remove member from workspace.
		 * Workspace member data are stored to server.
		 */
		$scope.removeMember = function(username) {
			console.log("Remove member (username: %s) from workspace (id: %s)", username, $scope.selectedWorkspace.id);
			Workspace.removeMember({workspaceId: $scope.selectedWorkspace.id, username: username}, function(data, status) {
					console.log("Workspace removeMember success! data: %o, status: %o", data, status);
					// remove member from local workspace
					var newMembers = $.grep($scope.selectedWorkspace.members, function(e) {
						return e.username != username;
					});
					$scope.selectedWorkspace.members = newMembers;
					console.log("new members: %o", $scope.selectedWorkspace.members);
				}, function(data, status) {
					console.log("Workspace removeMember error! data: %o, status: %o", data, status);
				});
		};
		
		// get logged user from local storage
		$rootScope.loggedUser = LocalStorage.get('logged-user');
		console.log("Logged user: %o", $rootScope.loggedUser);
		
		// redirect to login page if user is not logged in
		if (jQuery.isEmptyObject($rootScope.loggedUser) || jQuery.isEmptyObject($rootScope.loggedUser.username)) {
			console.log("Unauthenticated access. Redirect to login page.");
			$location.path("/");
		}
		else {
			// Loading all workspaces from server.
			$scope.loadWorkspaces();
		}
	}])
	.controller('UserCtrl', ['$scope', '$location', '$rootScope', 'Workspace', 'LocalStorage', function($scope, $location, $rootScope, Workspace, LocalStorage) {
		
		// get logged user from local storage
		$rootScope.loggedUser = LocalStorage.get('logged-user');
		console.log("Logged user: %o", $rootScope.loggedUser);
		
		// redirect to login page if user is not logged in
		if (jQuery.isEmptyObject($rootScope.loggedUser) || jQuery.isEmptyObject($rootScope.loggedUser.username)) {
			console.log("Unauthenticated access. Redirect to login page.");
			$location.path("/");
		}
		else {
			
		}
	}])
	;