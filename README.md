# Demo project
This is a demon project developed using spring boot and maven as a dependency management
with java 8 as microservices.
## Dependencies

* Spring boot parent version 2.1.3.RELEASE
* Spring security oauth2 version 2.3.6.RELEASE
* Hibernate core version 5.3.7.Final
* H2 database version 1.4.197
* Apache commons lang version 3.10
* Apache commons io version 2.6
* Json version 20180813
* Java 8
## Build and Run
Open terminal and navigate to project directory that you have downloaded and run below command to start each service.
**You have to start gateway service before starting other services**
#### API Gateway service
```java
#start app
mvn clean install -DskipTests -pl gateway spring-boot:start

#stop app
mvn clean install -DskipTests -pl gateway spring-boot:stop
```
#### Authorization service
```java
#start app
mvn clean install -DskipTests -pl authorization spring-boot:start

#stop app
mvn clean install -DskipTests -pl authorization spring-boot:stop
```
#### Product service
```java
#start app
mvn clean install -DskipTests -pl product spring-boot:start

#stop app
mvn clean install -DskipTests -pl product spring-boot:stop
```
#### Customer service
```java
#start app
mvn clean install -DskipTests -pl customer spring-boot:start

#stop app
mvn clean install -DskipTests -pl customer spring-boot:stop
```
## Usage
### Checking app is deployed sucessfullly
```sh
Open your browser and type localhost:8080 to visit eureka dashboard and all services
```
### Client id and secret
##### Note: Password and secret are encrypt with RSA public key before send to server 
```
client_id = mobile_app
client_secret = iIRSk4KPVR45/WadwgciZ+YGugrY1iJJVx4Kshx1RthDpwW5U3gsHvl6cQYntFKTqFcnikjdCxdZ0obA85cYTD3RVP09UDFC01z77Cp9q78eQo27WZsblmpJVhmWc+iA7lb47bQ7vTZt3q+jAbzKUSUctvxDasA+Sk9WFIgI/eEtz7t4V4hihyaaobxLdFyU1PhiLCgraNydr/xEe2qOhNrVuMB3NTSV31TmeQEELf0q+y0oXwe6HDgDKPO7azmJTz/Ro+1Ei3UZHgNOm7H5a+uPUHdhTsChTqurw0L4wkbMI4DcyeKnaXcsmOtTWq3DcOxHCRiTkkGSIpoyqTOF2Q==
```

### Self register
Use client id and secret as basic authorization
```sh
curl --request POST \
  --url http://localhost:8080/customer/register \
  --header 'Authorization: Basic bW9iaWxlX2FwcDppSVJTazRLUFZSNDUvV2Fkd2djaVorWUd1Z3JZMWlKSlZ4NEtzaHgxUnRoRHB3VzVVM2dzSHZsNmNRWW50RktUcUZjbmlramRDeGRaMG9iQTg1Y1lURDNSVlAwOVVERkMwMXo3N0NwOXE3OGVRbzI3V1pzYmxtcEpWaG1XYytpQTdsYjQ3YlE3dlRadDNxK2pBYnpLVVNVY3R2eERhc0ErU2s5V0ZJZ0kvZUV0ejd0NFY0aGloeWFhb2J4TGRGeVUxUGhpTENncmFOeWRyL3hFZTJxT2hOclZ1TUIzTlRTVjMxVG1lUUVFTGYwcSt5MG9Yd2U2SERnREtQTzdhem1KVHovUm8rMUVpM1VaSGdOT203SDVhK3VQVUhkaFRzQ2hUcXVydzBMNHdrYk1JNERjeWVLbmFYY3NtT3RUV3EzRGNPeEhDUmlUa2tHU0lwb3lxVE9GMlE9PQ==' \
  --header 'Content-Type: application/json' \
  --data '{
	"name": "Tony Stark",
	"phone": "099878741",
	"email": "test@gmail.com",
	"username": "test",
	"password": "E1y7EYbgFDM0ZcGSIpxe4VOAxs5QGlyI1B6YfxPqa95zIt09eiSNBivBeVrV7TRfCeA9qTbIJtAilo+453VodxeQmyBioQh1XFcrJyX9bsWQJbCOpPpNxTzhnxMv0gGLeCu7huxr09eNqZfTzx3i2xCq+FTlic/uu8IeS14xo9iA1JM+Xx1pMVobIrnqmvCvnmRgJagLIk06YdYPlgtsIoy2BwhYyS5wgkJn8BsbNoQbYxlTAnAyyNZ+IBgB9RgyHQ5UiNSv8mIk7hpKsXAbfCUh6CGegRLmj7joTNDKYDWtwWWf99y74/qi/QMq6Yfwgd3QjKt08iGY4YaKNtA3mA=="
}'

Success response
{"code":"S200","message":"Successful","data":{"customer_id":4}}
```
### Self register with photo upload
Use client id and secret as basic authorization
```sh
curl --request POST \
  --url http://localhost:8080/customer/register \
  --header 'Authorization: Basic bW9iaWxlX2FwcDppSVJTazRLUFZSNDUvV2Fkd2djaVorWUd1Z3JZMWlKSlZ4NEtzaHgxUnRoRHB3VzVVM2dzSHZsNmNRWW50RktUcUZjbmlramRDeGRaMG9iQTg1Y1lURDNSVlAwOVVERkMwMXo3N0NwOXE3OGVRbzI3V1pzYmxtcEpWaG1XYytpQTdsYjQ3YlE3dlRadDNxK2pBYnpLVVNVY3R2eERhc0ErU2s5V0ZJZ0kvZUV0ejd0NFY0aGloeWFhb2J4TGRGeVUxUGhpTENncmFOeWRyL3hFZTJxT2hOclZ1TUIzTlRTVjMxVG1lUUVFTGYwcSt5MG9Yd2U2SERnREtQTzdhem1KVHovUm8rMUVpM1VaSGdOT203SDVhK3VQVUhkaFRzQ2hUcXVydzBMNHdrYk1JNERjeWVLbmFYY3NtT3RUV3EzRGNPeEhDUmlUa2tHU0lwb3lxVE9GMlE9PQ==' \
  --header 'Content-Type: multipart/form-data; boundary=---011000010111000001101001' \
  --form 'json={"name":"tony","phone":"077553335","email":"kimchheng101@gmail.com","username":"tony","password":"iIRSk4KPVR45/WadwgciZ+YGugrY1iJJVx4Kshx1RthDpwW5U3gsHvl6cQYntFKTqFcnikjdCxdZ0obA85cYTD3RVP09UDFC01z77Cp9q78eQo27WZsblmpJVhmWc+iA7lb47bQ7vTZt3q+jAbzKUSUctvxDasA+Sk9WFIgI/eEtz7t4V4hihyaaobxLdFyU1PhiLCgraNydr/xEe2qOhNrVuMB3NTSV31TmeQEELf0q+y0oXwe6HDgDKPO7azmJTz/Ro+1Ei3UZHgNOm7H5a+uPUHdhTsChTqurw0L4wkbMI4DcyeKnaXcsmOtTWq3DcOxHCRiTkkGSIpoyqTOF2Q=="}' \
  --form 'photo=@C:\Users\jarvis\Documents\agito-formless.PNG'

Success response
{"code":"S200","message":"Successful","data":{"customer_id":4}}
```

### Login
Use client id and secret as basic authorization
```sh
curl --request POST \
  --url http://localhost:8080/auth/user/login \
  --header 'Authorization: Basic bW9iaWxlX2FwcDppSVJTazRLUFZSNDUvV2Fkd2djaVorWUd1Z3JZMWlKSlZ4NEtzaHgxUnRoRHB3VzVVM2dzSHZsNmNRWW50RktUcUZjbmlramRDeGRaMG9iQTg1Y1lURDNSVlAwOVVERkMwMXo3N0NwOXE3OGVRbzI3V1pzYmxtcEpWaG1XYytpQTdsYjQ3YlE3dlRadDNxK2pBYnpLVVNVY3R2eERhc0ErU2s5V0ZJZ0kvZUV0ejd0NFY0aGloeWFhb2J4TGRGeVUxUGhpTENncmFOeWRyL3hFZTJxT2hOclZ1TUIzTlRTVjMxVG1lUUVFTGYwcSt5MG9Yd2U2SERnREtQTzdhem1KVHovUm8rMUVpM1VaSGdOT203SDVhK3VQVUhkaFRzQ2hUcXVydzBMNHdrYk1JNERjeWVLbmFYY3NtT3RUV3EzRGNPeEhDUmlUa2tHU0lwb3lxVE9GMlE9PQ==' \
  --header 'Content-Type: application/json' \
  --data '{
	"username": "bucky",
	"password": "E1y7EYbgFDM0ZcGSIpxe4VOAxs5QGlyI1B6YfxPqa95zIt09eiSNBivBeVrV7TRfCeA9qTbIJtAilo+453VodxeQmyBioQh1XFcrJyX9bsWQJbCOpPpNxTzhnxMv0gGLeCu7huxr09eNqZfTzx3i2xCq+FTlic/uu8IeS14xo9iA1JM+Xx1pMVobIrnqmvCvnmRgJagLIk06YdYPlgtsIoy2BwhYyS5wgkJn8BsbNoQbYxlTAnAyyNZ+IBgB9RgyHQ5UiNSv8mIk7hpKsXAbfCUh6CGegRLmj7joTNDKYDWtwWWf99y74/qi/QMq6Yfwgd3QjKt08iGY4YaKNtA3mA=="
}'

Success response
{"code":"S200","message":"Successful","data":{"access_token":"c83c8f45-11e2-479a-8210-ab5f0d1f7f22","token_type":"bearer","refresh_token":"16d0a17f-9f5a-4dbe-bd80-03a58e2a5b64","expires_in":3599,"scope":"read write","refresh_token_expires_in":3799,"refresh_token_expires_date":"2021-10-31 16:13:44","expires_date":"2021-10-31 16:10:24","issued_at":"2021-10-31 15:10:24"}}
```

### View customer info
Use access token as authorization
```sh
curl --request GET \
  --url http://localhost:8080/customer/me \
  --header 'Authorization: Bearer c83c8f45-11e2-479a-8210-ab5f0d1f7f22'
```

### View customer profile picture
Use access token as authorization
```sh
curl --request GET \
  --url http://localhost:8080/customer/photo \
  --header 'Authorization: Bearer b4ff1817-a97a-400d-af02-a835f254f666'
```

### Browse all available products
Use access token as authorization
```sh
curl --request GET \
  --url http://localhost:8080/product/list \
  --header 'Authorization: Bearer 76b65958-2ad3-46fc-ba9d-14f081e46574'
```

### Purchase the product
Use access token as authorization
```sh
curl --request GET \
  --url http://localhost:8080/product/purchase/{productId} \
  --header 'Authorization: Bearer 39bdb868-737c-4c13-b852-f4c163b48cb9'
```

### View purchased policies
Use access token as authorization
```sh
curl --request GET \
  --url http://localhost:8080/product/policies \
  --header 'Authorization: Bearer 39bdb868-737c-4c13-b852-f4c163b48cb9'
```