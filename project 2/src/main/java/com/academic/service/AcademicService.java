package com.academic.service;

import com.academic.model.*;
import com.academic.repository.*;
import org.bson.types.ObjectId;

import java.util.List;

public class AcademicService {
    private final IUserRepository userRepository;
    private final IRepository<Group> groupRepository;
    private final SubjectRepositoryImpl subjectRepository;
    private final GradeRepositoryImpl gradeRepository;

    public AcademicService() {
        this.userRepository = new UserRepositoryImpl();
        this.groupRepository = new GroupRepositoryImpl();
        this.subjectRepository = new SubjectRepositoryImpl();
        this.gradeRepository = new GradeRepositoryImpl();
    }

    // Auth
    public User login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    // Admin CRUD
    public void createUser(User user) { userRepository.save(user); }
    public List<User> getAllUsers() { return userRepository.findAll(); }
    public List<User> getUsersByRole(String role) { return userRepository.findByRole(role); }
    public void deleteUser(ObjectId id) { userRepository.delete(id); }

    public void createGroup(Group group) { groupRepository.save(group); }
    public List<Group> getAllGroups() { return groupRepository.findAll(); }
    public Group getGroupById(ObjectId id) { return groupRepository.findById(id); }

    public void createSubject(Subject subject) { subjectRepository.save(subject); }
    public List<Subject> getAllSubjects() { return subjectRepository.findAll(); }
    public List<Subject> getSubjectsByTeacher(ObjectId teacherId) { return subjectRepository.findByTeacherId(teacherId); }

    // Teacher Logic
    public void enterGrade(ObjectId studentId, ObjectId subjectId, ObjectId teacherId, double score) {
        Grade grade = gradeRepository.findByStudentAndSubject(studentId, subjectId);
        if (grade == null) {
            grade = new Grade(studentId, subjectId, teacherId, score);
            gradeRepository.save(grade);
        } else {
            grade.setScore(score);
            grade.setTeacherId(teacherId);
            gradeRepository.update(grade);
        }
    }

    public List<Grade> getGradesForStudent(ObjectId studentId) {
        return gradeRepository.findByStudentId(studentId);
    }
    
    public Subject getSubjectById(ObjectId id) {
        return subjectRepository.findById(id);
    }
}
