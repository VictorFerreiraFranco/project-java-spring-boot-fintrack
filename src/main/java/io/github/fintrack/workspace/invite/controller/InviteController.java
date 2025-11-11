package io.github.fintrack.workspace.invite.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/invites")
@RequiredArgsConstructor
public class InviteController {

    @GetMapping("{id}")
    public void findById(
            @PathVariable String id
    ) {

    }

    @GetMapping("pending/workspace/{workspaceId}")
    public void findAllIsPendingByWorkspace(
            @PathVariable String workspaceId
    ) {

    }

    @GetMapping("pending/my")
    public void findAllIsPendingByUserLoggedIn() {

    }

    @PostMapping
    public void register() {}

    @GetMapping("accept/{id}")
    public void accept(
            @PathVariable String id
    ) {

    }

    @GetMapping("refused/{id}")
    public void refused(
            @PathVariable String id
    ) {

    }

    @GetMapping("cancel/{id}")
    public void canceled(
            @PathVariable String id
    ) {

    }

}
