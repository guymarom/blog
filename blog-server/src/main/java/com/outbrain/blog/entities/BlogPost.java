package com.outbrain.blog.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.TableGenerator;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * User: maromg
 * Date: 30/06/2014
 */
@Entity
//todo why does it return 406??
public class BlogPost {

    @Id
    @Column(unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GENERATOR", table = "BLOG_SEQUENCE")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GENERATOR")
    private long id;

    @Pattern(regexp = "\\b[A-Za-z0-9\\._%\\+-]+@[A-Za-z0-9\\.-]+\\.[A-Za-z]{2,4}\\b")
    @Column(nullable = false)
    private String authorEmail;

    @Column(length = 150, nullable = false, unique = true)
    @Size(min = 1, max = 150)
    private String title;

    @Column(nullable = false)
    @Lob
    private String content;

    public long getId() {
        return id;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "BlogPost{" +
                "id=" + id +
                ", authorEmail='" + authorEmail + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
