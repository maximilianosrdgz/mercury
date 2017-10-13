package factory.emailprops;

import java.util.Properties;

public class HotmailProperties implements EmailProperties {

    @Override
    public Properties createProperties() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.live.com");
        props.put("mail.smtp.port", "25");
        return props;
    }
}
