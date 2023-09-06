# Rental App

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 14.1.0.

### Start the project

Git clone:

> git clone https://github.com/OpenClassrooms-Student-Center/P3-Full-Stack-portail-locataire

Go inside folder:

> cd front_angular_connected_to_spring_boot_api

### Install dependencies:

> npm install

Launch Front-end:

> npm run start;

Launch Back-end

> right click on Api-Backend-Chatop > Run As > Spring Boot App

RentalApp URL ( you need to register ) :

> "http://localhost:4200/"
> refresh page for login

### SWAGGER DOCUMENTATION

> To access the Swagger Documentation, go to http://localhost:3001/login.

> Sign in with your credentials.

To make a request via Swagger, a Bearer Token is required. You will first need to obtain this token using Postman.

### Obtain the Bearer Token with Postman

> Open Postman.

> Create a new request and set the HTTP method to POST.

> In the URL field, enter http://localhost:3001/api/auth/login.

> Navigate to the Body tab, select raw, and choose JSON (application/json) from the dropdown menu.

> In the text area that appears, enter your JSON payload, for example:

{
  "email": "your.email@example.com",
  "password": "yourPassword"
}

> Click on the Send button to send the request.

> Copy the Bearer Token from the response you receive.

> Return to the Swagger documentation.

> Use the option to add the Bearer Token (often this is done by clicking an Authorize button or similar).

> Paste in the Bearer Token and validate.

You are now authorized to make secure requests via Swagger.
