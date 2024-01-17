package org.testGoogleAds.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testGoogleAds.mapper.ActionMapper;
import org.testGoogleAds.model.Action;

import java.util.List;

@RestController
@RequestMapping("/api/action")
public class ActionController {

    private final ActionMapper actionMapper;

    @Autowired
    public ActionController(ActionMapper actionMapper) {
        this.actionMapper = actionMapper;
    }


    @GetMapping("/actions")
    public ResponseEntity<List<Action>> getAllActions() {
        List<Action> actions = actionMapper.getAllActions();

        if (actions.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(actions, HttpStatus.OK);
        }
    }

    @PostMapping("/insertActions")
    public ResponseEntity<String> createAction(@RequestBody Action action) {
        try {
            action.id = null;
            actionMapper.insertAction(action);

            return new ResponseEntity<>("Action created successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error creating action: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
