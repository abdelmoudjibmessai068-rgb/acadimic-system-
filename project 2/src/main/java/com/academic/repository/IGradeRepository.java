package com.academic.repository;

import com.academic.model.Grade;
import org.bson.types.ObjectId;
import java.util.List;

public interface IGradeRepository extends IRepository<Grade> {
    List<Grade> findByStudentId(ObjectId studentId);
    List<Grade> findBySubjectId(ObjectId subjectId);
}
