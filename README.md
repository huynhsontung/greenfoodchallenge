# Mindful Meal Planner 
### Team members:
1. Son Tung Huynh, <tungh@sfu.ca>
2. Peter Tran, <ttt13@sfu.ca>
3. Jacky Cao, <jjc44@sfu.ca>
4. Jesse Zhou, <zza112@sfu.ca>
5. Arlen Xu, <arlenx@sfu.ca>


## Initialization
To initialize, the user is allowed to log in with their valid email, only then their password will be required. 
![alt text](ReadmePic/Sprint3/welcomesignin.PNG) | ![alt text](ReadmePic/Sprint3/welcomeemail.PNG)


The user is later then asked to select their gender and initial preset meal plan which the user may adjust more precisely later on.


![alt text](ReadmePic/Sprint3/welcomegreetings.PNG) | ![alt text](ReadmePic/Sprint3/welcomepresetplan.PNG) | ![alt text](ReadmePic/Sprint3/welcomeadjust.PNG)

## Tutorial
Tutorial pop-up will welcome first time users to get them started on how to use the application. Users may revisit the tutorial by clicking on the question mark icon found in each fragment.

![alt text](ReadmePic/Sprint3/tutorialdashboard.PNG) ![alt text](ReadmePic/Sprint3/tutorialimprove.PNG)


## Dashboard Activity
The dashboard provides the user a visual representation of their saved meal plans and its impact on the environment.

![alt text](ReadmePic/Sprint3/dashboardblue.PNG) ![alt text](ReadmePic/Sprint3/dashboardred.PNG) ![alt text](ReadmePic/Sprint3/dashboardplanlist.PNG)


The user can alternate between two pie charts by swiping right or left. The blue pie chart indicates the percentage of each portion in the user's diet. The red pie chart indicates how much each portion contributes to the production of CO2e.
The user may change the name of the meal plan by pressing the edit icon.
The user is given the approximate metric tonnes of CO2e created from the selected food plan, as well as the equivalence of an average car drive to produce that amount in kilometers.
The user's current plan is also displayed. 

## Improve Plan
The improve plan feature can be activated by pressing the 'Improve' button seen in the dashboard. A new meal plan that reduces the user's CO2e by 10% is displayed. The new plan is generated by an algorithm that uses a scaling factor to slightly adjusting each food portions, starting with those which produce the most CO2e.
A celebratory toast will be generated to inform the user how much CO2e could be reduced if all of Metro Vancouver were to use the same plan.

![alt text](ReadmePic/Sprint3/improve.PNG)

If the user is satisfied with this new plan. The user can press either 'save' or 'save as', where the latter button allows the ability to change the plan name.
If the user wishes to edit the plan, the user may use the sliders and update the amount of CO2e saved in real time. If the plan produces a greater amount of CO2e than the previous plan, the pie graph will turn red as a warning indication.


## Pledge 
The pledge activity has two fragments. The 'My Pledge' fragment allows the user to make a pledge of how many kilograms of CO2 they will commit to saving in a week. The 'Discover' fragment allows the user to see other users and their pledges, as well as various statistics on the pledges. The user may also filter pledges by municipality.

![alt text](ReadmePic/Sprint3/pledgemypledge.PNG) ![alt text](ReadmePic/Sprint3/pledgediscover.PNG)

## Meal Tracker
This fragment allows users to explore different green meals posted by other Vancouverites. The user may also upload their own green meal. In doing so, the user is required to enter meal details, and upload a photo.

![alt text](ReadmePic/Sprint3/mealtrackerfeed.PNG) ![alt text](ReadmePic/Sprint3/mealtrackeraddmeal.PNG) ![alt text](ReadmePic/Sprint3/mealtrackeraddphoto.PNG)

The database keeps record of green restaurants and their menus across Metro Vancouver. Upon selecting these items, tags will be set, so that the meals may be searched by other users.

## Profile
Users may view their profile. Upon clicking on their information, they can enter a profile account information activity, where the profile picture may be edited.

![alt text](ReadmePic/Sprint3/profile.PNG) ![alt text](ReadmePic/Sprint3/profileaccountinfo.PNG)

## Settings 
In settings, users have the option to log out (which takes users back to the dashboard) and view the about page.

![alt text](ReadmePic/Sprint3/settingsview.PNG) ![alt text](ReadmePic/Sprint3/settingsabout.PNG)


