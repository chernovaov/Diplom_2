import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

public class User {
    public String email;
    public String password;
    public String name;
    private static final int DATA_SIZE = 15;

    public User () {
    }

    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
    public static User getRandomEmailAndPassAndName () {
        final String email = randomAlphabetic(DATA_SIZE) + "@ya.com";
        final String password = randomAlphabetic(DATA_SIZE);
        final String name = randomAlphabetic(DATA_SIZE);
        return new User(email, password, name);
    }

    public User setEmail (String email){
        this.email = email;
        return this;
    }
    public User setPassword(String password) {
        this.password = password;
        return this;
    }
    public User setName (String name) {
        this.name = name;
        return this;
    }

    public static User getUserEmailOnly () {
        return new User().setEmail(randomAlphabetic(DATA_SIZE) + "@ya.com");
    }
    public static User getUserPasswordOnly () {
        return new User().setPassword(randomAlphabetic(DATA_SIZE));
    }
    public static User getUserNameOnly () {
        return new User().setName(randomAlphabetic(DATA_SIZE));
    }

    public static User getUserPasswordAndEmail () {
        return new User().setPassword(randomAlphabetic(DATA_SIZE)).setEmail(randomAlphabetic(DATA_SIZE) + "@ya.com");
    }
    public static User getUserNameAndEmail() {
        return new User().setName((randomAlphabetic(DATA_SIZE))).setEmail(randomAlphabetic(DATA_SIZE) + "@ya.com");
    }
    public static User getUserPasswordAndName() {
        return new User().setPassword((randomAlphabetic(DATA_SIZE))).setName((randomAlphabetic(DATA_SIZE)));
    }

}