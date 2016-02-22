package com.patientadmission.domain;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by Administrator on 2/18/2015.
 */
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class SecurityQuestion{

    private String question;
    private String answer;


}
