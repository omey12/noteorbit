package com.noteorbit.noteorbit.controller;

import com.noteorbit.noteorbit.entity.Notes;
import com.noteorbit.noteorbit.entity.User;
import com.noteorbit.noteorbit.service.NotesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/notes")
@CrossOrigin(origins = "*")
public class NotesController {

    @Autowired
    private NotesService notesService;

    private final String UPLOAD_DIR = "C:/uploads/";

    // 🔥 Upload
    @PostMapping("/upload")
    public String upload(
            @RequestParam String title,
            @RequestParam String subject,
            @RequestParam String text,
            @RequestParam(required = false) MultipartFile file,
            @RequestParam String email
    ) throws IOException {

        return notesService.uploadNotes(title, subject, text, file, email);
    }

    // 📄 Group Notes
    @GetMapping("/all")
    public List<Notes> getAll(@RequestParam String email) {
        return notesService.getNotesByUserGroup(email);
    }

    // 👤 My uploads
    @GetMapping("/my")
    public List<Notes> my(@RequestParam String name) {
        return notesService.getMyUploads(name);
    }

    // 👍 Like
    @PutMapping("/like")
    public String like(@RequestParam Long id) {
        return notesService.likeNotes(id);
    }
    @GetMapping("/test")
    public String test(){
        return "Working";
    }

    // 👥 Group members
    @GetMapping("/members")
    public List<User> members(@RequestParam String email){
        return notesService.getGroupMembers(email);
    }

    // ❌ Delete
    @DeleteMapping("/delete")
    public String delete(@RequestParam Long id){
        return notesService.deleteNote(id);
    }

    // ✏️ Update
    @PutMapping("/update")
    public String update(@RequestParam Long id,
                         @RequestParam String title,
                         @RequestParam String text){

        return notesService.updateNote(id, title, text);
    }

 // 📥 Download count
    @PutMapping("/download-count")
    public String download(@RequestParam Long id) {
        return notesService.downloadNotes(id);
    }
    // 📥 File download
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) throws IOException {

        Notes note = notesService.getNoteById(id);
        if (note == null || note.getFilename() == null)
            return ResponseEntity.notFound().build();

        File file = new File(UPLOAD_DIR, note.getFilename()); // ✅ FIX

        if (!file.exists())
            return ResponseEntity.notFound().build();

        Resource resource = new UrlResource(file.toURI());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.getName() + "\"")
                .body(resource);
    }

    // 👀 Preview (image/pdf)
    @GetMapping("/preview/{id}")
    public ResponseEntity<Resource> previewFile(@PathVariable Long id) throws IOException {

        Notes note = notesService.getNoteById(id);
        if (note == null || note.getFilename() == null)
            return ResponseEntity.notFound().build();

        File file = new File(UPLOAD_DIR, note.getFilename()); // ✅ FIX

        if (!file.exists())
            return ResponseEntity.notFound().build();

        Resource resource = new UrlResource(file.toURI());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE,
                        note.getFileType() != null ? note.getFileType() : "application/octet-stream")
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                .body(resource);
    }
}