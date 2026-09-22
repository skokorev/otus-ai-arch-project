package ru.yahoondex.archhelper.configurator.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yahoondex.archhelper.configurator.controllers.dto.Group;
import ru.yahoondex.archhelper.configurator.services.GroupService;
import ru.yahoondex.archhelper.configurator.services.dto.GroupLite;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private GroupService groupService;

    @Autowired
    public AdminController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping(path = "/groups")
    public List<GroupLite> getGroups() {
        return groupService.getGroups();
    }

    @GetMapping(path = "/groups/{id}")
    public Group getGroup(@PathVariable("id") String id) {
        return groupService.getGroup(id);
    }

    @PostMapping(path = "/groups")
    public void createGroup(String name) {
        groupService.createGroup(name);
    }

    @PutMapping(path = "/groups")
    public void renameGroup(String groupId, String newName) {
        groupService.renameGroup(groupId, newName);
    }

    @DeleteMapping(path = "/groups/{id}")
    public void deleteGroup(@PathVariable("id") String id) {
        groupService.deleteGroup(id);
    }

    @PostMapping(path = "/groups/{id}/set")
    public void addNameSet(@PathVariable("id") String groupId, String setId) {
        groupService.addNameSet(groupId, setId);
    }

    @DeleteMapping(path = "/groups/{id}/set/{setId}")
    public void deleteNameSet(@PathVariable("id") String groupId, @PathVariable("setId") String setId) {
        groupService.deleteNameSet(groupId, setId);
    }

    @PostMapping(path = "/groups/{id}/users")
    public void addUser(@PathVariable("id") String id, String email) {
        groupService.addUser(id, email);
    }

    @DeleteMapping(path = "/groups/{id}/users")
    public void removeUser(@PathVariable("id") String id, String email) {
        groupService.removeUser(id, email);
    }
}
