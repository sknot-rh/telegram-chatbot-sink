package TelegramChatBotSink;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
//import org.apache.camel.component.telegram.model.IncomingMessage;
import org.apache.kafka.connect.sink.SinkRecord;
import org.apache.kafka.connect.sink.SinkTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;

public class TaskClass extends SinkTask {
    private static final Logger LOG = LoggerFactory.getLogger(TaskClass.class);
    private final ObjectMapper mapper = new ObjectMapper();

    private String telegramBotId;
    private String telegramChatId;
    private String brainshopBotId;
    private String brainshopBotUid;
    private String brainshopBotKey;
    private String brainshopApiKeyHeader;

    @Override
    public String version() {
        return new TelegramChatBotSink().version();
    }

    @Override
    public void start(Map<String, String> props) {
        telegramChatId = props.get(TelegramChatBotSink.TELEGRAM_CHAT_ID);
        telegramBotId = props.get(TelegramChatBotSink.TELEGRAM_BOT_ID);
        brainshopBotId = props.get(TelegramChatBotSink.BRAINSHOP_AI_BOT_ID);
        brainshopBotUid = props.get(TelegramChatBotSink.BRAINSHOP_AI_UID);
        brainshopBotKey = props.get(TelegramChatBotSink.BRAINSHOP_AI_KEY);
        brainshopApiKeyHeader = props.get(TelegramChatBotSink.BRAINSHOP_API_KEY_HEADER);
    }

    @Override
    public void put(Collection<SinkRecord> sinkRecords) {
        for (SinkRecord record : sinkRecords) {
            String msg = record.value().toString();
            //IncomingMessage im = (IncomingMessage) record.value();
            //String message = im.getText();
            // there is an issue with camel dep and i am lazy to fix it :(
            String message = parseTextFromIm(msg);
            boolean isbot = msg.contains("is_bot='true'");

            try {
                if (!isbot) {
                    String botAnswer = sendRequestToChatBot(message);
                    LOG.debug("bot answers {}", botAnswer);
                    sendAnswerToTelegram(botAnswer);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void stop() {
        // Nothing to do
    }

    private String sendRequestToChatBot(String msg) throws Exception {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://acobot-brainshop-ai-v1.p.rapidapi.com/get?bid=" + brainshopBotId + "&key=" + brainshopBotKey + "&uid=" + brainshopBotUid + "&msg=" + msg)
                .get()
                .addHeader("x-rapidapi-host", "acobot-brainshop-ai-v1.p.rapidapi.com")
                .addHeader("x-rapidapi-key", brainshopApiKeyHeader)
                .build();

        Response response = client.newCall(request).execute();
        String botAnswer = mapper.readTree(response.body().string()).get("cnt").toString();
        return botAnswer;
    }

    private void sendAnswerToTelegram(String msg) throws Exception {
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("chat_id", telegramChatId)
                .add("text", msg.substring(1, msg.length() - 1))
                .build();

        Request request = new Request.Builder()
                .url("https://api.telegram.org/bot" + telegramBotId + "/sendMessage")
                .post(formBody)
                .build();

        client.newCall(request).execute();
    }

    private String parseTextFromIm(String inMessage) {
        // this would not be needed if I was not lazy
        return inMessage.substring(inMessage.indexOf(", text='") + ", text='".length(), inMessage.indexOf("', chat="));
    }

}