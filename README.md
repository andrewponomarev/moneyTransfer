[![Build Status](https://travis-ci.org/andrewponomarev/moneyTransfer.svg?branch=master)](https://travis-ci.org/andrewponomarev/moneyTransfer)
# [Revolut Backend Test](https://drive.google.com/file/d/1Rhk07_MT5WP_5f-lF0LxkJKt5pPM8SKd/view)
Design and implement a RESTful API (including data model and the backing implementation)
for money transfers between accounts.

## Explicit requirements:
1. You can use Java, Scala or Kotlin.
2. Keep it simple and to the point (e.g. no need to implement any authentication).
3. Assume the API is invoked by multiple systems and services on behalf of end users.
4. You can use frameworks/libraries if you like (except Spring), but don't forget about
requirement #2 – keep it simple and avoid heavy frameworks.
5. The datastore should run in-memory for the sake of this test.
6. The final result should be executable as a standalone program (should not require
a pre-installed container/server).
7. Demonstrate with tests that the API works as expected.

## Implicit requirements:
1. The code produced by you is expected to be of high quality.
2. There are no detailed requirements, use common sense.

## Used libraries :
    - Log4j - fast and simple logging
    - Guice - lightweight dependency injection
    - Spark Web - a simple web framework
    - HSQLDB - in-memory database
    - Junit - a well-known and simple framework for unit tests

## How to run:
1. Run `mvn clean install`
2. Go to `target` and run there `java -jar moneyTransfer-1.0.0-shaded`. 
3. The application is ready for usage on url `http://localhost:4567/`.

## API
## Indices

* [Default](#default)

  * [Add transfer](#1-add-transfer)
  * [Get transfer by id](#2-get-transfer-by-id)
  * [Get list of transfers](#3-get-list-of-transfers)
  * [Delete account](#4-delete-account)
  * [Add account](#5-add-account)
  * [Get account by id](#6-get-account-by-id)
  * [Get list of accounts](#7-get-list-of-accounts)


--------


## Default



### 1. Add transfer



***Endpoint:***

```bash
Method: POST
Type: 
URL: {{host}}/transfers
```



### 2. Get transfer by id



***Endpoint:***

```bash
Method: GET
Type: 
URL: {{host}}/transfers/1
```



### 3. Get list of transfers



***Endpoint:***

```bash
Method: GET
Type: 
URL: {{host}}/transfers
```



### 4. Delete account



***Endpoint:***

```bash
Method: DELETE
Type: RAW
URL: {{host}}/accounts/1
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Content-Type | application/json |  |



***Body:***

```js        
{
	"value":1000
}
```



### 5. Add account



***Endpoint:***

```bash
Method: POST
Type: RAW
URL: {{host}}/accounts
```


***Headers:***

| Key | Value | Description |
| --- | ------|-------------|
| Content-Type | application/json |  |



***Body:***

```js        
{
	"value":1000
}
```



### 6. Get account by id



***Endpoint:***

```bash
Method: GET
Type: 
URL: {{host}}/accounts/1
```



### 7. Get list of accounts



***Endpoint:***

```bash
Method: GET
Type: 
URL: {{host}}/accounts
```