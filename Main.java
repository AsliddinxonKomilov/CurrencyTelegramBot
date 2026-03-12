package currencyBot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import currencyBot.model.CurrencyResponse;
import currencyBot.service.CurrencyService;
import currencyBot.service.FileService;
public class Main {
    public static void main(String[] args) {
        TelegramBot telegramBot = new TelegramBot("BOT_TOKEN_HERE");
        final String[] currentRate = {"0"};
        telegramBot.setUpdatesListener(updates -> {
            for (Update update : updates) {
                if (update.message() != null && update.message().text() != null) {
                    long chatId = update.message().chat().id();
                    String userText = update.message().text();

                    if (userText.equals("/start")) {
                        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(
                                "\uD83C\uDDFA\uD83C\uDDF8 USD (Dollar)",
                                "\uD83C\uDDF7\uD83C\uDDFA RUB (Rubl)",
                                "\uD83C\uDDEA\uD83C\uDDFA EURO(Yevro)"
                        );
                        replyKeyboardMarkup.resizeKeyboard(true);
                        replyKeyboardMarkup.oneTimeKeyboard(false);

                        telegramBot.execute(new SendMessage(
                                chatId, """
                                👋 Assalomu Aleykum! Siz uchbu valyuta kursi bot orqali har kungi valyuta kursi haqida
                                ma'lumot olishingiz mumkin bo'ladi!
                                """
                        ).replyMarkup(replyKeyboardMarkup));
                    }
                    try {
                        CurrencyService currencyService = new CurrencyService();
                        String currencyCode = "";
                        if (userText.contains("USD"))
                            currencyCode = "USD";
                        else if (userText.contains("RUB"))
                            currencyCode = "RUB";
                        else if (userText.contains("EURO"))
                            currencyCode = "EUR";

                        if (!currencyCode.isEmpty()) {
                            CurrencyResponse currencyResponse = currencyService.getParsedCurrency(currencyCode);
                            if (currencyResponse != null) {
                                String message = currencyResponse.name + " kursi : " + currencyResponse.rate + " so'm " +
                                        "hisoblamoqchi bo'lgan qiymatni kiriting : ";
                                ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(
                                        "Hisoblash\uD83E\uDDEE", "Bekor qilish❌"
                                );
                                if (currencyResponse != null){
                                    currentRate[0] = String.valueOf(currencyResponse.rate);
                                }
                                replyKeyboardMarkup.resizeKeyboard(true);
                                replyKeyboardMarkup.oneTimeKeyboard(false);
                                telegramBot.execute(new SendMessage(chatId, message
                                ).replyMarkup(replyKeyboardMarkup));
                            }
                        }

                        if (userText.matches("\\d+")){
                            FileService fileService = new FileService();
                            String result = currencyService.calculateAmount(currentRate[0],
                                    userText);
                            telegramBot.execute(new SendMessage(chatId,result));
                            fileService.saveSearch(currencyCode,result);
                        }
                        else if (userText.equals("Bekor qilish❌")){
                            SendMessage sendMessage = new SendMessage(chatId,"Amal bekor qilindi! Kerakli valyutani" +
                                    " kiriting ");
                            ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(
                                    "\uD83C\uDDFA\uD83C\uDDF8 USD (Dollar)",
                                    "\uD83C\uDDF7\uD83C\uDDFA RUB (Rubl)",
                                    "\uD83C\uDDEA\uD83C\uDDFA EURO(Yevro)"
                            );
                            replyKeyboardMarkup.resizeKeyboard(true);
                            replyKeyboardMarkup.oneTimeKeyboard(false);
                            telegramBot.execute(sendMessage.replyMarkup(replyKeyboardMarkup));
                            currentRate[0] = "0";
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }
}