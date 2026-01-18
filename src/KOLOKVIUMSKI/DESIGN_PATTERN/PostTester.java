package KOLOKVIUMSKI.DESIGN_PATTERN;

import java.util.*;

interface IComment {
    IComment getComment(String commentId);
    void likeComment();
    void addReply(String commentId, IComment comment);
    String toString(int level);
    int likes();
}

class Comment implements IComment, Comparable<Comment> {

    String username;
    String commentContent;
    String commentId;
    int likes;
    Map<String, IComment> replies;

    public Comment(String username, String commentContent, String commentId) {
        this.username = username;
        this.commentContent = commentContent;
        this.replies = new LinkedHashMap<>();
        this.commentId = commentId;
        this.likes = 0;
    }

    @Override
    public IComment getComment(String commentId) {
        if (this.replies.containsKey(commentId))
            return this.replies.get(commentId);

        for (IComment reply : replies.values()) {
            IComment found = reply.getComment(commentId);
            if (found != null)
                return found;
        }
        return null;
    }

    @Override
    public void likeComment() {
        this.likes++;
    }

    @Override
    public void addReply(String commentId, IComment comment) {
        this.replies.put(commentId, comment);
    }

    @Override
    public String toString(int level) {
        String indent = "    ".repeat(level);
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%sComment: %s\n%sWritten by: %s\n%sLikes: %d\n", indent, commentContent,
                indent, username, indent, likes));
        replies.values().stream().sorted().forEach(v -> sb.append(v.toString(level+1)));
        return sb.toString();
    }

    @Override
    public int likes() {
        return this.likes + replies.values().stream().mapToInt(IComment::likes).sum();
    }

    @Override
    public int compareTo(Comment o) {
        int cmp = Integer.compare(o.likes(), this.likes()); // likes DESC
        if (cmp != 0) return cmp;

        return this.commentId.compareTo(o.commentId); // ID ASC
    }
}

class Post{
    private String username;
    private String postContent;
    private Map<String, IComment> comments;

    public Post(String username, String postContent) {
        this.username = username;
        this.postContent = postContent;
        this.comments = new LinkedHashMap<>();
    }

    IComment getComment(String commentId) {
        if (this.comments.containsKey(commentId))
            return this.comments.get(commentId);

        for(IComment comment : this.comments.values()){
            IComment found = comment.getComment(commentId);
            if(found != null){
                return found;
            }
        }

        return null;
    }

    public void addComment(String author, String id, String content, String replyToId) {
        Comment newComment = new Comment(author, content, id);
        if(replyToId == null){
            this.comments.put(id, newComment);
        }
        else{
            IComment comment = getComment(replyToId);
            comment.addReply(id, newComment);
        }
    }

    public void likeComment(String commentId) {
        IComment comment = getComment(commentId);
        comment.likeComment();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Post: %s\nWritten by: %s\nComments:\n", this.postContent, this.username));
        comments.values().stream().sorted().forEach(comment -> sb.append(comment.toString(2)));
        return sb.toString();
    }
}


public class PostTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String postAuthor = sc.nextLine();
        String postContent = sc.nextLine();

        Post p = new Post(postAuthor, postContent);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split(";");
            String testCase = parts[0];

            if (testCase.equals("addComment")) {
                String author = parts[1];
                String id = parts[2];
                String content = parts[3];
                String replyToId = null;
                if (parts.length == 5) {
                    replyToId = parts[4];
                }
                p.addComment(author, id, content, replyToId);
            } else if (testCase.equals("likes")) { //likes;1;2;3;4;1;1;1;1;1 example
                for (int i = 1; i < parts.length; i++) {
                    p.likeComment(parts[i]);
                }
            } else {
                System.out.println(p);
            }

        }
    }
}
