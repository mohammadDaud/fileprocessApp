package com.file.fileprocess.dtos;

import com.file.fileprocess.annotation.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankMarketingDTO {

    private String id;

    @ExcelColumn("age")
    private Integer age;

    @ExcelColumn("job")
    private String job;

    @ExcelColumn("marital")
    private String marital;

    @ExcelColumn("education")
    private String education;

    @ExcelColumn("default")
    private String defaultStatus;

    @ExcelColumn("housing")
    private String housing;

    @ExcelColumn("loan")
    private String loan;

    @ExcelColumn("contact")
    private String contact;

    @ExcelColumn("month")
    private String month;

    @ExcelColumn("day_of_week")
    private String dayOfWeek;

    @ExcelColumn("duration")
    private Integer duration;

    @ExcelColumn("campaign")
    private Integer campaign;

    @ExcelColumn("pdays")
    private Integer pdays;

    @ExcelColumn("previous")
    private Integer previous;

    @ExcelColumn("poutcome")
    private String poutcome;

    @ExcelColumn("emp.var.rate")
    private Double empVarRate;

    @ExcelColumn("cons.price.idx")
    private Double consPriceIdx;

    @ExcelColumn("cons.conf.idx")
    private Double consConfIdx;

    @ExcelColumn("euribor3m")
    private Double euribor3m;

    @ExcelColumn("nr.employed")
    private Double nrEmployed;

    @ExcelColumn("yes-no")
    private String yesNo;
}