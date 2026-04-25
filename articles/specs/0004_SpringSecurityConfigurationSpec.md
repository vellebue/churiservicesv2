# Spring Security Configuration

Configure Spring Security for articles microservice according to following premises:

    - Configure Security based on JWT with stateless sessions.
    - There is an authenticator server running at localhost port 9080 that is a Keycloak server. Use the following configuration tips.Security
        - Keycloak realm name is churiservicesv2
        - Client id is:  468b9948-2753-4caa-b854-14cb1cf45271
        - Client secret is: TCV0RTUaSyRCPaE8jGijV9vDeTplQBeb
        - Use this user for testing:
            - Username: angel
            - Password: angel8

Complete this file adding these indications:

    - URL and http headers to obtain web token from authentication server.

    - URL and headers to test /api/article-units service

## Obtaining a JWT access token from Keycloak

Use the OpenID Connect token endpoint with the `password` grant type (Resource Owner
Password Credentials flow), which is convenient for local testing with the test user
declared above.

**Endpoint**

```
POST http://localhost:9080/realms/churiservicesv2/protocol/openid-connect/token
```

**Headers**

```
Content-Type: application/x-www-form-urlencoded
Accept: application/json
```

**Body (form-urlencoded)**

```
grant_type=password
client_id=468b9948-2753-4caa-b854-14cb1cf45271
client_secret=TCV0RTUaSyRCPaE8jGijV9vDeTplQBeb
username=angel
password=angel8
```

**curl example**

```bash
curl -X POST 'http://localhost:9080/realms/churiservicesv2/protocol/openid-connect/token' \
  -H 'Content-Type: application/x-www-form-urlencoded' \
  -H 'Accept: application/json' \
  -d 'grant_type=password' \
  -d 'client_id=468b9948-2753-4caa-b854-14cb1cf45271' \
  -d 'client_secret=TCV0RTUaSyRCPaE8jGijV9vDeTplQBeb' \
  -d 'username=angel' \
  -d 'password=angel8'
```

The response contains an `access_token` field. That value is the JWT to be sent as the
`Authorization: Bearer <token>` header against the articles microservice.

## Testing the /api/article-units service

The articles microservice listens on port `8080` and the article units endpoint is
exposed under `/api/article-units`. All `/api/**` endpoints require a valid JWT issued
by the Keycloak realm `churiservicesv2`.

**Endpoint**

```
GET http://localhost:8080/api/article-units
```

**Headers**

```
Authorization: Bearer <access_token>
Accept: application/json
```

**curl example**

```bash
ACCESS_TOKEN=$(curl -s -X POST 'http://localhost:9080/realms/churiservicesv2/protocol/openid-connect/token' \
  -H 'Content-Type: application/x-www-form-urlencoded' \
  -d 'grant_type=password' \
  -d 'client_id=468b9948-2753-4caa-b854-14cb1cf45271' \
  -d 'client_secret=TCV0RTUaSyRCPaE8jGijV9vDeTplQBeb' \
  -d 'username=angel' \
  -d 'password=angel8' | jq -r .access_token)

curl -X GET 'http://localhost:8080/api/article-units' \
  -H "Authorization: Bearer $ACCESS_TOKEN" \
  -H 'Accept: application/json'
```

Expected behaviours:

- Without the `Authorization` header: HTTP `401 Unauthorized`.
- With a valid token: HTTP `200 OK` and the JSON list of article units.
- With an expired or tampered token: HTTP `401 Unauthorized`.

To fetch a single unit by symbol use:

```
GET http://localhost:8080/api/article-units/article-unit/{symbol}
Authorization: Bearer <access_token>
```
