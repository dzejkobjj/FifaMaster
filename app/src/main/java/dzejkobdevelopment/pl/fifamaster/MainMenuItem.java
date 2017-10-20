package dzejkobdevelopment.pl.fifamaster;

/**
 * Created by Dzejkob on 02.09.2017.
 */

public class MainMenuItem {
    private final int title;
    private final int imageUrl;
    private final int id;

    public MainMenuItem(int title, int url, int id){
        this.title = title;
        this.imageUrl = url;
        this.id = id;
    }
    public int getTitle() {
        return title;
    }

    public int getImageUrl() {
        return imageUrl;
    }

    public int getId() {
        return id;
    }
}
