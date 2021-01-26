import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Locale;

public class AuctionScrapper implements CommonScrapper {

    private String baseURL = "https://allegro.pl/listing?string=";
    private String scrappedURL = "https://allegro.pl/listing?string=";
    private ArrayList<Item> searchResult = new ArrayList<>();

    public AuctionScrapper(String searchedProduct) {
        this.scrappedURL += searchedProduct;
        this.baseURL += searchedProduct;
    }

    @Override
    public ArrayList<Item> scrappAndReturnResult() {
        try {
            Integer numberOfPages = getNumberOfPagesForScrapping();
            int paginationCounter = 0;
            if (numberOfPages > 0) {
                do {
                    Document newDocument = Jsoup.connect(scrappedURL).userAgent("Mozilla/5.0").get();
                    Elements elements = newDocument.getElementsByTag("article");
                    iterateThruElements(elements);
                    paginationCounter++;
                    scrappedURL = baseURL +  "&p="+paginationCounter;
                } while (paginationCounter <= numberOfPages);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return searchResult;
    }

    private Integer getNumberOfPagesForScrapping() throws IOException {
        Document newDocument = Jsoup.connect(scrappedURL).userAgent("Mozilla/5.0").get();
        return Integer.parseInt(newDocument.select("input").attr("data-maxpage"));
    }

    private void iterateThruElements(Elements elements) {
        for (Element e : elements) {
            String itemLink = getItemLinkByTag(e, "abs:Href");
            if (itemLink.contains("oferta")) {
                NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
                Number price = format.parse(getItemPriceByClass(e, "_1svub _lf05o"), new ParsePosition(0));
                String itemName = getProductNameByClass(e, "_w7z6o");
                Double itemPrice = price.doubleValue();
                Item tmpItem = new Item(itemLink, itemName, itemPrice);
                if(searchResult.stream().anyMatch(item -> item.getUrl().equals(itemLink))){
                    continue;
                }
                searchResult.add(tmpItem);
            }
        }
    }

    @Override
    public String getItemLinkByTag(Element e, String s) {
        return e.getElementsByTag("a").attr(s);
    }

    @Override
    public String getItemPriceByClass(Element e, String className) {
        return e.getElementsByClass(className).text().replace(" ", "");
    }

    @Override
    public String getProductNameByClass(Element e, String className) {
        return e.getElementsByClass(className).text();
    }
}
