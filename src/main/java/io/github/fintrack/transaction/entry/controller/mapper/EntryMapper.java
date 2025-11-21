package io.github.fintrack.transaction.entry.controller.mapper;

import io.github.fintrack.transaction.entry.controller.dto.EntryTransactionByYearResponse;
import io.github.fintrack.transaction.entry.controller.dto.SingleCategoryResponse;
import io.github.fintrack.transaction.entry.controller.dto.EntryTransactionByMonthYearResponse;
import io.github.fintrack.transaction.entry.controller.dto.SingleMethodResponse;
import io.github.fintrack.transaction.installment.controller.dto.InstallmentResponse;
import io.github.fintrack.transaction.installment.model.Installment;
import io.github.fintrack.transaction.transaction.model.Transaction;
import io.github.fintrack.workspace.financial.category.model.Category;
import io.github.fintrack.workspace.payment.method.model.Method;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface EntryMapper {

    @Mapping(target = "id", source = "entry.id")
    @Mapping(target = "date", source = "installment.date")
    @Mapping(target = "amount", source = "installment.amount")
    EntryTransactionByMonthYearResponse toTransactionByMonthYearResponse(Transaction entry, Installment installment);

    @Mapping(target = "id", source = "entry.id")
    @Mapping(
            target = "installments",
            expression = "java( installmentsToMap(installments) )"
    )
    EntryTransactionByYearResponse toTransactionByYearResponse(Transaction entry, List<Installment> installments);

    SingleMethodResponse toMethodResponse(Method method);

    SingleCategoryResponse toCategoryResponse(Category category);

    InstallmentResponse toInstallmentResponse(Installment installment);

    default Map<Integer, InstallmentResponse> installmentsToMap(List<Installment> installments) {
        return installments.stream()
                .collect(
                        Collectors.toMap(
                                inst -> inst.getDate().getMonthValue(),
                                this::toInstallmentResponse,
                                (a, b) -> a
                        )
                );
    }
}
