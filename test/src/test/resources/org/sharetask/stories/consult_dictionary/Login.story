Scenario: Login
Given Open login page
Given User support@shareta.sk with password password
When Pushing login button
Then User is logged in
