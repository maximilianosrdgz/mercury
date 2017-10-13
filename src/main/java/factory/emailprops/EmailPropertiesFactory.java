package factory.emailprops;

import org.springframework.stereotype.Component;

@Component
public class EmailPropertiesFactory {

    public EmailProperties getProperties(String host) {
        if(host == null) {
            return null;
        }
        if(host.equals("gmail.com")) {
            return new GmailProperties();
        }
        else {
            if(host.equals("hotmail.com")) {
                return new HotmailProperties();
            }
            else return null;
        }
    }
}
