# sample-jwt

Demo for JSON Web Token (JWT) with Spring Boot

# Uproszczony schemat

* Authentification - who am I? np. Jan Kowalski
* Autorization - what can I do? np. uprawnienia do zapisu

```
System A  -> request -> System B

System A -> jestem Jan Kowalski (user/password)
    <- System B zwroci accessToken
System A -> chce wywolac GET /users, accessToken
    System B zwaliduje token
    System B sprawdzi czy user (przedstawiony przez Token) ma dostep do zasobu GET /users
```