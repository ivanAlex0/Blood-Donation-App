package user;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class Scraper {

    /**
     *
     * @return counties (of type ArrayList<County>), array in which we store all of Romania's counties obtained through jsoup
     */
    public ArrayList<County> webScrapingCounties() {
        //web scraping counties of Romania
        try {
            Document docCounty = Jsoup.connect("https://ro.wikipedia.org/wiki/Lista_județelor_României_după_populație").get();
            return setCounties(docCounty);
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }
    }

    /**
     *
     * @param docCounty - from jsoup, it is the web page from which we got the name of Romania's counties
     * @return counties (of type ArrayList<County>), array in which we store all of Romania's counties obtained through jsoup, manually setting the
     * number of blood banks from each county
     */
    public ArrayList<County> setCounties(Document docCounty) {
        Elements link = docCounty.select("a[href]");

        ArrayList<County> counties = new ArrayList<County>();
        for (int i = 20; i < 61; i++) {
            County county = new County(link.get(i).text(), 1);
            counties.add(county);
            if (counties.get(i - 20).getName().equals("Hunedoara")) {
                counties.get(i - 20).setNumberOfBloodBanks(3);
            }
            if (counties.get(i - 20).getName().equals("Argeș")) {
                counties.get(i - 20).setNumberOfBloodBanks(2);
            }
            if (counties.get(i - 20).getName().equals("Covasna")) {
                counties.get(i - 20).setNumberOfBloodBanks(2);
            }
        }
        County county = new County("București", 5);
        counties.add(county);

        sortCountiesAlphabetically(counties);

        return counties;
    }

    /**
     *
     * @param counties is the arrayList with the counties of Romania, which we want to sort in alphabetical order
     *                in this case, using the Locale class, I sort Romania's counties, taking into consideration the special letters (ă, â, î, ș, ț)
     */
    public void sortCountiesAlphabetically(ArrayList<County> counties) {
        Locale locale = new Locale("ro", "RO");
        final Collator collator = Collator.getInstance(locale);

        counties.sort(new Comparator<County>() {
            @Override
            public int compare(County c1, County c2) {
                return collator.compare(c1.getName(), c2.getName());
            }
        });
        Collections.swap(counties, 39, 40);
    }
}
