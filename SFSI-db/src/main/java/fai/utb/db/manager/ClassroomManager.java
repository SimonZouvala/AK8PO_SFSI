package fai.utb.db.manager;

import fai.utb.db.entity.Classroom;

import java.util.List;

public interface ClassroomManager {

    void create(Classroom classroom);

    void setClassroomCapacity(Classroom classroomCapacity, int newCapacity);

    void remove(Classroom classroom);

    List<Classroom> getAllClassroom();

    Classroom getClassroomByAcronym(String acronym);

    Classroom getClassroom(Long id);
}
