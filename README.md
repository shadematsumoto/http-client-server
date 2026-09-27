# HTTP Client and Server

A simple Java client and server. The server sends `server_file.txt` to the client, and the client saves it as `client_file.txt`.

Requires a Java JDK.

## Run

Compile:

```
javac MyServer.java MyClient.java
```

Start the server:

```
java MyServer 25000
```

In a second terminal, run the client:

```
java MyClient 25000
```