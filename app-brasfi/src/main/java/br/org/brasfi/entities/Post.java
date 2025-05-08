package br.org.brasfi.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String content;

    @ManyToOne
    private Member author;

    private LocalDateTime creationDate = LocalDateTime.now();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> comments = new ArrayList<>(); //ta dando esse erro pq a classe message n foi implementada ainda sacou
                                                        //ai eu nao quis adicionar uma pra tirar esse erro.
    private String mediaFile;

    @ManyToMany
    private List<Member> likes = new ArrayList<>();

    private boolean edited;

    private LocalDateTime editDate;

    private boolean visible = true;

    public Post() {
    }

    public Post(String content, Member author, String mediaFile) {
        this.content = content;
        this.author = author;
        this.mediaFile = mediaFile;
        this.creationDate = LocalDateTime.now();
        this.visible = true;
        this.edited = false;
        this.comments = new ArrayList<>();
        this.likes = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Member getAuthor() {
        return author;
    }

    public void setAuthor(Member author) {
        this.author = author;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public List<Message> getComments() {
        return comments;
    }

    public void setComments(List<Message> comments) {
        this.comments = comments;
    }

    public String getMediaFile() {
        return mediaFile;
    }

    public void setMediaFile(String mediaFile) {
        this.mediaFile = mediaFile;
    }

    public List<Member> getLikes() {
        return likes;
    }

    public void setLikes(List<Member> likes) {
        this.likes = likes;
    }

    public boolean isEdited() {
        return edited;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
    }

    public LocalDateTime getEditDate() {
        return editDate;
    }

    public void setEditDate(LocalDateTime editDate) {
        this.editDate = editDate;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
