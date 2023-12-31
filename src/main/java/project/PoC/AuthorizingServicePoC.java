package project.PoC;


import java.awt.Color;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthorizingServicePoC {
    private static final double MAXIMUM_THRESHOLD = 0.05;
    private final AuthorizingRepositoryPoC authorizingRepositoryPoC = new AuthorizingRepositoryPoC();
    public String authorize(String colorString) {
        Color color = parse(colorString);

        Optional<User> user = findUserByColor(color);

        if (user.isPresent()) {
//            add info to db about user entering the room (in the future?)
//            return smth like "welcome x";
        }
        return "unauthorized";
    }

    private Optional<User> findUserByColor(Color color) {
        List<User> userList = authorizingRepositoryPoC.findAll();

        return userList.stream().filter(user -> isColorSimilar(user.getColor, color)).findFirst();
    }

    private boolean isColorSimilar(Color userColor, Color color) {
        double distance = compareColors(userColor, color);

        return distance <= MAXIMUM_THRESHOLD;
    }

    private double compareColors(Color userColor, Color color) {
//        convert to HSB
        float[] hsb1 = Color.RGBtoHSB(userColor.getRed(), userColor.getGreen(), userColor.getBlue(), null);
        float[] hsb2 = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);

        // Calculate the Euclidean distance between the color points in the HSB color space
        return Math.sqrt(Math.pow(hsb1[0] - hsb2[0], 2) + Math.pow(hsb1[1] - hsb2[1], 2) + Math.pow(hsb1[2] - hsb2[2], 2));
    }

    private static Color parse(String input)
    {
        Pattern c = Pattern.compile("rgb *\\( *([0-9]+), *([0-9]+), *([0-9]+) *\\)");
        Matcher m = c.matcher(input);

        if (m.matches())
        {
            return new Color(Integer.parseInt(m.group(1)),  // r
                    Integer.parseInt(m.group(2)),  // g
                    Integer.parseInt(m.group(3))); // b
        }

        return null;
    }
}
