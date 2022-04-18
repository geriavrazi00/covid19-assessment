# Java Developer Assessment

## Description
Write an application that calculates the correlation coefficient between the percentage of people that died and got vaccinated of COVID-19 given a continent or all available countries using the API as described on: https://github.com/M-Media-Group/Covid-19-API.

You will be asked to provide a demonstration of the functionality of the application including a code walkthrough via a screen sharing session during the interview.

## Requirements
- Write in Java or any other language targeting the JVM
- Write production-ready code
- Free to use any libraries and/or frameworks
- Document how to run the application
- Publish the source code into GitHub (using your own personal account) and share it with us

**_Disclaimer: The data used for this assessment is made available through a public API. Please follow the usage guidelines as prescribed. Accenture claims no ownership or responsibility to this data and its usage._**

# How to run
1. Download the code as ZIP or clone the repository locally. If downloaded, extract the folder somewhere.
2. You should have installed and configured maven and java in your machine. The application uses Java 11.
3. Inside the root of the project run the command: mvn clean install
4. After seeing the message in green "BUILD SUCCESS", run the following command: mvn spring-boot:run
5. The application should be up and running in a few seconds. 
6. To see the available APIs of the application, you can check the Swagger link: http://localhost:8080/swagger-ui/index.html
7. You may be able to send requests directly from the Swagger UI, Postman or any other API client.
8. The URL that achieves the functionality required is: http://localhost:8080/covid-data/correlation . It does not have any required parameters, so the request can be sent as is. There are 2 optional parameters though, continents and countries. They can be either 1 or more than 1 element comma separated. The application will use the data given to give as response the countries included in the calculation and the resulting correlation coefficient. 