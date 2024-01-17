package org.testGoogleAds.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.testGoogleAds.mapper.ActionMapper;
import org.testGoogleAds.model.Action;

import java.util.List;

@Service
public class ActionService {
    @Autowired
    private ActionMapper actionMapper;

    public List<Action> getAllActions() {
        return actionMapper.getAllActions();
    }

    public void insertAction(Action action) {
        actionMapper.insertAction(action);
    }

}

