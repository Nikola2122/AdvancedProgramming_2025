package LABS.LAB9;


import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

interface IDocument {
    void display();

    String getText();
}

class Document implements IDocument {
    String text;

    public Document(String text) {
        this.text = text;
    }

    @Override
    public void display() {
        System.out.println(getText());
    }

    @Override
    public String getText() {
        return text;
    }
}

abstract class DocumentDecorator implements IDocument {
    IDocument wrappedDocument;

    public DocumentDecorator(IDocument wrappedDocument) {
        this.wrappedDocument = wrappedDocument;
    }

    @Override
    public void display() {
        String text = getText();
        if(text.endsWith("\n")) {
            System.out.println(text.substring(0, text.length() - 1));
        }
        else System.out.println(text);
    }

    @Override
    public String getText() {
        return wrappedDocument.getText();
    }
}

class EnableLineDD extends DocumentDecorator {
    public EnableLineDD(IDocument wrappedDocument) {
        super(wrappedDocument);

    }

    @Override
    public String getText() {
        String str;
        String[] arr = wrappedDocument.getText().split("\n");
        str = IntStream.range(1, arr.length + 1).
                mapToObj(i -> i + ": " + arr[i - 1]).
                collect(Collectors.joining("\n"));
        str += "\n";
        return str;
    }
}

class EnableWordCountDD extends DocumentDecorator {
    public EnableWordCountDD(IDocument wrappedDocument) {
        super(wrappedDocument);
    }

    @Override
    public String getText() {
        String str;
        String base = wrappedDocument.getText();
        String[] arr = base.replace("\n", " ").split(" ");
        int length = arr.length;
        str = base + "Words: " + length;
        return str;
    }
}

class EnableRedactionDD extends DocumentDecorator {

    List<String> redactions;

    public EnableRedactionDD(IDocument wrappedDocument, List<String> redactions) {
        super(wrappedDocument);
        this.redactions = redactions;
    }

    @Override
    public String getText() {
        String[] arr = wrappedDocument.getText().split(" ");
        StringBuilder sb = new StringBuilder();
        for (String s : arr) {
            if (s.contains("\n")) {
                String[] tokens = s.split("\n");
                if (tokens.length == 1) {
                    if (redactions.contains(tokens[0])) {
                        sb.append("*");
                    } else {
                        sb.append(s);
                    }
                } else {
                    if (redactions.contains(tokens[0])) {
                        sb.append("*").append("\n");
                    } else {
                        sb.append(tokens[0]).append("\n");
                    }
                    if (redactions.contains(tokens[1].toLowerCase())) {
                        sb.append("*").append(" ");
                    } else {
                        sb.append(tokens[1]).append(" ");
                    }
                }
            } else {
                if (redactions.contains(s)) {
                    sb.append("*").append(" ");
                } else {
                    sb.append(s).append(" ");
                }
            }
        }
        return sb.toString();
    }
}

class DocumentViewer {

    Map<String, IDocument> documents;

    public DocumentViewer() {
        documents = new HashMap<>();
    }

    void addDocument(String id, String text) {
        this.documents.put(id, new Document(text));
    }

    void enableLineNumbers(String id) {
        IDocument document = documents.get(id);
        document = new EnableLineDD(document);
        documents.put(id, document);
    }

    void enableWordCount(String id) {
        IDocument document = documents.get(id);
        document = new EnableWordCountDD(document);
        documents.put(id, document);
    }

    void enableRedaction(String id, List<String> forbiddenWords) {
        IDocument document = documents.get(id);
        document = new EnableRedactionDD(document, forbiddenWords);
        documents.put(id, document);
    }

    void display(String id) {
        IDocument document = documents.get(id);
        System.out.printf("=== Document %s ===\n", id);
        document.display();
    }
}

public class Documents {
    public static void main(String[] args) {
        DocumentViewer viewer = new DocumentViewer();
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        for (int i = 0; i < n; i++) {
            String id = sc.next();
            int lines = sc.nextInt();
            sc.nextLine();
            StringBuilder builder = new StringBuilder();
            for (int j = 0; j < lines; j++) {
                String line = sc.nextLine();
                builder.append(line).append("\n");
            }
            viewer.addDocument(id, builder.toString());
        }

        String action;
        while ((action = sc.nextLine()) != null) {
            if (action.equals("exit")) {
                break;
            }
            String[] tokens = action.split(" ");
            String toDo = tokens[0];
            String id = tokens[1];
            switch (toDo) {
                case "enableLineNumbers":
                    viewer.enableLineNumbers(id);
                    break;
                case "enableWordCount":
                    viewer.enableWordCount(id);
                    break;
                case "enableRedaction":
                    viewer.enableRedaction(id, Arrays.stream(tokens).skip(2).collect(Collectors.toList()));
                    break;
                case "display":
                    viewer.display(id);
                    break;
            }
        }
    }
}
