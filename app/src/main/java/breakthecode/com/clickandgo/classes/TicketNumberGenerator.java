package breakthecode.com.clickandgo.classes;

import java.security.SecureRandom;

public class TicketNumberGenerator {

    public static String generateTicket() {
        SecureRandom random = new SecureRandom();
        String[] tableOfAvailableLetters = {"0","1","2","3","4","5","6","7","8","9","Q","q","W","w","E","e","R","r","T","t","Y","y","U","u","I","i","O","o","P","p","A","a",
                "S","s","D","d","F","f","G","g","H","h","J","j","K","k","L","l","Z","z","X","x","C","c","V","v","B","b","N","n","M","m"};
        String generatedCode = "";
        StringBuilder sb = new StringBuilder(generatedCode);
        for (int i = 0; i < 14; i++) {
            sb.append(tableOfAvailableLetters[random.nextInt(tableOfAvailableLetters.length)]);
        }
        return sb.toString();
    }
}
