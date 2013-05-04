'use strict';

/* Controllers */
angular.module('shareTaskApp.controllers', ['ui']).
	controller('AppCtrl', ['$scope', 'Workspace', function($scope, Workspace) {
		
		/**
		 * Loading all workspaces from server.
		 */
		$scope.workspaces = Workspace.findAll(function(workspaces) {
				console.log("Loaded workspaces from server: %o", workspaces);
				if (workspaces.length) {
					$scope.setActiveWorkspace(workspaces[0].id);
				}
			}, function(response) {
				console.log("response: %o", response);
			});
		
		/**
		 * Setting active workspace.
		 * Event is propagated into Workspace controller for loading workspace tasks from server.
		 * @param {number} id - Workspace ID.
		 */
		$scope.setActiveWorkspace = function(id) {
			console.log("Setting active workspace (id: %s)", id);
			$scope.$broadcast('EVENT_SET_ACTIVE_WORKSPACE', {workspaceId: id});
		};
	}])
	.controller('WorkspaceCtrl', ['$scope', '$filter', 'Workspace', 'Task', 'LocalStorage', function($scope, $filter, Workspace, Task, LocalStorage) {
		
		$scope.viewPanelTaskFilter = true;
		$scope.activeWorkspaceId;
		$scope.activeTask;
		$scope.taskEditMode = '';
		$scope.filter = {'queue': 'MY_PENDING', 'tag': '', 'searchString': '', 'orderBy': 'TASK_DUE_DATE'};
		$scope.tags = [];
		var taskFilter = $filter('filterTasks');
		
		/**
		 * Receive event 'EVENT_SET_ACTIVE_WORKSPACE' for setting active workspace.
		 * After receiving of event all workspace tasks are loaded from server.
		 */
		$scope.$on('EVENT_SET_ACTIVE_WORKSPACE', function(event, data) {
			console.log("Received broadcast message 'EVENT_SET_ACTIVE_WORKSPACE' (data: %o)", data);
			$scope.activeWorkspaceId = data.workspaceId;
			$scope.loadTasks();
		});
		
		/**
		 * Load all tasks for active workspace.
		 */
		$scope.loadTasks = function() {
			console.log("Load all tasks for active workspace (id: %s)", $scope.activeWorkspaceId);
			$scope.allTasks = Workspace.getTasks({workspaceId: $scope.activeWorkspaceId}, function(tasks) {
					console.log("Loaded workspace tasks from server: %o", tasks);
					$scope.setTaskFilterQueue($scope.filter.queue);
				}, function(response) {
					console.log("Workspace tasks error: %o", response);
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
			if (!jQuery.isEmptyObject(this.tasks)) {
				// set active task
				this.setActiveTask(this.tasks[0].id);
				// parse tags
				var taskTags = new Array();
				angular.forEach(this.tasks, function(task) {
					if (task.tags.length) {
						angular.forEach(task.tags, function(tag) {
							if (taskTags.indexOf(tag) == -1) {
								taskTags.push(tag);
							}
						});
					}
				});
				$scope.tags = taskTags;
				console.log("Tags parsed for queue: %o", $scope.tags);
			}
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
		};
		
		/**
		 * Filter tasks.
		 * Presented tasks are filtered and ordered according current filter settings.
		 */
		$scope.filterTasks = function() {
			console.log("Filter tasks: %o", $scope.filter);
			$scope.tasks = taskFilter($scope.filter, this.allTasks);
			$scope.tasks = $filter('orderBy')(this.tasks, this.orderTasks);
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
		 * Setting active task.
		 * Active task data is loaded from server.
		 * @param {number} taskId - Task ID.
		 */
		$scope.setActiveTask = function(taskId) {
			console.log("Set active task (id: %s)", taskId);
			$scope.activeTask = Task.findById({id: taskId}, function(data) {
					console.log("Task.findById: %o", data);
			}, function(response) {
					console.log("response: %o", response);
			});
			console.log("Active task: %o", this.activeTask);
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
		 * Removing tag from the task.
		 * Task is stored to server.
		 * @param {number} taskId - Task ID.
		 * @param {string} tag - Tag name.
		 */
		$scope.addTaskTag = function() {
			console.log("Add new tag to task, id: %s, tag: %s", $scope.activeTask.id, $scope.newTag);
			$scope.activeTask.tags.push($scope.newTag);
			$scope.updateTask($scope.activeTask);
			$scope.taskEditMode = '';
			$scope.newTag = '';
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
		 * Setting task edit mode.
		 * Represents task attribute which is currently in editing mode.
		 * @param {string} mode - Edit mode.
		 */
		$scope.setTaskEditMode = function(mode) {
			console.log("Switch task edit mode to: %s", mode);
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
			$scope.activeTask = task;
			var result = $.grep(this.tasks, function(e) {
				return e.id == task.id;
			});
			result[0].title = task.title;
			result[0].description = task.description;
			result[0].tags = task.tags;
			//console.log("tasks, %o", this.tasks);
			// TODO - call REST API update
			
			LocalStorage.store($scope.activeWorkspaceId, $scope.allTasks);
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
			console.log("Change task priority (id: %s, priority: %s)", $scope.activeTask.id, $scope.activeTask.priority);
			if ($scope.activeTask.priority == 'LOW') { $scope.activeTask.priority = 'STANDARD'; }
			else if ($scope.activeTask.priority == 'STANDARD') { $scope.activeTask.priority = 'HIGH'; }
			else if ($scope.activeTask.priority == 'HIGH') { $scope.activeTask.priority = 'LOW'; }
			$scope.updateTask($scope.activeTask);
		};
		
		/**
		 * Add new task.
		 * User adds task title only. All others attributes are set to default values.
		 * Task data are stored to server.
		 */
		$scope.addTask = function() {
			console.log("Add new task (taskTitle: %s)", $scope.newTaskTitle);
			var task = {title: $scope.newTaskTitle, createdBy: 'mmoravek', createdOn: new Date(), priority: 'STANDARD', comments: 0};
			console.log("Task: %o", task);
			// TODO Store new task to server
			
			$scope.allTasks.push(task);
			$scope.filterTasks();
			$scope.newTaskTitle = '';
		};
		
		/**
		 * Assign active task.
		 * Task data are stored to server.
		 */
		$scope.assignTask = function() {
			console.log("Assign task (id: %s)", $scope.activeTask.id);
			$scope.activeTask.state = 'ASSIGNED';
			$scope.updateTask($scope.activeTask);
		};
		
		/**
		 * Forward active task to another workspace member.
		 * Task data are stored to server.
		 */
		$scope.forwardTask = function(userId) {
			console.log("Forward task (userId: %s)", userId);
			$scope.updateTask($scope.activeTask);
		};
		
		/**
		 * Delete active task.
		 * Task data are stored to server.
		 */
		$scope.deleteTask = function() {
			console.log("Delete task (id: %s)", $scope.activeTask.id);
			$scope.updateTask($scope.activeTask);
		};
		
		/**
		 * Complete active task.
		 * Task data are stored to server.
		 */
		$scope.completeTask = function() {
			console.log("Complete task (id: %s)", $scope.activeTask.id);
			$scope.activeTask.state = 'FINISHED';
			$scope.updateTask($scope.activeTask);
		};
		
		/**
		 * Move active task to another workspace.
		 * Task data are stored to server.
		 */
		$scope.moveTask = function() {
			console.log("move task (id: %s)", $scope.activeTask.id);
		};
	}])
	.controller('AdminCtrl', ['$scope', 'Workspace', function($scope, Workspace) {
		
		
	}])
	.controller('UserCtrl', ['$scope', 'Workspace', function($scope, Workspace) {
		
		
	}])
	;