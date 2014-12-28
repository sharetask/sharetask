SET SQL_SAFE_UPDATES = 0;
set @username='xxx';

-- delete comments
delete from COMMENT where TASK_ID in (
select task_id from TASK where WORKSPACE_ID in (
select workspace_id from WORKSPACE where OWNER_USER_NAME = @username
));
-- delete tasks
delete from TASK where WORKSPACE_ID in (
select workspace_id from WORKSPACE where OWNER_USER_NAME = @username
);
-- update assigne to owner
update TASK as t set t.ASSIGNEE_USER_NAME = ( 
select w.OWNER_USER_NAME from WORKSPACE as w where w.ID = t.WORKSPACE_ID
) where t.ASSIGNEE_USER_NAME = @username;

delete from WORKSPACE_MEMBER where USER_NAME = @username;
delete from USER_AUTHENTICATION where USER_NAME = @username;
delete from USER_ROLE where USER_NAME = @username;
delete from USER_INFORMATION where USER_NAME = @username;

