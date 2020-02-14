A simple REST app that can create/list accounts and make transfers between them.

Endpoints:

`/transfers`

`/accounts`

`/accounts/{id}`

Detailed API doc can be found in _API.yaml_

Run app: `java -jar MoneyTransfer-1.0.jar`

Rebuild jar:
`gradlew jar`

For the sake of the test task the API tests are in the same directory as the main source code.

To run API tests make sure the app is up and running and execute following in _apitests_ directory:

`gradlew test`
