package com.file.fileprocess.services;

import com.file.fileprocess.batch.GenericBatchRepository;
import com.file.fileprocess.dtos.BankMarketingDTO;
import com.file.fileprocess.mapper.BankMarketingMapper;
import com.file.fileprocess.model.BankMarketing;
import com.file.fileprocess.repository.BankMarketingRepository;
import com.file.fileprocess.util.UuidGeneratorUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BankMarketingService {

    private static final int BATCH_SIZE = 1000;

    private final GenericBatchRepository batchRepository;
    private final BankMarketingMapper mapper;

    private static final String INSERT_SQL = """
        INSERT INTO bank_marketing
        (
            id,
            age,
            job,
            marital,
            education,
            default_status,
            housing,
            loan,
            contact,
            month,
            day_of_week,
            duration,
            campaign,
            pdays,
            previous,
            poutcome,
            emp_var_rate,
            cons_price_idx,
            cons_conf_idx,
            euribor3m,
            nr_employed,
            yes_no
        )
        VALUES
        (
            ?,?,?,?,?,?,?,?,?,?,
            ?,?,?,?,?,?,?,?,?,?,
            ?,?
        )
        """;

    @Transactional
    public void saveAll(
            List<BankMarketingDTO> dtos) {

        List<BankMarketing> entities =
                dtos.stream()
                        .map(mapper::toEntity)
                        .toList();

        batchRepository.batchInsert(
                INSERT_SQL,
                entities,
                BATCH_SIZE,
                (ps, entity) -> {

                    ps.setString(1, entity.getId());
                    ps.setObject(2, entity.getAge());
                    ps.setString(3, entity.getJob());
                    ps.setString(4, entity.getMarital());
                    ps.setString(5, entity.getEducation());
                    ps.setString(6, entity.getDefaultStatus());
                    ps.setString(7, entity.getHousing());
                    ps.setString(8, entity.getLoan());
                    ps.setString(9, entity.getContact());
                    ps.setString(10, entity.getMonth());
                    ps.setString(11, entity.getDayOfWeek());
                    ps.setObject(12, entity.getDuration());
                    ps.setObject(13, entity.getCampaign());
                    ps.setObject(14, entity.getPdays());
                    ps.setObject(15, entity.getPrevious());
                    ps.setString(16, entity.getPoutcome());
                    ps.setObject(17, entity.getEmpVarRate());
                    ps.setObject(18, entity.getConsPriceIdx());
                    ps.setObject(19, entity.getConsConfIdx());
                    ps.setObject(20, entity.getEuribor3m());
                    ps.setObject(21, entity.getNrEmployed());
                    ps.setString(22, entity.getYesNo());
                });

        log.info("Inserted {} records successfully",
                entities.size());
    }
}