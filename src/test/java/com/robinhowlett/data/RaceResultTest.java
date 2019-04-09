package com.robinhowlett.data;

import com.robinhowlett.data.samples.SampleTracks;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.hateoas.Link;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RaceResultTest {

    @Test
    public void buildLinks_WithTrackRaceDateAndNumber_GeneratesFourLinks() throws Exception {
        List<Link> expected = new ArrayList<Link>() {{
            add(new Link("https://www.equibase.com/premium/chartEmb.cfm?" +
                    "track=ARP&raceDate=7/24/2016&cy=USA&rn=1", "web"));
            add(new Link("https://www.equibase.com/premium/eqbPDFChartPlus.cfm?" +
                    "RACE=1&BorP=P&TID=ARP&CTRY=USA&DT=7/24/2016&DAY=D&STYLE=EQB", "pdf"));
            add(new Link("https://www.equibase.com/premium/chartEmb.cfm?" +
                    "track=ARP&raceDate=7/24/2016&cy=USA", "allWeb"));
            add(new Link("https://www.equibase.com/premium/eqbPDFChartPlus.cfm?" +
                    "RACE=A&BorP=P&TID=ARP&CTRY=USA&DT=7/24/2016&DAY=D&STYLE=EQB", "allPdf"));
        }};

        Track track = SampleTracks.getSampleTrackAraphaoe();
        LocalDate raceDate = LocalDate.of(2016, 7, 24);

        // method under test
        List<Link> actual = RaceResult.buildLinks(track, raceDate, 1);

        Assert.assertThat(actual, Matchers.equalTo(expected));
    }
}
