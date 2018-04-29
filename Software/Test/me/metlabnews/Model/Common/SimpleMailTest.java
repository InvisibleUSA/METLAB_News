package me.metlabnews.Model.Common;

import me.metlabnews.Model.DataAccess.ConfigurationManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimpleMailTest {

    @BeforeAll
    static void init()
    {
        ConfigurationManager.getInstance().initialize();
        Logger.getInstance().initialize();
    }
    @Test
    void send() {
        SimpleMail m = new SimpleMail();
        m.Subject = "test1";
        m.Text = "abx";
        m.To = "metlabnews@gmail.com";
        m.send();
    }

    @Test
    void sendHTML()
    {
        SimpleMail m = new SimpleMail();
        m.Subject = "test1";
        m.Text = "<html><body><h1>test</h1><p>Hallo</p></body></html>";
        m.To = "metlabnews@gmail.com";
        m.sendHTML();
    }
}