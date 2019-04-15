This project has a book api to help you search for external books using [Ice and Fire API](link:https://anapioficeandfire.com/Documentation#books) and to create, update, read, show and delete your own books.

# Pre-requisites 
Java 8
Git
Apache Maven 3.5.0

Please follow the instructions below, there are a few steps to pull, install and run the project. # 

## Pull the project
```
git clone https://github.com/ivanjunckes/ad-book.git
```
After cloning the project go to the folder ad-book.

## Build and run the project
Build the project using maven with the command:
``` 
mvn clean install -DskipTests tomee:run
```

When the log below show up, the application is ready to be tested.
```
INFO [main] sun.reflect.DelegatingMethodAccessorImpl.invoke Server startup in 4882 m
```
