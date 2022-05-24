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

# Summary

JWT contains 3 parts:

* Header.
    * Example: `{ "alg": "HS256", "typ": "JWT" }`
    * Base64 encoded: `eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9`
* Payload:
    * Example: `{ "sub": "1234567890", "name": "John Doe", "iat": 1516239022 }`
    * Base64 encoded: `eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ`
    * contains [claims](https://datatracker.ietf.org/doc/html/rfc7519#section-4):
        * Registered claims - [registry](https://www.iana.org/assignments/jwt/jwt.xhtml)
        * Public claims
        * Private claims
* Signature. Some 256-bit-secret
    * Example base64 encoded: `EpM5XBzTJZ4J8AfoJEcJrjth8pfH28LWdjLo90sYb9g`
    * Secret: `my-secret`

All 3 parts are combined and separated with '.'. Above examples would
give: `eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.EpM5XBzTJZ4J8AfoJEcJrjth8pfH28LWdjLo90sYb9g`

*
Example: [link](https://jwt.io/#debugger-io?token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.EpM5XBzTJZ4J8AfoJEcJrjth8pfH28LWdjLo90sYb9g)
* CyberChef example of decode/signe and
  verification: [link](https://gchq.github.io/CyberChef/#recipe=JWT_Decode()JWT_Sign('my-secret','HS256')JWT_Verify('my-secret')From_Base64('A-Za-z0-9%2B/%3D',true/disabled)&input=ZXlKaGJHY2lPaUpJVXpJMU5pSXNJblI1Y0NJNklrcFhWQ0o5LmV5SnpkV0lpT2lJeE1qTTBOVFkzT0Rrd0lpd2libUZ0WlNJNklrcHZhRzRnUkc5bElpd2lhV0YwSWpveE5URTJNak01TURJeWZRLkVwTTVYQnpUSlo0SjhBZm9KRWNKcmp0aDhwZkgyOExXZGpMbzkwc1liOWc)

# Libraries:

## [JJWT](https://github.com/jwtk/jjwt).

It uses below dependencies

```xml

<dependencies>
	<dependency>
		<!-- https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt-api -->
		<groupId>io.jsonwebtoken</groupId>
		<artifactId>jjwt-api</artifactId>
		<version>${jjwt.version}</version>
	</dependency>
	<dependency>
		<!-- https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt-impl -->
		<groupId>io.jsonwebtoken</groupId>
		<artifactId>jjwt-impl</artifactId>
		<version>${jjwt.version}</version>
		<scope>runtime</scope>
	</dependency>
	<dependency>
		<!-- https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt-jackson -->
		<groupId>io.jsonwebtoken</groupId>
		<artifactId>jjwt-jackson</artifactId>
		<version>${jjwt.version}</version>
		<scope>runtime</scope>
	</dependency>
</dependencies>
```

## [Auth0](https://auth0.com/docs/quickstart/backend/java)

* [Java Spring Boot](https://auth0.com/docs/quickstart/webapp/java-spring-boot/01-login)
* It uses below dependencies

```xml

<dependency>
	<!-- https://mvnrepository.com/artifact/com.auth0/java-jwt -->
	<groupId>com.auth0</groupId>
	<artifactId>java-jwt</artifactId>
	<version>${java-jwt.version}</version>
</dependency>
```

# Articles

* [JWT](https://jwt.io/)
    * [JWT Introduction](https://jwt.io/introduction)
    * [JWT libraries](https://jwt.io/libraries)
* [JWT Specification](https://tools.ietf.org/html/rfc7519)
* [JSON Web Token - Wikipedia](https://en.wikipedia.org/wiki/JSON_Web_Token)
* [JSON Web Tokens - co to jest?](https://blog.i-systems.pl/json-web-tokens-jwt/)
* Online encoders/decoders
    * [JWT](https://jwt.io/)
    * [Encode or Decode WJTs](https://token.dev/) - allow to use 'none' algorithm
* Algorithms:
    * [JSON Web Algorithms (JWA)](https://datatracker.ietf.org/doc/html/rfc7518#section-3) - list of algorithms, which
      may be used with JWT
    * [RS256 vs HS256: What's the difference?](https://stackoverflow.com/questions/39239051/rs256-vs-hs256-whats-the-difference)
    * [Elliptic-curve cryptography](https://en.wikipedia.org/wiki/Elliptic-curve_cryptography)
    * [JSON Web Token (JWT) with RSA encryption](https://connect2id.com/products/nimbus-jose-jwt/examples/jwt-with-rsa-encryption)
    * [JSON Web Token (JWT) Signing Algorithms Overview](https://auth0.com/blog/json-web-token-signing-algorithms-overview/)
    * [Mask generation function](https://en.wikipedia.org/wiki/Mask_generation_function)
* Trainings:
    * [Spring Boot and Spring Security with JWT including Access and Refresh Tokens](https://www.youtube.com/watch?v=VVn9OG9nfH0)
* Examples for code:
    * [Spring Security with JWT for REST API](https://www.toptal.com/spring/spring-security-tutorial)
    * [Spring Boot Security + JWT Hello World Example](https://www.javainuse.com/spring/boot-jwt)
* Tools
    * [Epoch Unix Timestmap converter](https://www.unixtimestamp.com/) - to check the date under the `exp` claim
