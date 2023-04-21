# OneManArmy

## Technological Stack:

Kotlin

Android Studio

FireBase

## Project Setup

In order to run this app, you have to open it in Android Studio, press the "Build" button, and then the "Run" button. This app was created using API 30 and the emulated platform is Google Pixel 3, so we suggest using the same API and platform that we did.

## About the App:

OneManArmy is an app created for sole proprietors to manage their business and for usual users to find a service that they need. This app lets business owners to register their businesses in the app, find new clients, manage their appointments, and send the receipts of finished work to their clients.

## Implementation:

Our app uses FireBase for back-end management. All user-related data, like email, password, etc. is stored in FireBase and pulled to front-end by request.

In order to create the receipt scanner, we have implemented the OpenCV library. This library allows us to scan the receipt, save it as PDF, and then either save it on your phone or send it to the client via e-mail.

For making appointments, we used the Material-Calendar library that let us create appointments for specific day and time and track them

## Main Features:

### Business Owner:

1) Ability to view and manage appointmets with clients
2) Ability to scan receipts to keep track of business expenses for itemized deductions
3) Ability to send receipts to clients as proof of finished work
4) Ability to set up profile with a phone number, picture and a list of skills

### Client:

1) Ability to find sole proprietors
2) Ability to make appointments with business owners
