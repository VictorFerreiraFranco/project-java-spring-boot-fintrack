package io.github.fintrack.workspace.payment.method.controller.contract;

import io.github.fintrack.workspace.payment.method.controller.dto.MethodFilter;
import io.github.fintrack.workspace.payment.method.controller.dto.MethodRequest;
import io.github.fintrack.workspace.payment.method.controller.dto.MethodResponse;
import io.github.fintrack.workspace.payment.method.controller.dto.MethodTypeResponse;
import io.github.fintrack.workspace.payment.method.controller.mapper.MethodMapper;
import io.github.fintrack.workspace.payment.method.model.Method;
import io.github.fintrack.workspace.payment.method.model.Type;
import io.github.fintrack.workspace.payment.method.service.MethodService;
import io.github.fintrack.workspace.workspace.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MethodContract {

    private final MethodService methodService;
    private final WorkspaceService workspaceService;
    private final MethodMapper methodMapper;

    @Transactional(readOnly = true)
    public MethodResponse getById(String id) {
        return methodMapper.toResponse(
                methodService.findByIdAndValidUserLoggedInIsMember(UUID.fromString(id))
        );
    }

    @Transactional(readOnly = true)
    public List<MethodResponse> searchAllByWorkspace(String workspaceId, MethodFilter filter) {
        return methodService.searchByWorkspace(
                        workspaceService.findByIdAndValidUserLoggedInIsMember(
                                UUID.fromString(workspaceId)
                        ),
                        filter
                )
                .stream()
                .map(methodMapper::toResponse)
                .toList();
    }

    public List<MethodTypeResponse> getTypes() {
        return Arrays.stream(Type.values())
                .map(type -> new MethodTypeResponse(type, type.getDescription()))
                .toList();
    }

    @Transactional()
    public MethodResponse register(String workspaceId, MethodRequest request) {
        Method method = methodMapper.toEntity(request);
        method.setWorkspace(workspaceService.findByIdAndValidUserLoggedInIsMember(UUID.fromString(workspaceId)));
        return methodMapper.toResponse(
                methodService.save(method)
        );
    }

    @Transactional
    public MethodResponse update(String id, MethodRequest request) {
        Method method = methodService.findByIdAndValidUserLoggedInIsMember(UUID.fromString(id));
        methodMapper.updateEntity(method, request);
        return methodMapper.toResponse(
                methodService.save(method)
        );
    }

    @Transactional
    public void delete(String id) {
        methodService.delete(
                this.methodService.findByIdAndValidUserLoggedInIsMember(UUID.fromString(id))
        );
    }

}
