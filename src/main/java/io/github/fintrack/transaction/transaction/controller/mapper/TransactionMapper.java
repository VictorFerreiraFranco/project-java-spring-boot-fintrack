package io.github.fintrack.transaction.transaction.controller.mapper;

import io.github.fintrack.transaction.installment.controller.mapper.InstallmentMapper;
import io.github.fintrack.transaction.transaction.controller.dto.TransactionRecurrenceRequest;
import io.github.fintrack.transaction.transaction.controller.dto.TransactionRequest;
import io.github.fintrack.transaction.transaction.controller.dto.TransactionResponse;
import io.github.fintrack.transaction.transaction.model.Transaction;
import io.github.fintrack.workspace.financial.category.controller.mapper.CategoryMapper;
import io.github.fintrack.workspace.payment.method.controller.mapper.MethodMapper;
import io.github.fintrack.workspace.workspace.controller.mapper.WorkspaceMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(
        componentModel = "spring",
        uses = {
                WorkspaceMapper.class,
                CategoryMapper.class,
                MethodMapper.class,
                InstallmentMapper.class,
        }
)
public interface TransactionMapper {

    TransactionResponse toResponse(Transaction transaction);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "method", ignore = true)
    @Mapping(target = "recurrence", expression = "java( Boolean.FALSE )")
    @Mapping(target = "installments", expression = "java( java.util.List.of() )")
    Transaction toTransactionByRequest(TransactionRequest request);

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "method", ignore = true)
    @Mapping(target = "recurrence", expression = "java( Boolean.TRUE )")
    @Mapping(target = "installments", expression = "java( java.util.List.of() )")
    Transaction toTransactionByRequestRecurrence(TransactionRecurrenceRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "workspace", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "method", ignore = true)
    @Mapping(target = "amountInstallment", ignore = true)
    @Mapping(target = "endDate", ignore = true)
    @Mapping(target = "recurrence", ignore = true)
    @Mapping(target = "installments", ignore = true)
    @Mapping(target = "creation", ignore = true)
    @Mapping(target = "deletion", ignore = true)
    void updateEntityByRequest(@MappingTarget Transaction entity, TransactionRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "workspace", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "method", ignore = true)
    @Mapping(target = "recurrence", ignore = true)
    @Mapping(target = "installments", ignore = true)
    @Mapping(target = "creation", ignore = true)
    @Mapping(target = "deletion", ignore = true)
    void updateEntityByRequestRecurrence(@MappingTarget Transaction entity, TransactionRecurrenceRequest request);
}
