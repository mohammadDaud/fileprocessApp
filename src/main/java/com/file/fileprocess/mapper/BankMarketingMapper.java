package com.file.fileprocess.mapper;

import com.file.fileprocess.dtos.BankMarketingDTO;
import com.file.fileprocess.model.BankMarketing;
import com.file.fileprocess.util.UuidGeneratorUtil;
import org.springframework.stereotype.Component;

@Component
public class BankMarketingMapper {

    public BankMarketing toEntity(BankMarketingDTO dto) {

        return BankMarketing.builder()
                .id(UuidGeneratorUtil.generateId())
                .age(dto.getAge())
                .job(dto.getJob())
                .marital(dto.getMarital())
                .education(dto.getEducation())
                .defaultStatus(dto.getDefaultStatus())
                .housing(dto.getHousing())
                .loan(dto.getLoan())
                .contact(dto.getContact())
                .month(dto.getMonth())
                .dayOfWeek(dto.getDayOfWeek())
                .duration(dto.getDuration())
                .campaign(dto.getCampaign())
                .pdays(dto.getPdays())
                .previous(dto.getPrevious())
                .poutcome(dto.getPoutcome())
                .empVarRate(dto.getEmpVarRate())
                .consPriceIdx(dto.getConsPriceIdx())
                .consConfIdx(dto.getConsConfIdx())
                .euribor3m(dto.getEuribor3m())
                .nrEmployed(dto.getNrEmployed())
                .yesNo(dto.getYesNo())
                .build();
    }
}