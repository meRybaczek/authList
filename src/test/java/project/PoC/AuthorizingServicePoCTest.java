package project.PoC;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.Color;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorizingServicePoCTest {

    public static final String UNAUTHORIZED = "unauthorized";
    private static final String TEST_NAME = "Johnny Mielony";
    private final UserRepositoryPoC userRepositoryPoC = mock(UserRepositoryPoC.class);
    private final AuthorizingServicePoC authorizingServicePoC = new AuthorizingServicePoC(userRepositoryPoC);

    @Test
    void testAuthorizeMethodHappyPath() {
        //given
        mockUsers();
        String givenString = "254,254,254";

        //when
        String resultString = authorizingServicePoC.authorize(givenString);

        //then
        assertThat(resultString).isEqualTo(TEST_NAME);
    }

    @Test
    void testAuthorizeMethodSadPath() {
        //given
        mockUsers();
        String givenString = "128,200,0";

        //when
        String resultString = authorizingServicePoC.authorize(givenString);

        //then
        assertThat(resultString).isEqualTo(UNAUTHORIZED);
    }

    @Test
    void testAuthorizeMethodWithNoUsers() {
        //given
        String givenString = "0,0,0";

        //when
        String resultString = authorizingServicePoC.authorize(givenString);

        //then
        assertThat(userRepositoryPoC.findAll()).isEmpty();
        assertThat(resultString).isEqualTo(UNAUTHORIZED);
    }

    private void mockUsers() {
        User user = new User(TEST_NAME,Color.WHITE);
        User user2 = new User("John Nowak", Color.BLACK);
        when(userRepositoryPoC.findAll()).thenReturn(List.of(user, user2));
    }
}