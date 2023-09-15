package com.killerf1.backend;

import java.security.SecureRandom;

public class GameIdGenerator {
    private static final String ALPHANUMERIC_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int ID_LENGTH = 5;

    /**
     * Generate a random 5 alphanumerical character game id
     * 
     * @return The randomly generated game id
     */
    public static String generateGameId() {
        SecureRandom random = new SecureRandom();
        StringBuilder gameId = new StringBuilder();

        for (int i = 0; i < ID_LENGTH; i++) {
            int randomIndex = random.nextInt(ALPHANUMERIC_CHARS.length());
            char randomChar = ALPHANUMERIC_CHARS.charAt(randomIndex);
            gameId.append(randomChar);
        }

        return gameId.toString();
    }
}
