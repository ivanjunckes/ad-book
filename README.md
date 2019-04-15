This project has an External Book API to help you search for external books using [Ice and Fire API](https://anapioficeandfire.com/Documentation#books) and also it has an API to create, update, read, show and delete your own books.

# Pre-requisites 
- Java 8
- Git
- Apache Maven 3.5.0

Please follow the instructions below, there are a few steps to pull, install and run the project. # 

# Installation Instructions

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

# Testing Instructions

## Testing External Books
This request will look for a book with name Game of Thrones in the external api Ice and Fire.
```
GET http://localhost:8080/api/external-books?name=A Game of Thrones
```

## Testing Books
This request will look for all the books in the database. 
```
GET http://localhost:8080/api/v1/books
```

This API supports GET, POST, PATCH, DELETE. To be able to view books you need to create them using POST.


