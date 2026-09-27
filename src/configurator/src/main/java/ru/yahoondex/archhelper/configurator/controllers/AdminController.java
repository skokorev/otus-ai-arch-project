package ru.yahoondex.archhelper.configurator.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.yahoondex.archhelper.configurator.controllers.dto.Group;
import ru.yahoondex.archhelper.configurator.services.GroupService;
import ru.yahoondex.archhelper.configurator.services.SetService;
import ru.yahoondex.archhelper.configurator.services.dto.GroupLite;
import ru.yahoondex.archhelper.configurator.services.dto.SetLite;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private GroupService groupService;
    private SetService setService;

    @Autowired
    public AdminController(GroupService groupService, SetService setService) {
        this.groupService = groupService;
        this.setService = setService;
    }

    @GetMapping(path = "/sets")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<SetLite> getSets() {
        return setService.getAllSets();
    }
    @GetMapping(path = "/groups")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<GroupLite> getGroups() {
        return groupService.getGroups();
    }

    @GetMapping(path = "/groups/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Group getGroup(@PathVariable("id") String id) {
        return groupService.getGroup(id);
    }

    private record GroupId(String groupId) {}
    @PostMapping(path = "/groups")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public GroupId createGroup(String name) {
        return new GroupId(groupService.createGroup(name).groupId());
    }

    @PutMapping(path = "/groups")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public GroupId renameGroup(String groupId, String newName) {
        groupService.renameGroup(groupId, newName);
        return new GroupId(groupId);
    }

    @DeleteMapping(path = "/groups/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteGroup(@PathVariable("id") String id) {
        groupService.deleteGroup(id);
    }

    @PostMapping(path = "/groups/{id}/set")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void addNameSet(@PathVariable("id") String groupId, String setId) {
        groupService.addNameSet(groupId, setId);
    }

    @DeleteMapping(path = "/groups/{id}/set/{setId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteNameSet(@PathVariable("id") String groupId, @PathVariable("setId") String setId) {
        groupService.deleteNameSet(groupId, setId);
    }

    @PostMapping(path = "/groups/{id}/users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void addUser(@PathVariable("id") String id, String email) {
        groupService.addUser(id, email);
    }

    @DeleteMapping(path = "/groups/{id}/users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void removeUser(@PathVariable("id") String id, String email) {
        groupService.removeUser(id, email);
    }
}
