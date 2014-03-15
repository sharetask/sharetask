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
			Log.debug("filter: %o", filter);
			var newTasks = [];
			angular.forEach(tasks, function(task) {
				
				if (task.title.toLowerCase().indexOf(filter.searchString.toLowerCase()) != -1
						|| task.description.toLowerCase().indexOf(filter.searchString.toLowerCase()) != -1) {
					
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
						var todayDate = new Date();
						var dueDate = new Date(task.dueDate);
						var todayString = todayDate.getFullYear()+"-"+("0"+todayDate.getMonth()).substr(-2)+"-"+todayDate.getDate();
						var dueDateString = dueDate.getFullYear()+"-"+("0"+dueDate.getMonth()).substr(-2)+"-"+dueDate.getDate();
						//Log.debug("today: %s, duedate: %s", todayString, dueDateString);
						if (todayString == dueDateString && task.state != "FINISHED" && task.state != "TERMINATED" && task.state != "FAILED") {
							if (filter.tag == '') {
								newTasks.push(task);
							}
							else if (!jQuery.isEmptyObject(task.tags) && task.tags.indexOf(filter.tag) != -1) {
								newTasks.push(task);
							}
						}
					}
					else if (filter.queue == "MY_OVERDUE") {
						var todayDate = new Date();
						var dueDate = new Date(task.dueDate);
						var todayString = todayDate.getFullYear()+"-"+("0"+todayDate.getMonth()).substr(-2)+"-"+todayDate.getDate();
						var dueDateString = dueDate.getFullYear()+"-"+("0"+dueDate.getMonth()).substr(-2)+"-"+dueDate.getDate();
						//Log.debug("today: %s, duedate: %s", todayString, dueDateString);
						if (task.dueDate !== null && todayString > dueDateString && task.state != "FINISHED" && task.state != "TERMINATED" && task.state != "FAILED") {
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
				}
			});
			return newTasks;
		};
	}])
	.filter('queueTasksCount', ['Workspace', function(Workspace) {
		return function(tasks, queueName) {
			//Log.debug("queueTasksCount: queueName: %s", queueName);
			var count = 0;
			if (!jQuery.isEmptyObject(tasks)) {
				angular.forEach(tasks, function(task) {
					if (queueName == "MY_PENDING") {
						if (task.state != "FINISHED" && task.state != "TERMINATED" && task.state != "FAILED") {
							count++;
						}
					}
					else if (queueName == "MY_TODAY") {
						var todayDate = new Date();
						var dueDate = new Date(task.dueDate);
						var todayString = todayDate.getFullYear()+"-"+("0"+todayDate.getMonth()).substr(-2)+"-"+todayDate.getDate();
						var dueDateString = dueDate.getFullYear()+"-"+("0"+dueDate.getMonth()).substr(-2)+"-"+dueDate.getDate();
						//Log.debug("today: %s, duedate: %s", todayString, dueDateString);
						if (todayString == dueDateString && task.state != "FINISHED" && task.state != "TERMINATED" && task.state != "FAILED") {
							count++;
						}
					}
					else if (queueName == "MY_OVERDUE") {
						var todayDate = new Date();
						var dueDate = new Date(task.dueDate);
						var todayString = todayDate.getFullYear()+"-"+("0"+todayDate.getMonth()).substr(-2)+"-"+todayDate.getDate();
						var dueDateString = dueDate.getFullYear()+"-"+("0"+dueDate.getMonth()).substr(-2)+"-"+dueDate.getDate();
						//Log.debug("today: %s, duedate: %s", todayString, dueDateString);
						if (task.dueDate !== null && todayString > dueDateString && task.state != "FINISHED" && task.state != "TERMINATED" && task.state != "FAILED") {
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
			//Log.debug("orderName: %o", input);
			if (input == "TASK_DUE_DATE") { return "_DueDate_"; }
			else if (input == "TASK_TITLE") { return "_Name_"; }
			else if (input == "TASK_AUTHOR") { return "_Author_"; }
			else if (input === undefined || input === null) { return "__"; }
		};
	}])
	.filter('taskQueueName', ['Workspace', function(Workspace) {
		return function(input) {
			//Log.debug("orderName: %o", input);
			if (input == "MY_PENDING") { return "_PendingTasks_"; }
			else if (input == "MY_TODAY") { return "_TodayTasks_"; }
			else if (input == "MY_OVERDUE") { return "_OverdueTasks_"; }
			else if (input == "MY_HIGH_PRIORITY") { return "_HighPriorityTasks_"; }
			else if (input == "MY_COMPLETED") { return "_CompletedTasks_"; }
			else if (input === undefined || input === null) { return "__"; }
		};
	}])
	.filter('priorityName', ['Workspace', function(Workspace) {
		return function(input) {
			//Log.debug("priorityName: %o", input);
			if (input == "LOW") { return "_Low_"; }
			else if (input === "MEDIUM") { return "_Standard_"; }
			else if (input === "HIGH") { return "_High_"; }
			else if (input === undefined || input === null) { return "__"; }
		};
	}])
	.filter('hash', [function() {
		return function(input) {
			//Log.debug("hash: %o", input);
			return hex_md5(input.toLowerCase());
		};
	}])
	.filter('urlEncode', [function() {
		return function(input) {
			//Log.debug("urlEncode: %o", input);
			return encodeURIComponent(input);
		};
	}])
	.filter('urlDecode', [function() {
		return function(input) {
			//Log.debug("urlDecode: %o", input);
			return decodeURIComponent(input);
		};
	}])
	.filter('nl2br', [function() {
		return function(input) {
			Log.debug("nl2br: %s", input);
			if (input) {
				return input.replace(/\n/g, '<br />');
			}
			else {
				return input;
			}
		};
	}])
	.filter('dateString', [function() {
		return function(input) {
			//Log.debug("dateString: %o", input);
			var input = new Date(input);
			return input.getFullYear()+"-"+("0"+(input.getMonth() + 1)).substr(-2)+"-"+input.getDate();
		};
	}])
	.filter('myDate', ['$filter', function($filter) {
		return function(input, format) {
			//Log.debug("myDate: %o, format: %s", input, format);
			var inDateString = $filter('dateString')(input);
			//Log.debug("inDateString: %s", inDateString);
			var todayString = $filter('dateString')(new Date());
			//Log.debug("todayString: %s", todayString);
			var yesterdayDate = new Date();
			yesterdayDate.setDate(yesterdayDate.getDate() - 1);
			var yesterdayDateString = $filter('dateString')(yesterdayDate);
			//Log.debug("yesterdayDateString: %s", yesterdayDateString);
			var tommorowDate = new Date();
			tommorowDate.setDate(tommorowDate.getDate() + 1);
			var tommorowDateString = $filter('dateString')(tommorowDate);
			//Log.debug("tommorowDateString: %s", tommorowDateString);
			if (inDateString == yesterdayDateString) {
				//Log.debug("formatedDate: %s", $filter('i18n')('_YesterdayDate_'));
				return $filter('i18n')('_YesterdayDate_');
			}
			if (inDateString == todayString) {
				//Log.debug("formatedDate: %s", $filter('i18n')('_TodayDate_'));
				return $filter('i18n')('_TodayDate_');
			}
			if (inDateString == tommorowDateString) {
				//Log.debug("formatedDate: %s", $filter('i18n')('_TomorrowDate_'));
				return $filter('i18n')('_TomorrowDate_');
			}
			else {
				//Log.debug("formatedDate: %s", $filter('date')(input, format));
				return $filter('date')(input, format);
			}
		};
	}])
	;
