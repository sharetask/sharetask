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

/* Filters */
angular.module('shareTaskApp.filters', []).
	filter('filterTasks', ['Workspace', function(Workspace) {
		return function(filter, tasks) {
			console.log("filter: %o", filter);
			var newTasks = [];
			angular.forEach(tasks, function(task) {
				if (filter.queue == "MY_PENDING") {
					if (task.state != "FINISHED" && task.state != "TERMINATED" && task.state != "FAILED") {
						if (filter.tag == '') {
							newTasks.push(task);
						}
						else if (!jQuery.isEmptyObject(task.tags) && task.tags.indexOf(filter.tag) != -1) {
							newTasks.push(task);
						}
					}
				}
				else if (filter.queue == "MY_TODAY") {
					if (new Date().toDateString() == new Date(task.dueDate).toDateString() && task.state != "FINISHED" && task.state != "TERMINATED" && task.state != "FAILED") {
						if (filter.tag == '') {
							newTasks.push(task);
						}
						else if (!jQuery.isEmptyObject(task.tags) && task.tags.indexOf(filter.tag) != -1) {
							newTasks.push(task);
						}
					}
				}
				else if (filter.queue == "MY_OVERDUE") {
					if (new Date().toDateString() > new Date(task.dueDate).toDateString() && task.state != "FINISHED" && task.state != "TERMINATED" && task.state != "FAILED") {
						if (filter.tag == '') {
							newTasks.push(task);
						}
						else if (!jQuery.isEmptyObject(task.tags) && task.tags.indexOf(filter.tag) != -1) {
							newTasks.push(task);
						}
					}
				}
				else if (filter.queue == "MY_HIGH_PRIORITY") {
					if (task.priority == "HIGH" && task.state != "FINISHED" && task.state != "TERMINATED" && task.state != "FAILED") {
						if (filter.tag == '') {
							newTasks.push(task);
						}
						else if (!jQuery.isEmptyObject(task.tags) && task.tags.indexOf(filter.tag) != -1) {
							newTasks.push(task);
						}
					}
				}
				else if (filter.queue == "MY_COMPLETED") {
					if (task.state == "FINISHED") {
						if (filter.tag == '') {
							newTasks.push(task);
						}
						else if (!jQuery.isEmptyObject(task.tags) && task.tags.indexOf(filter.tag) != -1) {
							newTasks.push(task);
						}
					}
				}
			});
			return newTasks;
		};
	}])
	.filter('queueTasksCount', ['Workspace', function(Workspace) {
		return function(tasks, queueName) {
			//console.log("queueTasksCount: queueName: %s", queueName);
			var count = 0;
			if (!jQuery.isEmptyObject(tasks)) {
				angular.forEach(tasks, function(task) {
					if (queueName == "MY_PENDING") {
						if (task.state != "FINISHED" && task.state != "TERMINATED" && task.state != "FAILED") {
							count++;
						}
					}
					else if (queueName == "MY_TODAY") {
						if (new Date().toDateString() == new Date(task.dueDate).toDateString() && task.state != "FINISHED" && task.state != "TERMINATED" && task.state != "FAILED") {
							count++;
						}
					}
					else if (queueName == "MY_OVERDUE") {
						if (new Date().toDateString() > new Date(task.dueDate).toDateString() && task.state != "FINISHED" && task.state != "TERMINATED" && task.state != "FAILED") {
							count++;
						}
					}
					else if (queueName == "MY_HIGH_PRIORITY") {
						if (task.priority == "HIGH" && task.state != "FINISHED" && task.state != "TERMINATED" && task.state != "FAILED") {
							count++;
						}
					}
					else if (queueName == "MY_COMPLETED") {
						if (task.state == "FINISHED") {
							count++;
						}
					}
				});
			}
			return count;
		};
	}])
	.filter('orderName', ['Workspace', function(Workspace) {
		return function(input) {
			//console.log("orderName: %o", input);
			if (input == "TASK_DUE_DATE") { return "Due date"; }
			else if (input == "TASK_TITLE") { return "Name"; }
			else if (input == "TASK_AUTHOR") { return "Author"; }
		};
	}])
	.filter('taskQueueName', ['Workspace', function(Workspace) {
		return function(input) {
			//console.log("orderName: %o", input);
			if (input == "MY_PENDING") { return "Pending"; }
			else if (input == "MY_TODAY") { return "Today"; }
			else if (input == "MY_OVERDUE") { return "Overdue"; }
			else if (input == "MY_HIGH_PRIORITY") { return "High Priority"; }
			else if (input == "MY_COMPLETED") { return "Completed"; }
		};
	}])
	;
