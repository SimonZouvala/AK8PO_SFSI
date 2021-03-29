package fai.utb.db.manager;

import fai.utb.db.entity.Group;
import org.w3c.dom.Document;

import java.util.List;

public class GroupManagerImpl implements GroupManager {


    private Document document;

    public GroupManagerImpl(Document document) {
        this.document = document;
    }

    @Override
    public void create(Group group) {

    }

    @Override
    public void remove(Group group) {

    }

    @Override
    public void setQuantity(Group group, int quantity) {

    }

    @Override
    public List<Group> getAllGroup() {
        return null;
    }

    @Override
    public Group getGroup(Long id) {
        return null;
    }
}
