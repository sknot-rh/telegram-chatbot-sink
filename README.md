# telegram-chatbot-sink

This is a simple Kafka sink Connector which answers to user messages using a [ChatBot](https://rapidapi.com/Acobot/api/brainshop-ai).

The functionality is demonstrated using another source connector - Camel Telegram Connector.
Camel connector reads a messages from the chat and this connector replies with bot message.


You have co customize `00` secret files. File 00-brainshop-creds.yaml has to contain 
The properties file for Telegram should look like this:

```properties
chat_id:135
token=123:xxx
```

The properties file for BrainShop should look like this:

```properties
bot.id=13
key=secretKey42
uid=someuid
API.header=headerhash5465456454654
```

The deployment uses two connect clusters. One for the Camel connector, second cluster for this ChatBot connector. It is possible to build a connector image containg both plugins and rgister them separately. I just wanted to try cooperation of two connect clusters.


