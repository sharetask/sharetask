Scenario: Login
Given Open login page
Given User support@shareta.sk with password password
When Pushing login button
Then User is logged in

Scenario: Logout
Given Open workspace page
When Pushing logout button
Then User is logged out
