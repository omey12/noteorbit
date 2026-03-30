package com.noteorbit.noteorbit.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "notes")
public class Notes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 📄 Basic Info
    private String title;
    private String subject;

    // 📝 Text Notes
    @Column(length = 5000)
    private String text;

    // 📁 File Upload
    private String filename;
    private String fileType;

    // 👤 User Info
    private String uploadedBy;
    private String college;
    private String dept;
    private String year;
    private String className;

    // 👍 Stats
    private int likes;
    private int downloads;

    // Constructor
    public Notes() {}

    // Getters & Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }

    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }

    public String getUploadedBy() { return uploadedBy; }
    public void setUploadedBy(String uploadedBy) { this.uploadedBy = uploadedBy; }

    public String getCollege() { return college; }
    public void setCollege(String college) { this.college = college; }

    public String getDept() { return dept; }
    public void setDept(String dept) { this.dept = dept; }

    public String getYear() { return year; }
    public void setYear(String year) { this.year = year; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public int getLikes() { return likes; }
    public void setLikes(int likes) { this.likes = likes; }

    public int getDownloads() { return downloads; }
    public void setDownloads(int downloads) { this.downloads = downloads; }
}