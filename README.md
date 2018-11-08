# Mindful Meal Planner 

## Initial Screen Activity
This is the initial activity that appears on the user's first use of the app. Here, the user will be asked to input some information about themselves: Their name, gender, and approximate current meal plan.


![alt text](ReadmePic/initScreen.PNG)


## Dashboard Activity
This dashboard activity provides the user a visual as well as information about their current meal plan and its impact on the environment.


![alt text](ReadmePic/dashboard.PNG)


The user can alternate between two pie charts by swiping right or left. The blue pie chart indicates the percentage of each portion in the user's diet. The red pie chart indicates how much each portion contributes to the production of CO2e.


![alt text](ReadmePic/dashboardSwipeLeft.PNG)

On pressing the edit icon, the user may change the name of their plan if they wish.


![alt text](ReadmePic/changeNamedashboard.PNG)


 
The user is also given information about approximately how many metric tonnes of CO2e are created from the current food plan, and the equalancy of how many kilometers an average car could drive to produce that amount.
The user's current plan is also displayed. 

## Improve Plan Activity
This activity is reached by pressing the 'Improve' button seen in the dashboard. Here, a new plan is displayed for the user. This plan is generated by an algorithm that seeks to reduce the user's CO2e by 10%. To do this, it uses a scaling factor to slightly adjust each of the user's food portions, starting with those which produce the most CO2e.
A toast will be generated to inform the user how much CO2e could be reduced if all of Metro Vancouver were to use the same plan.

![alt text](ReadmePic/improveActivity.PNG)

If the user is satisfied with this new plan. The user can press either 'save' or 'save as', where the latter button allows the ability to change the plan name.
If the user wishes to edit the plan, the edit button will reveal sliders and update the amount of CO2e saved in real time. If the plan produces a greater amount of CO2e than the previous plan, the pie graph will turn red as a warning indication.

![alt text](ReadmePic/editImprovePlan.PNG)



## Side bar
A screenshot of the sidebar, containing access to the settings activity which contains the about page activity and the dashboard activity. The sidebar can be reached by either the icon on the top left corner, or by swiping from the left side of the application.


![alt text](ReadmePic/sidebar2.PNG)


## Settings 
This activity is accessed through the sidebar. Currently, it contains access to the about page activity, as well as a back button in the top left corner, taking the user back to the dashboard.


![alt text](ReadmePic/settings.PNG)


## About Page Activity
The about page activity gives the user an overview of the app: The carbon footprint caused by food, and how changing the foods we eat is environmentally impactful.


![alt text](ReadmePic/aboutActivity.PNG)

