package KOLOKVIUMSKI;

import java.util.*;
import java.util.stream.Collectors;

class CategoryNotFoundException extends Exception {
    public CategoryNotFoundException(String message) {
        super(String.format("Category %s was not found", message));
    }
}

class Category {
    String name;

    public Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(name, category.name);
    }
}

abstract class NewsItem{
    String title;
    Date date;
    Category category;

    public NewsItem(String title, Date date, Category category) {
        this.title = title;
        this.date = date;
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public Date getDate() {
        return date;
    }

    public Category getCategory() {
        return category;
    }
    abstract String getTeaser();
}

class TextNewsItem extends NewsItem{
    String text;

    public TextNewsItem(String title, Date date, Category category, String text) {
        super(title, date, category);
        this.text = text;
    }
    public String getText() {
        return text;
    }

    @Override
    String getTeaser() {
        long diffInMinutes = (new Date().getTime() - date.getTime()) / (60 * 1000);

        StringBuilder sb = new StringBuilder();
        sb.append(title).append("\n");
        sb.append(diffInMinutes).append("\n");
        if(text.length()<80){
            sb.append(text);
        }
        else {
            sb.append(text.substring(0, 80));
        }
        return sb.toString();
    }
}
class MediaNewsItem extends NewsItem{
    String url;
    int views;

    public MediaNewsItem(String title, Date date, Category category, String url, int views) {
        super(title, date, category);
        this.url = url;
        this.views = views;
    }

    public String getUrl() {
        return url;
    }

    public int getViews() {
        return views;
    }

    @Override
    String getTeaser() {
        long diffInMinutes = (new Date().getTime() - date.getTime()) / (60 * 1000);

        StringBuilder sb = new StringBuilder();
        sb.append(title).append("\n");
        sb.append(diffInMinutes).append("\n");
        sb.append(url).append("\n");
        sb.append(views);
        return sb.toString();
    }
}

class FrontPage{
    Category [] categories;
    List<NewsItem> newsItems;

    public FrontPage(Category[] categories){
        this.categories = Arrays.copyOf(categories, categories.length);
        this.newsItems = new ArrayList<>();
    }

    void addNewsItem(NewsItem newsItem){
        newsItems.add(newsItem);
    }
    List<NewsItem> listByCategory(Category category){
        return newsItems.stream().filter(c -> c.getCategory().equals(category)).collect(Collectors.toList());
    }

    List<NewsItem> listByCategoryName(String category) throws CategoryNotFoundException {
        if(Arrays.stream(categories).noneMatch(s -> s.getName().equals(category))){
            throw new CategoryNotFoundException(category);
        }

        return newsItems.stream().filter(c -> c.getCategory().getName().equals(category)).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        String str = this.newsItems.stream().map(NewsItem::getTeaser).collect(Collectors.joining("\n"));
        return str + "\n";
    }
}
public class FrontPageTest {
    public static void main(String[] args) {
        // Reading
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        String[] parts = line.split(" ");
        Category[] categories = new Category[parts.length];
        for (int i = 0; i < categories.length; ++i) {
            categories[i] = new Category(parts[i]);
        }
        int n = scanner.nextInt();
        scanner.nextLine();
        FrontPage frontPage = new FrontPage(categories);
        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            cal = Calendar.getInstance();
            int min = scanner.nextInt();
            cal.add(Calendar.MINUTE, -min);
            Date date = cal.getTime();
            scanner.nextLine();
            String text = scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            TextNewsItem tni = new TextNewsItem(title, date, categories[categoryIndex], text);
            frontPage.addNewsItem(tni);
        }

        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int min = scanner.nextInt();
            cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, -min);
            scanner.nextLine();
            Date date = cal.getTime();
            String url = scanner.nextLine();
            int views = scanner.nextInt();
            scanner.nextLine();
            int categoryIndex = scanner.nextInt();
            scanner.nextLine();
            MediaNewsItem mni = new MediaNewsItem(title, date, categories[categoryIndex], url, views);
            frontPage.addNewsItem(mni);
        }
        // Execution
        String category = scanner.nextLine();
        System.out.println(frontPage);
        for(Category c : categories) {
            System.out.println(frontPage.listByCategory(c).size());
        }
        try {
            System.out.println(frontPage.listByCategoryName(category).size());
        } catch(CategoryNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}


// Vasiot kod ovde