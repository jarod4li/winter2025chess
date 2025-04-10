# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```

Server Diagram URL:
https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvqbCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HmDYnG4sFOR1E5SgURimSgAApItFYpRIgBHHxqMAASkw4JUJ0KsnkShU6nK9hQYAAqh04Tc7jiiYplGpVASjDpSgAxJCcGA0yhMmA6ML04CjUxMkmsk5g4oQmBoHwIBC4+X405SllkkBQuQoAXwm5MxnaaXqdnGUoKDgcfkdJlqkQawlm7WqUq6lD6hQ+MCpOHAP2pE2YLWktmnK02u2+-2OvGGXKnIEXMoRaHIqCRVTKrCpkGywpHS4wa4dO6TcqrJ5B-31CAAa3QVam+ydx2ThWy5nKACYnE5uuWhuKxjBq48pnXUg3m2hW6sDugOKYvL5-AFoOwKTAADIQaJJAJpDJZZDmdkl9PVOpNVoGdQJNDDsWjWYvN4cA7X9kF0s9FMb73H0XyfksKyTAc-6gsW6ooOUCCHrycIHkeaIYrEOKJuy4asuSlKGnSFZjqaxLupanI8nyhpCiKMDAWGboRkWRTOgh9qCtoHaiLhzH4TAcIAJIAGacVAQrge8MAALwyTAUkcFi3Rej6waBsGoZ4RaUacnAeqZDAcYhtxOFdmcwKlmhvI5nmgLnIW5nXlcQEkaMkG1sGc4thMUEdpQrE9hg-aDsOAxueOk5fDO3kLhObZ9MunBrt4fiBF4KDoPuh6+MwJ7pJkmBBcwcrUKWFTSAAonulX1JVzQtI+qjPt0MVNug-mwRZablDO0BIAAXvEiTlAAPG1875PZlldYmiE5X6qELWAGGYth8F8eREalBwKDcJkxkafW7VoGRzIsbppTSHtlKGMZdFhBNHXaZGpVUAq92mRt5kwaUaG5bZCD5g5AVOcUAE-uDoKnMVIVDr0SWrp4qWblCdp7jCMAAOJjmy+VnkVF55HBZU3ljtUNfYY6tV5J2dX+IPpn1UCDcNaBjU9aBTTBV7wYhMI46MqioQLuOrVhPEujI-FkhSYCHZzZ3mpGhRWtRsaadowqPbT85MVtMpg+x5SffIktJq6BtksgsSC2ocJKxRl3q0ZKrY7j+vnYbb0KpVJRhEJaCxOw+IQGJdsJt9KaM6UdsY7E01pqxzlllMVNC+MlT9OnKBCdImcAIx9gAzAALE8p6ZIala+U8OgIKAjbV2OHlp2OAByY6+XsMCNJDpOBUTYBw8ObcZ+UFTZ2OeeFyX5dTJXBoRa3fT143zfubXY8oJ3ozd73iMpRugTYD4UDYNw8AGYYdspAV545CVJMlBPtQNJT1PBLr6DDjnu8oP3TshRfowGZqzAgI0YDjW-lzUefQ-4t0+DAaCjNebGxgKpTIds4T6W9Fgsc4tsTm02l7WWlIFYwMdhdVWVFeQa3jFreinNPbK2TnzIymszZmUtqQj0mCUDYJzlpGWKsOTWltO7UYkd2KsRAbg-UdtAbAxmmw0mLl4HT3zuUIuZdkH03MrDGAA54aAQ0aMGe2i556JXEfNKARLB7SQskGAAApCAvJJGGACGvEAjZCaPzQS-SoVQqT3haDnGmx15zDgvsABxUA4AQCQlAWYOc86ANBsAmOoDgz9SGhA9mUDOb5DgbE+JiTkkrAAOosCEnVFoAAhPcCg4AAGkvhpK0TAHRpc9E8x9iocoAArdxaBsFuN5IolA6I1rEM1CI0ocsKFRPQFQw2NDuR0I4Qw+Q2sckrLQCwp2AyOKm2AHMnhysCJgEEZotZOkNkuwjowsInSjkXROeUZ5XDvqXPdKUYSYkymUAqdAWS8kal1MqspJU2AtCZEXhvFAxFRyjHuaIq0VI4X6hgIiiKuKMipAYhFC50srYejhJVBABgYV+HhSgHxTcIootuKRd56yxFYvpcKBuvikUEv9MS1FYhuFkt4aUOlCixwO3ZQ8zl2LMhshzjAWILMHBzOjjNcoEyxkELUHZfpb0Ib6JhkPEeCMzDJWRsfAIXg4nwG4HgUB2AL6EDZnfAmxVAnlSqjVOqDVjD6KyVqnoKCVGfJAI6+E60ZHzPJZ6KNMqXqUVKIgb0sAmR6AMBq4NPUMFRqUYnRyRqyihpNd2M1xjhyHyAA
