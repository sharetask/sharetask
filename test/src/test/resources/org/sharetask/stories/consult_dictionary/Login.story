Scenario: Login
Given Open login page
Given User michal.bocek@gmail.com with password soso7979
When Pushing login button
Then User is logged in
