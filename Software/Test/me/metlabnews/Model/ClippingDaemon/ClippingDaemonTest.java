package me.metlabnews.Model.ClippingDaemon;

import me.metlabnews.Model.Common.Logger;
import me.metlabnews.Model.DataAccess.ConfigurationManager;
import me.metlabnews.Model.DataAccess.Queries.BaseX.QueryAddProfile;
import me.metlabnews.Model.Entities.ObservationProfile;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ClippingDaemonTest {
    @BeforeAll
    static void init()
    {
        ConfigurationManager.getInstance().initialize();
        Logger.getInstance().initialize();
    }

    @Test
    void run()
    {
        QueryAddProfile qap = new QueryAddProfile();
        qap.name = "abctest" + LocalTime.now().format(DateTimeFormatter.ofPattern("HHmmss"));
        ArrayList<String> keywords = new ArrayList<>();
        ArrayList<String> sources = new ArrayList<>();
        keywords.add("USA");
        sources.add("Spiegel");
        qap.profile = new ObservationProfile(qap.name, "ede1998@arcor.de", keywords, sources, LocalTime.now().plusSeconds(12));
	    qap.profile = new ObservationProfile(qap.name, "tobias.reis@gmx.de", keywords, sources,
	                                         LocalTime.now().plusSeconds(12));
        assert qap.execute();

        ClippingDaemon cd = new ClippingDaemon();
        cd.initialize();

        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            cd.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}