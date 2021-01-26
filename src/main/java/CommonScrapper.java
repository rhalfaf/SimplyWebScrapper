import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

public interface CommonScrapper {
    String scrappedURL = null;
    LinkedList<Item> searchResult = null;

    ArrayList<Item> scrappAndReturnResult();

    String getItemLinkByTag(Element e, String s);

    String getItemPriceByClass(Element e, String className);

    String getProductNameByClass(Element e, String className);
}
