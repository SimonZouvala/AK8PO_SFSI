package fai.utb.db.manager;

import fai.utb.db.entity.Group;

import java.util.List;
import java.util.UUID;

/**
 * This abstract class allows to manipulate with Groups.
 * @author Šimon Zouvala
 */
public abstract class GroupManager  extends BasicManager{
    /**
     * Create new {@link Group} and save to relevant XML file.
     * @param group to save
     */
    public abstract void create(Group group);

    /**
     * Remove {@link Group} and save changes to relevant XML file.
     * @param group to remove
     */
    public abstract void remove(Group group);

    /**
     * Changes number of students in group and change work labels based on this.
     * @param group where number of students will be change
     * @param quantity value to change
     */
    public abstract void setQuantity(Group group, int quantity);

    /**
     * Get all {@link Group}s from relevant XML file.
     * @return list of all {@link Group}s
     */
    public abstract List<Group> getAllGroup();

    /**
     * Get required {@link Group} from relevant XML file.
     * @param id id of required group
     * @return {@link Group}
     */
    public abstract Group getGroup(UUID id);

}
