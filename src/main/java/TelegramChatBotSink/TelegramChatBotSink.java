package TelegramChatBotSink;

import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.sink.SinkConnector;
import org.apache.kafka.connect.connector.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TelegramChatBotSink extends SinkConnector {
    private static final Logger LOG = LoggerFactory.getLogger(TaskClass.class);
    public static final String TELEGRAM_CHAT_ID = "telegram.chat.id";
    public static final String TELEGRAM_BOT_ID = "telegram.bot.id";
    public static final String BRAINSHOP_AI_BOT_ID = "brainShop.AI.bot.id";
    public static final String BRAINSHOP_AI_KEY = "brainShop.AI.key";
    public static final String BRAINSHOP_AI_UID = "brainShop.AI.uid";
    public static final String BRAINSHOP_API_KEY_HEADER = "brainShop.AI.API.header";
    private static final ConfigDef CONFIG_DEF = new ConfigDef()
            .define(TELEGRAM_CHAT_ID, ConfigDef.Type.STRING, "0", ConfigDef.Importance.HIGH, "The id of telegram chat where Chat Bot will send a messages.")
            .define(TELEGRAM_BOT_ID, ConfigDef.Type.STRING, "0", ConfigDef.Importance.HIGH, "The id of telegram bot which will answer to telegram chat.")
            .define(BRAINSHOP_AI_BOT_ID, ConfigDef.Type.STRING, "0", ConfigDef.Importance.HIGH, "The id of BrainShop chat bot which will respond AI message.")
            .define(BRAINSHOP_AI_KEY, ConfigDef.Type.STRING, "0", ConfigDef.Importance.HIGH, "The key of BrainShop chat bot which will respond AI message.")
            .define(BRAINSHOP_AI_UID, ConfigDef.Type.STRING, "0", ConfigDef.Importance.HIGH, "The uid of BrainShop chat bot which will respond AI message.")
            .define(BRAINSHOP_API_KEY_HEADER, ConfigDef.Type.STRING, "0", ConfigDef.Importance.HIGH, "The key of BrainShop API.");

    private String telegramChatId;
    private String telegramBotId;
    private String brainshopAiBotId;
    private String brainshopAiBotUid;
    private String brainshopAiBotKey;
    private String brainshopAiAPIkey;

    @Override
    public String version() {
        return "1.0.0";
    }

    @Override
    public void start(Map<String, String> props) {
        AbstractConfig parsedConfig = new AbstractConfig(CONFIG_DEF, props);
        telegramChatId = parsedConfig.getString(TELEGRAM_CHAT_ID);
        telegramBotId = parsedConfig.getString(TELEGRAM_BOT_ID);
        brainshopAiBotId = parsedConfig.getString(BRAINSHOP_AI_BOT_ID);
        brainshopAiBotUid = parsedConfig.getString(BRAINSHOP_AI_UID);
        brainshopAiBotKey = parsedConfig.getString(BRAINSHOP_AI_KEY);
        brainshopAiAPIkey = parsedConfig.getString(BRAINSHOP_API_KEY_HEADER);
    }

    @Override
    public Class<? extends Task> taskClass() {
        return TaskClass.class;
    }

    @Override
    public List<Map<String, String>> taskConfigs(int maxTasks) {
        ArrayList<Map<String, String>> configs = new ArrayList<>();

        Map<String, String> config = new HashMap<>(6);

        if (telegramBotId != null) {
            config.put(TELEGRAM_BOT_ID, telegramBotId);
        }

        if (telegramChatId != null) {
            config.put(TELEGRAM_CHAT_ID, telegramChatId);
        }

        if (brainshopAiBotKey != null) {
            config.put(BRAINSHOP_AI_KEY, brainshopAiBotKey);
        }

        if (brainshopAiBotUid != null) {
            config.put(BRAINSHOP_AI_UID, brainshopAiBotUid);
        }

        if (brainshopAiBotId != null) {
            config.put(BRAINSHOP_AI_BOT_ID, brainshopAiBotId);
        }

        if (brainshopAiAPIkey != null) {
            config.put(BRAINSHOP_API_KEY_HEADER, brainshopAiAPIkey);
        }

        for (int i = 0; i < maxTasks; i++) {
            configs.add(config);
        }
        return configs;
    }

    @Override
    public void stop() {
        // Nothing to do
    }

    @Override
    public ConfigDef config() {
        return CONFIG_DEF;
    }
}
