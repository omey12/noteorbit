package com.noteorbit.noteorbit.service;

import com.noteorbit.noteorbit.entity.Notes;
import com.noteorbit.noteorbit.entity.User;
import com.noteorbit.noteorbit.repository.NotesRepository;
import com.noteorbit.noteorbit.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class NotesService {

    private final NotesRepository notesRepository;
    private final UserRepository userRepository;

    // ✅ FIXED PATH (NO TOMCAT TEMP ISSUE)
    private final String UPLOAD_DIR = "C:/uploads/";

    public NotesService(NotesRepository notesRepository, UserRepository userRepository) {
        this.notesRepository = notesRepository;
        this.userRepository = userRepository;

        // ✅ ensure folder exists
        File dir = new File(UPLOAD_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    // ❌ DELETE
    public String deleteNote(Long id){
        Notes n = notesRepository.findById(id).orElse(null);
        if(n == null) return "Not found";

        notesRepository.delete(n);
        return "Deleted";
    }

    // ✏️ UPDATE TITLE / TEXT
    public String updateNote(Long id, String title, String text){
        Notes n = notesRepository.findById(id).orElse(null);
        if(n == null) return "Not found";

        n.setTitle(title);
        n.setText(text);
        notesRepository.save(n);

        return "Updated";
    }

    // 🔥 Upload Notes (FINAL FIXED)
    public String uploadNotes(String title, String subject, String text,
                             MultipartFile file, String email) throws IOException {

        User user = userRepository.findByEmail(email);
        if (user == null) return "User not found!";

        Notes note = new Notes();

        note.setTitle(title);
        note.setSubject(subject);
        note.setText(text);

        // 👤 auto user data
        note.setUploadedBy(user.getName());
        note.setCollege(user.getCollege());
        note.setDept(user.getDept());
        note.setYear(user.getYear());
        note.setClassName(user.getClassName());

     // 📁 File handling (FINAL FIXED)
        if (file != null && !file.isEmpty()) {

            String contentType = file.getContentType();

            if (!contentType.equals("application/pdf") &&
                !contentType.equals("image/png") &&
                !contentType.equals("image/jpeg")) {

                return "❌ Only PDF or Image allowed!";
            }

            String original = file.getOriginalFilename();
            String filename = System.currentTimeMillis() + "_" + original.replaceAll("\\s+", "_");

            File dest = new File(UPLOAD_DIR, filename); // ✅ FIX

            dest.getParentFile().mkdirs();

            file.transferTo(dest);

            note.setFilename(filename);
            note.setFileType(contentType);
        }

        notesRepository.save(note);

        return "Upload Successful!";
    }

    // 📄 Group Notes
    public List<Notes> getNotesByUserGroup(String email) {

        User user = userRepository.findByEmail(email);
        if (user == null) return null;

        return notesRepository.findByCollegeAndDeptAndYearAndClassName(
                user.getCollege(),
                user.getDept(),
                user.getYear(),
                user.getClassName()
        );
    }

    // 👥 Group Members
    public List<User> getGroupMembers(String email){

        User user = userRepository.findByEmail(email);
        if(user == null) return null;

        return userRepository.findAll().stream()
                .filter(u ->
                        u.getCollege().equals(user.getCollege()) &&
                        u.getDept().equals(user.getDept()) &&
                        u.getYear().equals(user.getYear()) &&
                        u.getClassName().equals(user.getClassName())
                ).toList();
    }

    // 👤 My Uploads
    public List<Notes> getMyUploads(String name) {
        return notesRepository.findByUploadedBy(name);
    }

    // 👍 Like
    public String likeNotes(Long id) {

        Notes n = notesRepository.findById(id).orElse(null);
        if (n == null) return "Not found";

        n.setLikes(n.getLikes() + 1);
        notesRepository.save(n);

        return "Liked";
    }

    public Notes getNoteById(Long id){
        return notesRepository.findById(id).orElse(null);
    }
    
    // 📥 Download
    public String downloadNotes(Long id) {

        Notes n = notesRepository.findById(id).orElse(null);
        if (n == null) return "Not found";

        n.setDownloads(n.getDownloads() + 1);
        notesRepository.save(n);

        return "Downloaded";
    }
}