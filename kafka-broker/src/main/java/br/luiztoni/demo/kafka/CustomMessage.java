package br.luiztoni.demo.kafka;

public class CustomMessage {
    private String author;
    private String content;

    @Override
    public String toString() {
        return "CustomMessage{" +
                "author='" + author + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
