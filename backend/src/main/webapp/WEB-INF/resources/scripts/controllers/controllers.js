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

/* Controllers WEB page*/
angular.module('shareTaskWeb.controllers', ['localization'])
	.controller('IndexCtrl', ['$scope', '$location', '$rootScope', '$window', 'User', '$route', function($scope, $location, $rootScope, $window, User, $route) {
		
		$scope.showSignInForm = false;
		$route.reload();
		
		/**
		 * Logout user.
		 */
		$scope.logout = function() {
			User.logout($rootScope.loggedUser.username, function(data, status) {
					console.log("User logout success! data: %o, status: %o", data, status);
					$rootScope.loggedUser = null;
				}, function(data, status, script, func) {
					console.log("User logout error! data: %o, status: %o", data, status);
				});
		};
	}])
	.controller('AuthCtrl', ['$scope', '$location', '$rootScope', '$window', 'User', function($scope, $location, $rootScope, $window, User) {
		
		$scope.loginData = {processing: false, result: 0};
		
		/**
		 * Login user.
		 * User is redirected to tasks page.
		 */
		$scope.login = function() {
			console.log("Login user (username: %s) with password (password: %s)", $scope.user.username, $scope.user.password);
			$scope.loginData.processing = true;
			User.authenticate({username: $scope.user.username, password: $scope.user.password}, function(data, status) {
					console.log("User auth success! data: %o, status: %o", data, status);
					// get user profile info
					User.get({username: $scope.user.username}, function(data, status) {
							console.log("User get success! data: %o, status: %o", data, status);
							$rootScope.loggedUser = data;
							
							$window.location.href = $rootScope.appBaseUrl+"app#/tasks";
						}, function(data, status) {
							console.log("Auth error! data: %o, status: %o", data, status);
							$scope.user = {};
							$rootScope.loggedUser = null;
							$scope.loginData.processing = false;
						});
					$scope.loginData.result = 1;
				}, function(data, status) {
					console.log("User auth error! data: %o, status: %o", data, status);
					$scope.user = {};
					$rootScope.loggedUser = null;
					$scope.loginData.result = -1;
					$scope.loginData.processing = false;
				});
		};
	}])
	.controller('RegisterCtrl', ['$scope', '$location', '$rootScope', '$window', 'User', function($scope, $location, $rootScope, $window, User) {
		
		$scope.newAccountData = {processing: false, result: 0};
		$scope.languages= [
                           {code: 'en', label: '_English_'},
                           {code: 'cs', label: '_Czech_'}
                           ];
		$scope.language = $scope.languages[0].code;
		/**
		 * Register user.
		 */
		$scope.register = function() {
			console.log("Register user (username: %s)", $scope.newAccount.username);
			$scope.newAccountData.processing = true;
			$scope.newAccount.password = $scope.newAccount.password1;
			$scope.newAccount.language = $scope.language;
			delete($scope.newAccount.password1);
			delete($scope.newAccount.password2);
			User.create({user: $scope.newAccount}, function(data, status) {
					console.log("User create success! data: %o, status: %o", data, status);
					$scope.newAccount = {name: '', surName: '', email: ''};
					$scope.newAccountData.result = 1;
					$scope.newAccountData.processing = false;
				}, function(data, status) {
					console.log("User create error! data: %o, status: %o", data, status);
					$scope.newAccountData.result = -1;
					$scope.newAccountData.processing = false;
					if (data.type === 'USER_ALREADY_EXISTS') {
						$scope.newAccountData.result = -2;
					}
				});
		};
	}])
	.controller('ForgotPasswordCtrl', ['$scope', '$location', '$rootScope', '$window', 'User', function($scope, $location, $rootScope, $window, User) {
		
		$scope.forgotPasswordData = {processing: false, result: 0};
		
		/**
		 * Send request for resetting password
		 */
		$scope.send = function() {
			console.log("Send request for resetting password of user (username: %s)", $scope.username);
			$scope.forgotPasswordData.processing = true;
			User.resetPassword({username: $scope.username}, function(data, status) {
					console.log("User resetPassword success! data: %o, status: %o", data, status);
					$scope.username = '';
					$scope.forgotPasswordData.result = 1;
					$scope.forgotPasswordData.processing = false;
				}, function(data, status) {
					console.log("User resetPassword error! data: %o, status: %o", data, status);
					$scope.forgotPasswordData.result = -1;
					$scope.forgotPasswordData.processing = false;
				});
		};
	}]);

/* Controllers */
var controllers = angular.module('shareTaskApp.controllers', ['ui', 'ngDragDrop', 'ui.bootstrap', 'localization']);

/**
 * Application controller.
 */
controllers.controller('AppCtrl', ['$scope', '$location', '$rootScope', '$filter', '$timeout', '$window', 'localize', 'Workspace', 'Task', 'User', 'LocalStorage', 'ErrorHandling', 'Logger', 'Analytics', function($scope, $location, $rootScope, $filter, $timeout, $window, localize, Workspace, Task, User, LocalStorage, ErrorHandling, Logger, Analytics) {
		
		$scope.errorMessageOnTaskList = '';
		$scope.viewPanelTaskFilter = true;
		$scope.viewDatePicker = false;
		$scope.workspaces = [];
		$scope.selectedWorkspace = null;
		$scope.selectedTask;
		$scope.taskEditMode = '';
		$scope.filter = {queue: 'MY_PENDING', searchString: '', tag: '', orderBy: 'TASK_DUE_DATE'};
		$scope.tags = [];
		$scope.newTasks = [];
		$scope.newTaskTitle = '';
		$scope.checkedTasks = [];
		$rootScope.currentPage = "tasks";
		$rootScope.errorConsole = {show: false, msgCode: ''};
		$rootScope.firstWorkspaceWindow = {show: false};
		//$scope.dateOptions = {format: 'dd/mm/yyyy'};
		//$scope.datePickerOptions = { dateFormat: "M d, yy" };
		$scope.datePickerOptions = {firstDay: 1, dateFormat: "yy-mm-dd", minDate:'0'};
		/*
		$scope.datePickerOptions = {firstDay: 1,
									dateFormat: "yy-mm-dd",
									prevText: '<<',
									nextText: '>>',
									minDate:'0',
									dayNames: ["Neděle", "Pondělí", "Úterý", "Středa", "Čtvrtek", "Pátek", "Sobota"],
									dayNamesMin: ["Ne", "Po", "Út", "St", "Čt", "Pá", "So"],
									monthNames: ["Leden", "Únor", "Březen", "Duben", "Květen", "Červen", "Červenec", "Srpen", "Září", "Říjen", "Listopad", "Prosinec"]};
		*/
		$scope.addWorkspaceData = {processing: false, result: 0};
		var taskFilter = $filter('filterTasks');
		
		Analytics.trackPage('/tasks');
		
		/**
		 * Keyboard shortcuts service logic
		 * @param {object} e - key event.
		 */
		$scope.keyPressed = function(e) {
			if (e.target.tagName === 'BODY') {
				Log.debug("key pressed: %o", e);
				switch (e.which) {
					case 110:
						// key 'n' pressed - add new task
						$scope.setEditMode('NEW-TASK');
						break;
					case 67:
						// key 'Shift+c' pressed - complete task
						$scope.completeTask(false);
						break;
					case 102:
						// key 'f' pressed - forward task
						$scope.setEditMode('FORWARD-TASK');
						break;
					case 100:
						// key 'd' pressed - delete task
						$scope.setEditMode('DELETE-TASK');
						break;
					case 80:
						// key 'Shift+p' pressed - change task priority
						$scope.changeTaskPriority();
						break;
					/*case 119:
						// key 'w' pressed - add new workspace
						$scope.setEditMode('NEW-WORKSPACE');
						break;*/
					case 115:
						// key 's' pressed - search
						setTimeout(function() { $('#inputSearch')[0].focus(); }, 0);
						break;
					case 116:
						// key 't' pressed - show/hide task filter panel
						$scope.setView('PANEL-TASK-FILTER');
						break;
					case 114:
						// key 'r' pressed - refresh/load workspace task from server
						$scope.loadTasks();
						break;
					case 99:
						// key 'c' pressed - add new task comment
						setTimeout(function() { $('#inputTaskAddComment')[0].focus(); }, 0);
						break;
					case 108:
						// key 'l' pressed - add new task label
						$scope.setEditMode('ADD-TAG');
						break;
				}
			}
		};
		
		$scope.click = function() {
			Log.debug("Click event");
			$scope.setEditMode('');
		};
		
		$scope.$on('EVENT-RELOAD-WORKSPACES', function() {
			$scope.loadWorkspaces();
		});
		
		/**
		 * Loading all workspaces from server.
		 */
		$scope.loadWorkspaces = function() {
			Log.debug("Load all workspaces");
			Workspace.find({type: 'MEMBER'}, function(data, status) {
					Log.debug("Workspace find success! data: %o, status: %o", data, status);
					if (data.length) {
						$scope.workspaces = data;
						$scope.selectedWorkspace = data[0];
						$scope.loadTasks();
					}
					else {
						$rootScope.firstWorkspaceWindow = {show: true};
					}
					Analytics.trackEvent('Workspaces', 'load', 'success', status);
				}, function(data, status, script, func) {
					Log.debug("Workspace find error!");
					ErrorHandling.handle(data, status, script, func);
					Analytics.trackEvent('Workspaces', 'load', 'error', status);
				});
		};
		
		/**
		 * Add new workspace.
		 * User adds workspace title only. All others attributes are set to default values.
		 * Workspace data are stored to server.
		 */
		$scope.addWorkspace = function() {
			Log.debug("Add new workspace (workspaceTitle: %s)", $scope.newWorkspaceTitle);
			var workspace = {title: $scope.newWorkspaceTitle, owner: {username: $rootScope.loggedUser.username}};
			Log.debug("Workspace: %o", workspace);
			Workspace.create({workspace: workspace}, function(data, status) {
					Log.debug("Workspace create success! data: %o, status: %o", data, status);
					$scope.workspaces.push(data);
					$scope.setSelectedWorkspace(data);
					$scope.newWorkspaceTitle = '';
					$scope.setEditMode('');
					Analytics.trackEvent('Workspace', 'add', 'success', status);
				}, function(data, status, script, func) {
					Log.debug("Workspace create error!");
					ErrorHandling.handle(data, status, script, func);
					Analytics.trackEvent('Workspace', 'add', 'error', status);
				});
		};
		
		/**
		 * Setting selected workspace.
		 * @param {object} workspace - Workspace.
		 */
		$scope.setSelectedWorkspace = function(workspace) {
			Log.debug("Set selected workspace (workspace: %o)", workspace);
			$scope.selectedWorkspace = workspace;
			$scope.loadTasks();
		};
		
		/**
		 * Load all active tasks for selected workspace.
		 */
		$scope.loadTasks = function() {
			Log.debug("Load all active tasks for workspace (id: %s)", $scope.selectedWorkspace.id);
			Workspace.getActiveTasks({workspaceId: $scope.selectedWorkspace.id}, function(data, status) {
					Log.debug("Workspace getActiveTasks success! data: %o, status: %o", data, status);
					$scope.newTasks = [];
					$scope.allTasks = data;
					$scope.setTaskFilterQueue($scope.filter.queue);
					Analytics.trackEvent('Tasks', 'load', 'success', status);
				}, function(data, status, script, func) {
					Log.debug("Workspace getActiveTasks error!");
					ErrorHandling.handle(data, status, script, func);
					Analytics.trackEvent('Tasks', 'load', 'success', status);
			});
		};
		
		/**
		 * Setting filter queue.
		 * Presented tasks are filtered and ordered according current filter settings.
		 * @param {string} queue - Queue name.
		 */
		$scope.setTaskFilterQueue = function(queue) {
			Log.debug("Set filter queue: %s", queue);
			$scope.filter.queue = queue;
			$scope.filterTasks();
			if (!jQuery.isEmptyObject($scope.tasks)) {
				// set selected task
				//$scope.setSelectedTask($scope.tasks[0].id);
				// set tags
				$scope.setTags();
			}
			else {
				$scope.selectedTask = null;
				$scope.tags = [];
			}
			Analytics.trackEvent('Tasks', 'setFilterQueue', queue);
		};
		
		/**
		 * Parsing tasks from selected queue and collects all tags
		 */
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
			Log.debug("Tags parsed for queue: %o", $scope.tags);
		};
		
		/**
		 * Setting filter tag.
		 * If input tag is same as current filter tag then current filter tag is removed.
		 * Presented tasks are filtered and ordered according current filter settings.
		 * @param {string} tag - Tag name.
		 */
		$scope.setTaskFilterTag = function(tag) {
			Log.debug("Set filter tag: %s", tag);
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
			Analytics.trackEvent('Tasks', 'setFilterTag');
		};
		
		/**
		 * Filter tasks.
		 * Presented tasks are filtered and ordered according current filter settings.
		 */
		$scope.filterTasks = function(selectedTaskId) {
			Log.debug("Filter tasks: %o, selectedTaskId: %s", $scope.filter, selectedTaskId);
			$scope.tasks = taskFilter($scope.filter, $scope.allTasks);
			$scope.tasks = $filter('orderBy')($scope.tasks, $scope.orderTasks);
			// set selected task
			if (!jQuery.isEmptyObject($scope.tasks) && isNaN(selectedTaskId)) {
				$scope.setSelectedTask($scope.tasks[0].id);
			}
			else if (!jQuery.isEmptyObject($scope.tasks) && !isNaN(selectedTaskId)) {
				var tasks = $.grep($scope.tasks, function(e) {
					return e.id == selectedTaskId;
				});
				if (!jQuery.isEmptyObject(tasks)) {
					$scope.setSelectedTask(tasks[0].id);
				}
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
			Log.debug("Set task ordering to: %s", orderBy);
			$scope.filter.orderBy = orderBy;
			$scope.filterTasks();
			Analytics.trackEvent('Tasks', 'setOrdering', orderBy);
		};
		
		$scope.dateOver = function(date) {
			var inputDate = new Date(date);
			var currDate = new Date();
			var inputDateString = inputDate.getFullYear()+"-"+("0"+inputDate.getMonth()).substr(-2)+"-"+inputDate.getDate();
			var currDateString = currDate.getFullYear()+"-"+("0"+currDate.getMonth()).substr(-2)+"-"+currDate.getDate();
			//Log.debug("date %o vs curr %o", inputDateString, currDateString);
			if (inputDateString < currDateString) return 1;
			if (inputDateString == currDateString) return 0;
			if (inputDateString > currDateString) return -1;
		}
		
		/**
		 * Setting selected task.
		 * @param {number} taskId - Task ID.
		 */
		$scope.setSelectedTask = function(taskId) {
			Log.debug("Set selected task (id: %s)", taskId);
			var tasks = $.grep($scope.tasks, function(e) {
				return e.id == taskId;
			});
			$scope.selectedTask = tasks[0];
			Log.debug("Selected task: %o", $scope.selectedTask);
			// get task comments
			Task.getComments({workspaceId: $scope.selectedWorkspace.id, taskId: $scope.selectedTask.id}, function(data, status) {
					Log.debug("Task getComments success! data: %o, status: %o", data, status);
					$scope.selectedTask.comments = data;
				}, function(data, status, script, func) {
					Log.debug("Task getComments error!");
					ErrorHandling.handle(data, status, script, func);
				});
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
			Log.debug("Add new tag to task, id: %s, tag: %s", $scope.selectedTask.id, $scope.newTag);
			$scope.newTagProcessing = true;
			if ($scope.selectedTask.tags === null) {
				$scope.selectedTask.tags = [];
			}
			$scope.selectedTask.tags.push($scope.newTag);
			$scope.updateTask($scope.selectedTask);
			$scope.setTags();
			$scope.taskEditMode = '';
			$scope.newTag = '';
			$scope.newTagProcessing = false;
			Analytics.trackEvent('Task', 'addTag', 'Typed');
		};
		
		/**
		 * Adding tag to the task via drag&drop.
		 * Task is stored to server.
		 */
		$scope.addTaskTagDragDrop = function(event, ui) {
			Log.debug("Add new tag to task, id: %s, tag: %s", this.task.id, ui.draggable.context.textContent);
			Log.debug("onDropFunc (event: %o, ui: %o)", event, ui);
			Log.debug("onDropFunc (draggable context: %o)", ui.draggable.context.textContent);
			Log.debug("onDropFunc (task.tags: %o)", this.task);
			// add tag
			this.task.tags.push(ui.draggable.context.textContent);
			// update task
			$scope.updateTask(this.task);
			Analytics.trackEvent('Task', 'addTag', 'Drag&Drop');
		};
		
		/**
		 * Removing tag from the task.
		 * Task is stored to server.
		 * @param {number} taskId - Task ID.
		 * @param {string} tag - Tag name.
		 */
		$scope.removeTag = function(taskId, tag) {
			Log.debug("Remove task tag, id: %s, tag: %s", taskId, tag);
			// get task
			var task = $.grep($scope.tasks, function(e) {
				return e.id == taskId;
			});
			// remove tag from task
			var newTags = $.grep(task[0].tags, function(e) {
				Log.debug("%s : %o", tag, e);
				return e != tag;
			});
			task[0].tags = newTags;
			Log.debug("new tags: %o", task[0].tags);
			Analytics.trackEvent('Task', 'removeTag');
		};
		
		/**
		 * Setting edit mode.
		 * Represents attribute which is currently in editing mode.
		 * @param {string} mode - Edit mode.
		 */
		$scope.setEditMode = function(mode) {
			Log.debug("Switch edit mode to: %s", mode);
			if (mode == this.taskEditMode) {
				$scope.taskEditMode = "";
			}
			else {
				$scope.taskEditMode = mode;
				// make focus
				if ($scope.taskEditMode === 'NEW-TASK') { setTimeout(function() { $('#inputTaskAdd')[0].focus(); }, 0); }
				if ($scope.taskEditMode === 'NEW-WORKSPACE') { setTimeout(function() { $('#inputWorkspaceAdd')[0].focus(); }, 0); }
				if ($scope.taskEditMode === 'FORWARD-TASK') { setTimeout(function() { $('#inputTaskForward')[0].focus(); }, 0); }
				if ($scope.taskEditMode === 'ADD-TAG') { setTimeout(function() { $('#inputTaskAddLabel')[0].focus(); }, 0); }
				if ($scope.taskEditMode === 'DESCRIPTION') { setTimeout(function() { $('#inputTaskEditDescription')[0].focus(); }, 0); }
			}
		};
		
		/**
		 * Setting view flag.
		 * Flag represent if element is displayed.
		 * @param {string} element - Element name.
		 */
		$scope.setView = function(element) {
			Log.debug("Set view for element: %s", element);
			if (element == 'PANEL-TASK-FILTER') {
				//$scope.viewPanelTaskFilter === true ? $scope.viewPanelTaskFilter = false : $scope.viewPanelTaskFilter = true;
				$scope.viewPanelTaskFilter = !$scope.viewPanelTaskFilter;
				Log.debug("viewPanelTaskFilter set to: %s", $scope.viewPanelTaskFilter);
				Analytics.trackEvent('UI', 'showHideTaskFilterSidebar', $scope.viewPanelTaskFilter);
			}
			if (element == 'TASK-DUE-DATE-PICKER') {
				if ($scope.selectedTask.state == 'FINISHED') {
					Log.debug("Task (id: %s) already completed", $scope.selectedTask.id);
					return;
				}
				$scope.viewTaskDueDatePicker = !$scope.viewTaskDueDatePicker;
				$scope.viewTaskCreatedDatePicker = false;
				Log.debug("viewTaskDueDatePicker set to: %s", $scope.viewTaskDueDatePicker);
			}
			/*
			if (element == 'TASK-CREATED-DATE-PICKER') {
				$scope.viewTaskCreatedDatePicker = !$scope.viewTaskCreatedDatePicker;
				$scope.viewTaskDueDatePicker = false;
				Log.debug("viewTaskCreatedDatePicker set to: %s", $scope.viewTaskCreatedDatePicker);
			}
			*/
		};
		
		/**
		 * Updating task data.
		 * Task data are stored to server.
		 * @param {object} task - Task.
		 */
		$scope.updateTask = function(task) {
			Log.debug("Update task data (task: %o)", task);
			$scope.taskEditMode = '';
			$scope.selectedTask = task;
			var result = $.grep(this.tasks, function(e) {
				return e.id == task.id;
			});
			result[0].title = task.title;
			result[0].description = task.description;
			result[0].tags = task.tags;
			result[0].dueDate = task.dueDate;
			delete(result[0].comments);
			delete(result[0].checked);
			Task.update({workspaceId: $scope.selectedWorkspace.id, task: result[0]}, function(data, status) {
					Log.debug("Task update success! data: %o, status: %o", data, status);
				}, function(data, status, script, func) {
					Log.debug("Task update error!");
					ErrorHandling.handle(data, status, script, func);
				});
			$scope.setSelectedTask(task.id);
		};
		
		/**
		 * Check task
		 * @param {object} task - Task.
		 */
		$scope.checkTask = function(task) {
			Log.debug("Check task (task: %o)", task);
			task.checked = !task.checked;
			// get checked tasks
			$scope.checkedTasks = $.grep($scope.tasks, function(e) {
				return e.checked == true;
			});
			Log.debug("Checked tasks: %s", $scope.checkedTasks.length);
		};
		
		/**
		 * Changing task queue filter.
		 */
		$scope.changeTaskQueue = function() {
			Log.debug("Change task filter queue (queue: %s)", $scope.filter.queue);
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
			if ($scope.selectedTask == null) {
				return;
			}
			Log.debug("Change task priority (id: %s, priority: %s)", $scope.selectedTask.id, $scope.selectedTask.priority);
			if ($scope.selectedTask.state == 'FINISHED') {
				Log.debug("Task (id: %s) already completed", $scope.selectedTask.id);
				return;
			}
			if ($scope.selectedTask.priority == 'LOW') { $scope.selectedTask.priority = 'MEDIUM'; }
			else if ($scope.selectedTask.priority == 'MEDIUM') { $scope.selectedTask.priority = 'HIGH'; }
			else if ($scope.selectedTask.priority == 'HIGH') { $scope.selectedTask.priority = 'LOW'; }
			$scope.updateTask($scope.selectedTask);
			Analytics.trackEvent('Task', 'setPriority');
		};
		
		/**
		 * Changing active task due date.
		 * Task data are stored to server.
		 */
		$scope.changeTaskDueDate = function() {
			$scope.viewTaskDueDatePicker = false;
			if ($scope.selectedTask == null) {
				return;
			}
			Log.debug("Change task due date (id: %s, due date: %s)", $scope.selectedTask.id, $scope.selectedTask.dueDate);
			if ($scope.selectedTask.state == 'FINISHED') {
				Log.debug("Task (id: %s) already completed", $scope.selectedTask.id);
				return;
			}
			$scope.updateTask($scope.selectedTask);
			//$scope.filterTasks($scope.selectedTask.id);
			//$scope.tasks = $filter('orderBy')($scope.tasks, $scope.orderTasks);
			//$scope.setTaskFilterQueue($scope.filter.queue);
			Analytics.trackEvent('Task', 'setDueDate');
		};
		
		/**
		 * Unset active task due date.
		 * Task data are stored to server.
		 */
		$scope.unsetTaskDueDate = function() {
			$scope.viewTaskDueDatePicker = false;
			if ($scope.selectedTask == null) {
				return;
			}
			Log.debug("Unset task due date (id: %s)", $scope.selectedTask.id);
			if ($scope.selectedTask.state == 'FINISHED') {
				Log.debug("Task (id: %s) already completed", $scope.selectedTask.id);
				return;
			}
			$scope.selectedTask.dueDate = null;
			$scope.updateTask($scope.selectedTask);
			//$scope.filterTasks($scope.selectedTask.id);
			//$scope.tasks = $filter('orderBy')($scope.tasks, $scope.orderTasks);
			//$scope.setTaskFilterQueue($scope.filter.queue);
			Analytics.trackEvent('Task', 'setDueDate');
		};
		
		/**
		 * Add new task.
		 * User adds task title only. All others attributes are set to default values.
		 * Task data are stored to server.
		 */
		$scope.addTask = function() {
			Log.debug("Add new task (taskTitle: %o)", $scope);
			$scope.newTaskProcessing = true;
			var createdByObj = {username: $rootScope.loggedUser.username};
			var assigneeObj = {username: $rootScope.loggedUser.username};
			var task = {title: $scope.newTaskTitle, createdBy: createdByObj, assignee: assigneeObj, createdOn: new Date(), priority: 'MEDIUM'};
			Log.debug("Task: %o", task);
			Task.create({workspaceId: $scope.selectedWorkspace.id, task: task}, function(data, status) {
					Log.debug("Task create success! data: %o, status: %o", data, status);
					$scope.allTasks.push(data);
					$scope.filterTasks(data.id);
					$scope.newTaskTitle = '';
					$scope.setEditMode('');
					$scope.newTaskProcessing = false;
					Analytics.trackEvent('Task', 'add', 'success', status);
				}, function(data, status, script, func) {
					Log.debug("Task create error!");
					$scope.newTaskProcessing = false;
					ErrorHandling.handle(data, status, script, func);
					Analytics.trackEvent('Task', 'add', 'error', status);
				});
		};
		
		/**
		 * Forward active task to another workspace member.
		 * Task data are stored to server.
		 * @param {object} user - User.
		 */
		$scope.forwardTask = function(user) {
			Log.debug("Forward task (id: %s) to user (user: %o)", $scope.selectedTask.id, user);
			if ($scope.selectedTask.state == 'FINISHED') {
				Log.debug("Task (id: %s) already completed", $scope.selectedTask.id);
				return;
			}
			Task.forward({workspaceId: $scope.selectedWorkspace.id, taskId: $scope.selectedTask.id, username: user.username}, function(data, status) {
					Log.debug("Task forward success! data: %o, status: %o", data, status);
					var task = $.grep($scope.tasks, function(e) {
						return e.id == $scope.selectedTask.id;
					});
					task[0].assignee = user;
					$scope.selectedTask.assignee = user;
					$scope.filterTasks();
					$scope.setSelectedTask($scope.tasks[0].id);
					$scope.setEditMode('');
					Analytics.trackEvent('Task', 'forward', 'success', status);
				}, function(data, status, script, func) {
					Log.debug("Task forward error!");
					ErrorHandling.handle(data, status, script, func);
					Analytics.trackEvent('Task', 'forward', 'error', status);
				});
		};
		
		/**
		 * Delete active task.
		 * @param {boolean} bulk - If true then all checked tasks are deleted. Else only selected task is deleted.
		 * Task data are stored to server.
		 */
		$scope.deleteTask = function(bulk) {
			Log.debug("Delete task (bulk: %s)", bulk);
			if (!bulk) {
				// delete selected task
				Log.debug("Delete task (id: %s)", $scope.selectedTask.id);
				Task.remove({workspaceId: $scope.selectedWorkspace.id, taskId: $scope.selectedTask.id}, function(data, status) {
						Log.debug("Task remove success! data: %o, status: %o", data, status);
						var tasks = $.grep($scope.allTasks, function(e) {
							return e.id != $scope.selectedTask.id;
						});
						$scope.allTasks = tasks;
						$scope.filterTasks();
						if (!jQuery.isEmptyObject($scope.tasks)) {
							$scope.setSelectedTask($scope.tasks[0].id);
						}
						$scope.setEditMode('');
						Analytics.trackEvent('Task', 'deleteOne', 'success', status);
					}, function(data, status, script, func) {
						Log.debug("Task remove error!");
						ErrorHandling.handle(data, status, script, func);
						$scope.errorMessageOnTaskList = 'Error deleting task "'+$scope.selectedTask.title+'".';
						Analytics.trackEvent('Task', 'deleteOne', 'error', status);
					});
			}
			else {
				// delete all checked tasks
				var checkedTasks2 = $.grep($scope.tasks, function(e) {
					return e.checked == true;
				});
				angular.forEach(checkedTasks2, function(value, key) {
					Log.debug("Delete task (id: %s)", value.id);
					Task.remove({workspaceId: $scope.selectedWorkspace.id, taskId: value.id}, function(data, status) {
							Log.debug("Task remove success! data: %o, status: %o", data, status);
							var tasks = $.grep($scope.allTasks, function(e) {
								return e.id != value.id;
							});
							$scope.allTasks = tasks;
							$scope.filterTasks();
							if (!jQuery.isEmptyObject($scope.tasks)) {
								$scope.setSelectedTask($scope.tasks[0].id);
							}
							$scope.setEditMode('');
						}, function(data, status, script, func) {
							Log.debug("Task remove error!");
							ErrorHandling.handle(data, status, script, func);
							$scope.errorMessageOnTaskList = 'Error deleting selected tasks.';
						});
					value.checked = false;
				});
				$scope.checkedTasks = [];
				Analytics.trackEvent('Task', 'deleteBulk');
			}
		};
		
		/**
		 * Complete task.
		 * @param {boolean} bulk - If true then all checked tasks are completed. Else only selected task is completed.
		 * Task data are stored to server.
		 */
		$scope.completeTask = function(bulk) {
			Log.debug("Complete task (bulk: %s)", bulk);
			if ($scope.selectedTask.state == 'FINISHED') {
				Log.debug("Task (id: %s) already completed", $scope.selectedTask.id);
				return;
			}
			if (!bulk) {
				// complete selected task
				Log.debug("Complete task (id: %s)", $scope.selectedTask.id);
				Task.complete({workspaceId: $scope.selectedWorkspace.id, taskId: $scope.selectedTask.id}, function(data, status) {
						Log.debug("Task complete success! data: %o, status: %o", data, status);
						var task = $.grep($scope.tasks, function(e) {
							return e.id == $scope.selectedTask.id;
						});
						if (!jQuery.isEmptyObject(task)) {
							task[0].state = 'FINISHED';
						}
						$scope.selectedTask.state = 'FINISHED';
						$scope.filterTasks();
						if (!jQuery.isEmptyObject($scope.tasks)) {
							$scope.setSelectedTask($scope.tasks[0].id);
						}
						$scope.setEditMode('');
						Analytics.trackEvent('Task', 'completeOne', 'success', status);
					}, function(data, status, script, func) {
						Log.debug("Task complete error!");
						ErrorHandling.handle(data, status, script, func);
						Analytics.trackEvent('Task', 'completeOne', 'error', status);
					});
			}
			else {
				// complete all checked tasks
				var checkedTasks2 = $.grep($scope.tasks, function(e) {
					return e.checked == true;
				});
				angular.forEach(checkedTasks2, function(value, key) {
					if (value.state != 'FINISHED') {
						Log.debug("Complete task (id: %s)", value.id);
						Task.complete({workspaceId: $scope.selectedWorkspace.id, taskId: value.id}, function(data, status) {
								Log.debug("Task complete success! data: %o, status: %o", data, status);
								var task = $.grep($scope.tasks, function(e) {
									return e.id == value.id;
								});
								Log.debug("task: %o", task);
								if (!jQuery.isEmptyObject(task)) {
									task[0].state = 'FINISHED';
								}
								$scope.taskEditMode = '';
								$scope.filterTasks();
								if (!jQuery.isEmptyObject($scope.tasks)) {
									$scope.setSelectedTask($scope.tasks[0].id);
								}
								$scope.setEditMode('');
							}, function(data, status, script, func) {
								Log.debug("Task complete error!");
								ErrorHandling.handle(data, status, script, func);
							});
					}
					value.checked = false;
				});
				$scope.checkedTasks = [];
				Analytics.trackEvent('Task', 'completeBulk');
			}
		};
		
		/**
		 * Add new comment to task.
		 * New comment is stored to server.
		 */
		$scope.addTaskComment = function() {
			Log.debug("Add new comment (comment: %o) to task (id: %s)", $scope.newTaskComment, $scope.selectedTask.id);
			$scope.newCommentProcessing = true;
			if (!jQuery.isEmptyObject($scope.newTaskComment)) {
				Task.addComment({workspaceId: $scope.selectedWorkspace.id, taskId: $scope.selectedTask.id, comment: $scope.newTaskComment}, function(data, status) {
						Log.debug("Task addComment success! data: %o, status: %o", data, status);
						var createdBy = {username: $rootScope.loggedUser.username, name: $rootScope.loggedUser.name, surName: $rootScope.loggedUser.surName};
						$scope.newTaskComment.createdBy = createdBy;
						$scope.newTaskComment.createdOn = new Date();
						$scope.newTaskComment.message = $scope.newTaskComment.comment;
						$scope.selectedTask.comments.push($scope.newTaskComment);
						$scope.newTaskComment = null;
						$scope.newCommentProcessing = false;
						Analytics.trackEvent('Task', 'addComment', 'success', status);
					}, function(data, status, script, func) {
						Log.debug("Task addComment error!");
						$scope.newCommentProcessing = false;
						ErrorHandling.handle(data, status, script, func);
						Analytics.trackEvent('Task', 'addComment', 'error', status);
					});
			}
		};
		
		/**
		 * Timer for synchronization of tasks.
		 * Timer is fired regularly on background.
		 */
		$scope.syncTasks = function() {
			Log.debug("Starting SyncTasksTimer job");
			$scope.syncTasksTimer = $timeout(function() {
				Log.debug("SyncTasksTimer job fired");
				if ($scope.selectedWorkspace) {
					$scope.newTasks = [];
					Log.debug("Load all active tasks for workspace (id: %s)", $scope.selectedWorkspace.id);
					Workspace.getActiveTasks({workspaceId: $scope.selectedWorkspace.id}, function(data, status) {
							Log.debug("Workspace getActiveTasks success! data: %o, status: %o", data, status);
							Log.debug("Workspace allTasks: %o", $scope.allTasks);
							angular.forEach(data, function(task1) {
								var found = false;
								angular.forEach($scope.allTasks, function(task2) {
									if (task1.id === task2.id) {
										found = true;
									}
								});
								if (!found) {
									$scope.newTasks.push(task1);
								}
							});
							Log.debug("There are these new tasks: %o", $scope.newTasks);
						}, function(data, status, script, func) {
							Log.debug("Workspace getActiveTasks error!");
							//ErrorHandling.handle(data, status, script, func);
					});
				}
	        	$scope.syncTasks();
			}, 120000);
	        Log.debug("SyncTasksTimer (timer: %o) started", $scope.syncTasksTimer);
		};
		
		// Loading all workspaces from server.
		$scope.loadWorkspaces();
		// start sync for tasks
		$scope.syncTasks();
	}]);
	
/**
 * Worksapce controller.
 */
controllers.controller('WorkspacesCtrl', ['$scope', '$location', '$rootScope', '$timeout', '$window', 'localize', 'Workspace', 'User', 'ErrorHandling', 'Utils', function($scope, $location, $rootScope, $timeout, $window, localize, Workspace, User, ErrorHandling, Utils) {
	
		$scope.updateWorkspaceData = {processing: false, result: 0};
		$scope.deleteWorkspaceData = {processing: false, result: 0};
		$scope.newMemberData = {processing: false, result: 0};
		$scope.removeMemberData = {processing: false, result: 0};
		$scope.addWorkspaceData = {processing: false, result: 0};
		$scope.selectedWorkspaceTaskCount = 0;
		$rootScope.currentPage = "workspaces";
		
		/**
		 * Loading all workspaces from server.
		 */
		$scope.loadWorkspaces = function() {
			Log.debug("Load all workspaces");
			Workspace.find({type: 'OWNER'}, function(data, status) {
					Log.debug("Workspace find success! data: %o, status: %o", data, status);
					if (data.length) {
						$scope.workspaces = data;
						$scope.selectedWorkspace = data[0];
						$scope.loadTasks();
					}
				}, function(data, status, script, func) {
					Log.debug("Workspace find error!");
					ErrorHandling.handle(data, status, script, func);
				});
		};
		
		/**
		 * Load all active tasks for selected workspace.
		 */
		$scope.loadTasks = function() {
			Log.debug("Load all tasks for workspace (id: %s)", $scope.selectedWorkspace.id);
			Workspace.getActiveTasks({workspaceId: $scope.selectedWorkspace.id}, function(data, status) {
					Log.debug("Workspace getActiveTasks success! data: %o, status: %o", data, status);
					$scope.selectedWorkspaceTaskCount = data.length;
				}, function(data, status, script, func) {
					Log.debug("Workspace getActiveTasks error!");
					ErrorHandling.handle(data, status, script, func);
			});
		};
		
		/**
		 * Setting selected workspace.
		 * @param {number} id - Workspace ID.
		 */
		$scope.setSelectedWorkspace = function(id) {
			Log.debug("Set selected workspace (id: %s)", id);
			var workspace = $.grep($scope.workspaces, function(e) {
				return e.id == id;
			});
			$scope.selectedWorkspace = workspace[0];
			$scope.loadTasks();
			$scope.setEditMode('');
			Log.debug("Selected workspace: %o", $scope.selectedWorkspace);
		};
		
		/**
		 * Add new workspace.
		 * Workspace data are stored to server.
		 */
		$scope.addWorkspace = function() {
			Log.debug("Add new workspace (workspace: %o)", $scope.newWorkspace);
			$scope.addWorkspaceData.processing = true;
			$scope.newWorkspace.owner = {username: $rootScope.loggedUser.username};
			Log.debug("Workspace: %o", $scope.newWorkspace);
			Workspace.create({workspace: $scope.newWorkspace}, function(data, status) {
					Log.debug("Workspace create success! data: %o, status: %o", data, status);
					$scope.workspaces.push(data);
					$scope.setSelectedWorkspace(data.id);
					$scope.addWorkspaceData.result = 1;
					$scope.addWorkspaceData.processing = false;
					$scope.newWorkspace = null;
					$scope.setEditMode('');
				}, function(data, status, script, func) {
					Log.debug("Workspace create error!");
					ErrorHandling.handle(data, status, script, func);
					$scope.addWorkspaceData.result = -1;
					$scope.addWorkspaceData.processing = false;
				});
		};
		
		/**
		 * Update workspace.
		 * Workspace data are stored to server.
		 */
		$scope.updateWorkspace = function() {
			Log.debug("Update workspace (workspace: %o)", $scope.selectedWorkspace);
			$scope.updateWorkspaceData.processing = true;
			Workspace.update({workspace: $scope.selectedWorkspace}, function(data, status) {
					Log.debug("Workspace update success! data: %o, status: %o", data, status);
					$scope.updateWorkspaceData.result = 1;
					$scope.updateWorkspaceData.processing = false;
				}, function(data, status, script, func) {
					Log.debug("Workspace update error!");
					ErrorHandling.handle(data, status, script, func);
					$scope.updateWorkspaceData.result = -1;
					$scope.updateWorkspaceData.processing = false;
				});
		};
		
		/**
		 * Delete workspace.
		 * Workspace data are stored to server.
		 */
		$scope.deleteWorkspace = function() {
			Log.debug("Delete workspace (workspace: %o)", $scope.selectedWorkspace);
			$scope.deleteWorkspaceData.processing = true;
			Workspace.remove({workspace: $scope.selectedWorkspace}, function(data, status) {
					Log.debug("Workspace remove success! data: %o, status: %o", data, status);
					$scope.deleteWorkspaceData.result = 1;
					$scope.deleteWorkspaceData.processing = false;
					var workspaces = $.grep($scope.workspaces, function(e) {
						return e.id != $scope.selectedWorkspace.id;
					});
					$scope.workspaces = workspaces;
					$scope.selectedWorkspace = workspaces[0];
					$scope.loadTasks();
				}, function(data, status, script, func) {
					Log.debug("Workspace remove error!");
					ErrorHandling.handle(data, status, script, func);
					$scope.deleteWorkspaceData.result = -1;
					$scope.deleteWorkspaceData.processing = false;
				});
		};
		
		/**
		 * Setting edit mode.
		 * Represents attribute which is currently in editing mode.
		 * @param {string} mode - Edit mode.
		 */
		$scope.setEditMode = function(mode) {
			Log.debug("Switch edit mode to: %s", mode);
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
			Log.debug("Invite new member (member: %o) to workspace (id: %s)", $scope.newMember, $scope.selectedWorkspace.id);
			$scope.newMemberData.processing = true;
			if (!$scope.newMember || !$scope.newMember.username) {
				$scope.newMemberData.result = -2;
				$scope.newMemberData.processing = false;
				return;
			}
			else if (!Utils.validateEmail($scope.newMember.username)) {
				$scope.newMemberData.result = -3;
				$scope.newMemberData.processing = false;
				return;
			}
			Workspace.inviteMember({workspaceId: $scope.selectedWorkspace.id, user: $scope.newMember}, function(data, status) {
					Log.debug("Workspace inviteMember success! data: %o, status: %o", data, status);
					$scope.newMemberData.result = 1;
					$scope.newMemberData.processing = false;
					$scope.newMember.username = '';
					$scope.setEditMode('');
				}, function(data, status, script, func) {
					Log.debug("Workspace inviteMember error!");
					$scope.newMemberData.result = -1;
					$scope.newMemberData.processing = false;
					ErrorHandling.handle(data, status, script, func);
				});
		};
		
		/**
		 * Remove member from workspace.
		 * Workspace member data are stored to server.
		 */
		$scope.removeMember = function(username) {
			Log.debug("Remove member (username: %s) from workspace (id: %s)", username, $scope.selectedWorkspace.id);
			$scope.removeMemberData.processing = true;
			Workspace.removeMember({workspaceId: $scope.selectedWorkspace.id, username: username}, function(data, status) {
					Log.debug("Workspace removeMember success! data: %o, status: %o", data, status);
					// remove member from local workspace
					var newMembers = $.grep($scope.selectedWorkspace.members, function(e) {
						return e.username != username;
					});
					$scope.selectedWorkspace.members = newMembers;
					Log.debug("new members: %o", $scope.selectedWorkspace.members);
					$scope.removeMemberData.processing = false;
				}, function(data, status, script, func) {
					Log.debug("Workspace removeMember error!");
					$scope.removeMemberData.processing = false;
					ErrorHandling.handle(data, status, script, func);
				});
		};
		
		// Loading all workspaces from server.
		$scope.loadWorkspaces();
	}]);

/**
 * User controller.
 */
controllers.controller('UserCtrl', ['$scope', '$location', '$rootScope', '$filter', '$window', 'localize', 'User', 'Gravatar', 'ErrorHandling', function($scope, $location, $rootScope, $filter, $window, localize, User, Gravatar, ErrorHandling) {
		$scope.updateUserProfile = {processing: false, result: 0};
		$rootScope.currentPage = "user";
		$scope.gravatar = {};
		var hashFilter = $filter('hash');
		
		$scope.languages= [
                           {code: 'en', label: '_English_'},
                           {code: 'cs', label: '_Czech_'}
                           ];
		
		/**
		 * Get Gravatar profile data for user.
		 */
		$scope.getGravatar = function() {
			var mailHash = hex_md5($scope.loggedUser.username.toLowerCase());
			Gravatar.get({hash: mailHash}, function(data, status) {
					Log.debug("Gravatar get success! data: %o, status: %o", data, status);
					$scope.gravatar = data;
				}, function(data, status, script, func) {
					Log.debug("Gravatar get error! data: %o, status: %o", data, status);
				});
		};
		
		/**
		 * Update user's profile
		 * User's data are stored to server.
		 */
		$scope.update = function() {
			Log.debug("Update user's (username: %s) profile to (user: %o)", $scope.loggedUser.username, $scope.loggedUser);
			$scope.updateUserProfile.processing = true;
			if ($scope.loggedUser.newPassword1 && $scope.loggedUser.newPassword1 == $scope.loggedUser.newPassword2) {
				Log.debug("Change password to: %s", $scope.loggedUser.newPassword1);
				var userPassword = {username: $scope.loggedUser.username, password: $scope.loggedUser.newPassword1};
				// change user's password
				User.changePassword(userPassword, function(data, status) {
						Log.debug("User changePassword success! data: %o, status: %o", data, status);
					}, function(data, status, script, func) {
						Log.debug("User changePassword error!");
						ErrorHandling.handle(data, status, script, func);
					});
			}
			delete($scope.loggedUser.password);
			delete($scope.loggedUser.newPassword1);
			delete($scope.loggedUser.newPassword2);
			User.update({user: $scope.loggedUser}, function(data, status) {
					Log.debug("User update success! data: %o, status: %o", data, status);
					$scope.updateUserProfile.result = 1;
					$scope.updateUserProfile.processing = false;
					$rootScope.loggedUser = data;
				}, function(data, status, script, func) {
					Log.debug("User update error!");
					ErrorHandling.handle(data, status, script, func);
					$scope.updateUserProfile.result = -1;
					$scope.updateUserProfile.processing = false;
				});
		};
		
		User.getCurrentUser(function(data, status) {
				Log.debug("User getCurrentUser success! data: %o, status: %o", data, status);
				$rootScope.loggedUser = data;
				$scope.usernameHash = hashFilter($rootScope.loggedUser.username);
				$scope.getGravatar();
			}, function(data, status, script, func) {
				Log.debug("User getCurrentUser error! data: %o, status: %o", data, status);
				Log.debug("Unauthenticated access. Redirect to login page.");
				$window.location.href = $rootScope.appBaseUrl;
			});
	}]);

/**
 * Statistics controller.
 */
controllers.controller('StatisticsCtrl', ['$scope', '$rootScope', 'localize', 'Statistics', 'User', function($scope, $rootScope, localize, Statistics, User) {
		
		$scope.loadStatistics = function() {
			Statistics.getOverview(function(data, status) {
				Log.debug("Stistics getOverview success! data: %o, status: %o", data, status);
				$scope.overview = data;
			}, function(data, status, script, func) {
				Log.debug("Statistics getOverview error! data: %o, status: %o", data, status);
				Log.debug("Unauthenticated access. Redirect to login page.");
				$window.location.href = $rootScope.appBaseUrl;
			});
		};
			
		
		User.getCurrentUser(function(data, status) {
				Log.debug("User getCurrentUser success! data: %o, status: %o", data, status);
				$rootScope.loggedUser = data;
				$scope.loadStatistics();
			}, function(data, status, script, func) {
				Log.debug("User getCurrentUser error! data: %o, status: %o", data, status);
				Log.debug("Unauthenticated access. Redirect to login page.");
				$window.location.href = $rootScope.appBaseUrl;
			});
	}]);

/**
 * Header controller.
 */
controllers.controller('HeaderController', [ '$rootScope', '$scope', '$location', 'User', '$log', '$window', 
		function($rootScope, $scope, $location, User, $log, $window) {
			$scope.isActive = function(viewLocation) {
				return viewLocation === $location.path();
			};

			$scope.toggleHelp = function() {
				$rootScope.showHelp = !$rootScope.showHelp;
			};
			
			$scope.checkLogin = function() {
				$log.log("Checking login");
				User.isLoggedIn(
						function(data, status) {
							$log.log("User is loged in! status: %o", status);
							$rootScope.isLoggedIn = true;
							User.getCurrentUser(function(data, status) {
								$rootScope.loggedUser = data;
							});
							$location.path('/');
						}, function(data, status) {
							$log.log("User isn't loged in! status: %o", status);
							$rootScope.isLoggedIn = false;
						});
			};

			$scope.logout = function() {
				Log.debug("Logout user: %s", $rootScope.loggedUser.username);
				User.logout(function(data, status) {
					Log.debug("User logout success! data: %o, status: %o", data, status);
					$rootScope.loggedUser = {};
					$window.location.href = $rootScope.appBaseUrl;
				}, function(data, status, script, func) {
					Log.debug("User logout error! data: %o, status: %o", data, status);
					ErrorHandling.handle(data, status, script, func);
				});
			};
} ]);