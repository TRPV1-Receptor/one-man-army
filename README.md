# OneManArmy

## Project Description

One Man Army is a mobile application that allows small business owners to streamline their record-keeping by digitizing receipts and invoices, tracking expenses, and monitoring their finances
______________________________________________________________

## Development Team
* Michael Trofimov
* Eliashazboun
* Alejandro Cruz-Bautista
* Anthony Miranda
* Deidre240
* ayarbray

## Recent Updates
* Quick fix for passing the wrong ID through intent
* Receipts now dynamically populate necessary fields, hotfix on ownderdash
* Pulling currently logged in user in the dashboard activity and then passing it as an OwnerModel() to all the other activities. Each activity now has access to the info of the currently logged in user 
* Search implemented 
* Created a FAQ button with questions and answers for client side 
* Second registration screen working now, routing to profile setup 

## Technological Stack:

Kotlin

Android Studio

FireBase

## Installation

``` git clone https://gitlab.com/cs-department-ecu/csci-4230-spring-2023/section-001/onemanarmy/oneManArmy.git ```

## Project Setup

To run this app, you have to open it in Android Studio, press the "Build" button, and then the "Run" button. This app was created using Android API 30, and the platform that is being emulated is Google Pixel 3, so we suggest using the same API and platform we did.


## About the App:

OneManArmy is an app created for sole proprietors to manage their businesses and for usual users to find a service they need. This app lets business owners register their businesses in the app, find new clients, manage their appointments, and send their clients the receipts of finished work.


## Main Features:

### Business Owner:

1) Ability to view and manage appointments with clients
2) Ability to create receipts for completed orders
3) Ability to send receipts to clients as proof of finished work
4) Ability to set up a profile with a phone number, picture, and a list of skills

### Client:

1) Ability to find sole proprietors by their names
2) Ability to make appointments with business owners


## Implementation:

Our app uses FireBase for back-end management. All user-related data, like e-mail, password, etc., is stored in FireBase and pulled to the front end by request.

There are two separate dashboards for the business owner and client. The main difference is that the client's dashboard has a "Search" page instead of a "Receipt Creator."

On the "Appointments" page, clients and business owners can make appointments with each other. The dates are demonstrated in the calendar, and we used the Material-Calendar library that lets us create appointments for specific days and times and track them. Days with the appointments on them are highlighted on the calendar.

On the "Account" page, the client can edit his basic information: name, e-mail, phone number, and address. From the business owner's side, the "Profile" page is similar, but it is possible to change the name of their business and add their skills.

On the "Receipt Creator" page, the business owner can create receipts for the orders that they complete. We made our receipt creator work in such a way that it gets saved as a PDF. After that, the business owner can use it either for keeping track of itemized deductions or for sending it to the clients as proof of finished work via e-mail. By default, the receipts are saved in the Downloads folder.

On the "Search" page, the client can look for the service providers; from there, they can access their profiles to make an appointment. The filter used for the search is the business owner's name.

On the "Settings" page, the user can change the color of the app to a dark theme, contact the project's creators, read through the frequently asked questions, and send us an e-mail regarding any issues related to the app.

Also, there is an option to change between client and business owner accounts by pressing the "Private Account" button.


## Issues:

1) We were planning to use the OpenCV library for "Receipt Creator," so there would be no need to input all the receipt information manually. Initially, we planned to scan the printed receipts. Eventually, we decided to go with manual input because it was less complicated to work with and to scan the receipt, we had to import the whole OpenCV library, which complicated things even further.


## Contributors:

Alejandro Cruz-Batista. GitHub: https://github.com/itsAlex58
Elias Hazboun. GitHub: 
Anthony Miranda. GitHub: https://github.com/anthonym553
Mihail Trofimov. GitHub: https://github.com/Tr0f1k
Deidre Whitehead. GitHub: https://github.com/DreW3325
Austin Yarbray. GitHub: https://github.com/ayarbray123

## Contributing

If you would like to contribute to the development of One Man Army, please follow these steps:

1. Fork the repository from GitLab.
2. Create a new branch for your changes.
3. Make your changes and commit them to the new branch.
4. Push the new branch to your forked repository.
5. Create a pull request to merge your changes into the main branch of the main repository.
