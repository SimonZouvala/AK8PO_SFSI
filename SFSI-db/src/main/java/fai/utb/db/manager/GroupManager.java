package fai.utb.db.manager;

import fai.utb.db.entity.Group;

import java.util.List;

public abstract class GroupManager  extends BasicManager{

    public abstract void create(Group group);

    public abstract void remove(Group group);

    public abstract void setQuantity(Group group, int quantity);

    public abstract List<Group> getAllGroup();

    public abstract Group getGroup(Long id);

}
