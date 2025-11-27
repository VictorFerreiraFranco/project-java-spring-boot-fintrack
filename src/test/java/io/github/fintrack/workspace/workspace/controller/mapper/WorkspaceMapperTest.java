package io.github.fintrack.workspace.workspace.controller.mapper;

import io.github.fintrack.workspace.member.model.Member;
import io.github.fintrack.workspace.member.model.Role;
import io.github.fintrack.workspace.workspace.controller.dto.WorkspaceDetailsResponse;
import io.github.fintrack.workspace.workspace.controller.dto.WorkspaceRequest;
import io.github.fintrack.workspace.workspace.controller.dto.WorkspaceSingleResponse;
import io.github.fintrack.workspace.workspace.model.Workspace;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class WorkspaceMapperTest {

    @Autowired
    private WorkspaceMapper workspaceMapper;
    
    @Test
    @DisplayName("Should map WorkspaceRequest to Workspace")
    void shouldMapWorkspaceToWorkspaceResponse(){
        WorkspaceRequest request = new WorkspaceRequest("Test Workspace");

        Workspace entity = workspaceMapper.toEntity(request);

        assertThat(entity.getName()).isEqualTo(request.name());

        assertThat(entity.getId()).isNull();
        assertThat(entity.getType()).isNull();
        assertThat(entity.getMembers()).isNull();
        assertThat(entity.getOwner()).isNull();
    }

    @Test
    @DisplayName("Should map Workspace to WorkspaceDetailsResponse")
    void shouldMapWorkspaceToWorkspaceDetailsResponse(){
        Member owner = Member.builder()
                .role(Role.OWNER)
                .build();
        owner.setId(UUID.randomUUID());

        Member member = Member.builder()
                .role(Role.MEMBER)
                .build();
        member.setId(UUID.randomUUID());

        Workspace workspace = Workspace.builder()
                .name("Test Workspace")
                .build();
        workspace.setId(UUID.randomUUID());
        workspace.getCreation().setCreatedAt(LocalDateTime.now());
        workspace.setMembers(List.of(owner, member));

        WorkspaceDetailsResponse detailsResponse = workspaceMapper.toDetailsResponse(workspace);

        assertThat(detailsResponse.id()).isEqualTo(workspace.getId());
        assertThat(detailsResponse.name()).isEqualTo(workspace.getName());
        assertThat(detailsResponse.created()).isEqualTo(workspace.getCreation().getCreatedAt());
        assertThat(detailsResponse.owner().id()).isEqualTo(owner.getId());
        assertThat(detailsResponse.members())
                .hasSize(2);
    }

    @Test
    @DisplayName("Should map Workspace to WorkspaceSingleResponse")
    void shouldMapWorkspaceToWorkspaceSingleResponse(){
        Workspace workspace = Workspace.builder()
                .name("Test Workspace")
                .build();
        workspace.setId(UUID.randomUUID());
        workspace.getCreation().setCreatedAt(LocalDateTime.now());

        WorkspaceSingleResponse singleResponse = workspaceMapper.toSingleResponse(workspace);

        assertThat(singleResponse.id()).isEqualTo(workspace.getId());
        assertThat(singleResponse.name()).isEqualTo(workspace.getName());
        assertThat(singleResponse.created()).isEqualTo(workspace.getCreation().getCreatedAt());
    }
}
