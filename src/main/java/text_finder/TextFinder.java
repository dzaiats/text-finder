package text_finder;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public class TextFinder {

    public static final double DERIVATION = 0.3;

    public static boolean textIsFound(String pattern, String text) {
        pattern = pattern.toLowerCase();
        text = text.toLowerCase();

        if (text.contains(pattern)) {
            return true;
        } else if (text.replace(" ", "").contains(pattern.replace(" ", ""))) {
            return true;
        } else {
            String[] patternArr = pattern.split("\\W|\\s");
            String[] textArr = text.split("\\W|\\s");

            int matchTime = 0;
            int positionPrev = 0;
            for (String sp : patternArr) {
                for (String st : textArr) {
                    if (st.equals(sp)) {
                        int positionNext = Arrays.asList(textArr).indexOf(st);
                        if (positionNext > positionPrev) {
                            positionPrev = positionNext;
                            matchTime++;
                            break;
                        }
                    } else if (st.contains(sp)) {
                        matchTime++;
                        break;
                    }
                }
            }

            boolean found = false;
            if (matchTime != patternArr.length) {
                String textJoined = String.join("", textArr);
                String patternJoined = String.join("", patternArr);
                String[] patternJoinedArr = patternJoined.split("");

                int position = 0;
                Map<Integer, String> positions = new TreeMap<Integer, String>();

                for (String s : patternJoinedArr) {
                    while (position >= 0) {
                        position = textJoined.indexOf(s, position + 1);
                        if (position > -1) {
                            positions.put(position, s);
                        }
                    }
                    position = 0;
                }

                int count = 0;
                int countInLine = 1;

                for (Map.Entry entry : positions.entrySet()) {
                    Integer i = (Integer) entry.getKey();
                    if (positions.get(i + 1) != null && count < positions.size()) {
                        countInLine++;
                    } else {
                        countInLine = 1;
                    }
                    if (countInLine >= patternJoined.length() * (1 - DERIVATION)) {
                        found = true;
                        break;
                    }
                    count++;
                }
            }

            return matchTime == patternArr.length || found;
        }
    }
}
