# sample-jwt

Demo for JSON Web Token (JWT) with Spring Boot

# Uproszczony schemat

* Authentication - who am I? np. Jan Kowalski
* Authorization - what can I do? np. uprawnienia do zapisu

```
System A  -> request -> System B

Scenariusz: uzytkownik uwierzytelnia sie za pomoca loginu i hasla

System A -> jestem Jan Kowalski (user/password)
    System B zwroci accessToken (expiration 10minutes) & refreshToken (expiration 30minutes)
    System A zapisze sobie accessToken i refreshToken
System A -> chce wywolac GET /users, accessToken
    System B zwaliduje token
    System B sprawdzi czy user (przedstawiony przez Token) ma dostep do zasobu GET /users
    
Scenariusz: gdy token jest expired

System A -> chce wywolac GET /users, accessToken
    System B zwaliduje token
    System B stwierdza ze accessToken wygasl
System A dostaje informacje, rozumie ja jako 'accessToken wygasl' i wie gdzie sie zglosic po nowy token, ktory uzyje refreshTokena (a nie login i haslo!)
System A -> wysyla refreshToken do GET /auth 
    System B zwaliduje refresh Token
    System B zwroci accessToken (expiration 10minutes) & refreshToken (expiration 30minutes)
    
```