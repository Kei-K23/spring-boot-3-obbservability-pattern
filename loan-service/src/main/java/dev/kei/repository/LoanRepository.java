package dev.kei.repository;

import dev.kei.dto.LoanResponse;
import dev.kei.entity.Loan;
import io.micrometer.observation.annotation.Observed;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
@Observed
public class LoanRepository {
    private final JdbcClient jdbcClient;

    public LoanRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Transactional(readOnly = true)
    public List<LoanResponse> findAllLoan() {
        var selectQuery = "SELECT * FROM loans";
        return jdbcClient.sql(selectQuery).query(LoanResponse.class).list();
    }

    @Transactional
    public void save(Loan loan) {
        var insertQuery = "INSERT INTO loans(loanId, customerName, customerId, amount, loanStatus) VALUES(?, ?, ?, ?, ?)";
        jdbcClient.sql(insertQuery)
                .param(1, UUID.randomUUID().toString())
                .param(2, loan.getCustomerName())
                .param(3, loan.getCustomerId())
                .param(4, loan.getAmount())
                .param(5, loan.getLoanStatus().toString())
                .update();
    }
}
