package project.PoC;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthorizingServicePoC {
    private static final double MAXIMUM_THRESHOLD = 0.05;//final factor result will be calculated after the assembly of the hardware

    private final UserRepositoryPoC userRepositoryPoC;
    public String authorize(String colorString) {
        Color color = parse(colorString);

        Optional<User> user = findUserByColor(color);

        if (user.isPresent()) {
//            add info to db about user entering the room (in the future?)
            return user.get().getUserFullName();
        }
        return "unauthorized";
    }

    private Optional<User> findUserByColor(Color color) {
        List<User> userList = userRepositoryPoC.findAll();

        return userList.stream().filter(user -> isColorSimilar(user.getColor(), color)).findFirst();
    }

    private boolean isColorSimilar(Color userColor, Color color) {
        double distance = Math.sqrt(Math.pow(userColor.getRed() - color.getRed(), 2) +
                Math.pow(userColor.getGreen() - color.getGreen(), 2) +
                Math.pow(userColor.getBlue() - color.getBlue(), 2));

        return distance <= MAXIMUM_THRESHOLD;
    }

    private static Color parse(String input)
    {
        Pattern pattern = Pattern.compile("([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5]),([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5]),([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])");
        Matcher matcher = pattern.matcher(input);

        if (matcher.matches())
        {
            return new Color(Integer.parseInt(matcher.group(1)),  // r
                    Integer.parseInt(matcher.group(2)),  // g
                    Integer.parseInt(matcher.group(3))); // b
        }

        return null;
    }
}
