FROM strimzi/kafka:0.17.0-rc1-kafka-2.4.0
USER root:root
RUN mkdir -p /opt/kafka/plugins/TelegramChatBotSink
COPY ./target/telegram-chat-bot-sink-1.0.jar /opt/kafka/plugins/TelegramChatBotSink/
USER 1001
