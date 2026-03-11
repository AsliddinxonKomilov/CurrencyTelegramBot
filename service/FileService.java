package currencyBot.service;

import lombok.Data;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

@Data
public class FileService {
    public void  saveSearch(String currency,String value) {
        synchronized (this) {
            try (FileWriter fileWriter = new FileWriter("currencyHistory.txt",true)) {
                fileWriter.write("Pul birligi : " + currency  +
                        "| Hisoblandi : " + value +
                         " | Vaqt : " + LocalDate.now() + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public String readHistory() throws IOException {
        String readString = Files.readString(Path.of("currencyHistory.txt"));
        return readString;
    }
}
