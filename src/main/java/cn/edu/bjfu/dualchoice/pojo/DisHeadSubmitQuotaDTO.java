package cn.edu.bjfu.dualchoice.pojo;

import lombok.Data;

import java.util.List;

@Data
public class DisHeadSubmitQuotaDTO {
    String primarySubject;
    List<SecQuotaDTO> quota;
}
