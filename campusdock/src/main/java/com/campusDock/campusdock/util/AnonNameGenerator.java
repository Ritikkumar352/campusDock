package com.campusDock.campusdock.util;

import java.util.*;

public class AnonNameGenerator {

    private static final List<String> techTerms = Arrays.asList(
            "Stack", "Overflow", "Kernel", "Daemon", "Glitch", "Byte", "Cache", "Cipher",
            "Quantum", "Vector", "Lambda", "Root", "Thread", "Loop", "Hack", "Node",
            "Ping", "Token", "Array", "Recursion","Delay"
    );

    private static final List<String> personas = Arrays.asList(
            "Phantom", "Vortex", "Nomad", "Ronin","Robin", "Sentinel", "Warlock",
            "Catalyst", "Rogue", "Specter", "Gladiator", "Striker", "Shade",
            "Invoker", "Outlaw", "Warden", "Oracle", "Drifter", "Rebel"
    );

    private static final List<String> httpCodes = Arrays.asList(
            "200", "201", "202", "204", "400", "401", "403", "404", "409",
            "418", "429", "500", "502", "503", "504"
    );

    private static final Random random = new Random();

    /**
     * Generates a unique anonymous name in the format TechPersona-HTTPCode
     * Ensures the generated name is not in the takenNames set.
     *
     * @param takenNames Set of existing anon usernames (from DB or cache)
     * @return A unique anonymous name
     */
    public static String generateUniqueAnonName(Set<String> takenNames) {
        String anonName;
        int maxTries = 50; // avoid infinite loop

        do {
            String tech = techTerms.get(random.nextInt(techTerms.size()));
            String persona = personas.get(random.nextInt(personas.size()));
            String code = httpCodes.get(random.nextInt(httpCodes.size()));
            anonName = tech + persona + "-" + code;
            maxTries--;
        } while (takenNames.contains(anonName) && maxTries > 0);

        if (takenNames.contains(anonName)) {
            // fallback: add a UUID segment
            anonName += "-" + UUID.randomUUID().toString().substring(0, 4);
        }

        return anonName;

    }
}
