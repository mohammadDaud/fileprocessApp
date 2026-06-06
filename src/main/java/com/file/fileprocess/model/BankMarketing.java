package com.file.fileprocess.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "bank_marketing")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankMarketing {

    @Id
    @Column(name = "id", nullable = false, unique = true,length = 37)
    private String id;

    @Column(name = "age")
    private Integer age;

    @Column(name = "job")
    private String job;

    @Column(name = "marital")
    private String marital;

    @Column(name = "education")
    private String education;

    @Column(name = "default_status")
    private String defaultStatus;

    @Column(name = "housing")
    private String housing;

    @Column(name = "loan")
    private String loan;

    @Column(name = "contact")
    private String contact;

    @Column(name = "month")
    private String month;

    @Column(name = "day_of_week")
    private String dayOfWeek;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "campaign")
    private Integer campaign;

    @Column(name = "pdays")
    private Integer pdays;

    @Column(name = "previous")
    private Integer previous;

    @Column(name = "poutcome")
    private String poutcome;

    @Column(name = "emp_var_rate")
    private Double empVarRate;

    @Column(name = "cons_price_idx")
    private Double consPriceIdx;

    @Column(name = "cons_conf_idx")
    private Double consConfIdx;

    @Column(name = "euribor3m")
    private Double euribor3m;

    @Column(name = "nr_employed")
    private Double nrEmployed;

    @Column(name = "yes_no", length = 3)
    private String yesNo;
}