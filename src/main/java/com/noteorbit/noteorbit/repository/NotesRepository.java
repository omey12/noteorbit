package com.noteorbit.noteorbit.repository;

import com.noteorbit.noteorbit.entity.Notes;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotesRepository extends JpaRepository<Notes, Long> {

    List<Notes> findByCollegeAndDeptAndYearAndClassName(
            String college, String dept, String year, String className
    );

    List<Notes> findByUploadedBy(String uploadedBy);
}