package ohtu;

import ohtu.data_access.FileUserDao;
import ohtu.io.ConsoleIO;
import ohtu.io.IO;
import ohtu.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class App {

    private IO io;
    private AuthenticationService auth;

    @Autowired
    public App(IO io, AuthenticationService auth) {
        this.io = io;
        this.auth = auth;
    }

    public String[] ask() {
        String[] userPwd = new String[2];
        userPwd[0] = io.readLine("username:");
        userPwd[1] = io.readLine("password:");
        return userPwd;
    }

    public void run() {
        while (true) {
            if (prompt()) break;
        }
    }

    private boolean prompt() {
        String command = io.readLine(">");
        if (command.equals("new")) {
            return newRegistration();

        } else if (command.equals("login")) {
            return login();
        }
        return command.isEmpty();
    }

    private boolean login() {
        String[] usernameAndPassword = ask();
        if (auth.logIn(usernameAndPassword[0], usernameAndPassword[1])) {
            io.print("logged in");
        } else {
            io.print("wrong username or password");
        }
        return false;
    }

    private boolean newRegistration() {
        String[] usernameAndPassword = ask();
        if (auth.createUser(usernameAndPassword[0], usernameAndPassword[1])) {
            io.print("new user registered");
        } else {
            io.print("new user not registered");
        }
        return false;
    }

    public static void main(String[] args) {
        ApplicationContext ctx = new FileSystemXmlApplicationContext("src/main/resources/spring-context.xml");

        App application = (App) ctx.getBean("app");
//        App application = new App(new ConsoleIO(), new AuthenticationService(new FileUserDao("salasanat.txt")));
        application.run();
    }

    // testejä debugatessa saattaa olla hyödyllistä testata ohjelman ajamista
    // samoin kuin testi tekee, eli injektoimalla käyttäjän syötteen StubIO:n avulla
    //
    // UserDao dao = new InMemoryUserDao();  
    // StubIO io = new StubIO("new", "eero", "sala1nen" );   
    //  AuthenticationService auth = new AuthenticationService(dao);
    // new App(io, auth).run();
    // System.out.println(io.getPrints());
}
