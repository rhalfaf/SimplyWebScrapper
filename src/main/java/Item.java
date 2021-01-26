import java.util.Objects;

class Item implements Comparable {
    String url;
    String itemName;
    Double itemPrice;


    public Item(String url, String itemName, Double price) {
        this.url = url;
        this.itemName = itemName;
        this.itemPrice = price;
    }

    public String getUrl() {
        return url;
    }

    public String getItemName() {
        return itemName;
    }


    @Override
    public String toString() {
        return "Item{" +
                "url='" + url + '\'' +
                ", itemName='" + itemName + '\'' +
                ", itemPrice=" + itemPrice +
                '}' + "\n";
    }


    @Override
    public int compareTo(Object i) {
        return this.itemPrice.compareTo(((Item) i).itemPrice);
    }

}
