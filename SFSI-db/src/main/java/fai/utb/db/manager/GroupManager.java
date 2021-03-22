package fai.utb.db.manager;

import fai.utb.db.entity.Group;

import java.util.List;

public interface GroupManager {

    void create(Group group);

    void remove(Group group);

    void setQuantity(Group group, int quantity);

    List<Group> getAllGroup();

    Group getGroup(Long id);

}
