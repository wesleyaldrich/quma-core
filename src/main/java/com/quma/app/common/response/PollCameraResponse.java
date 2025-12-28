package com.quma.app.common.response;

import com.quma.app.common.dto.FrResponseDto;
import com.quma.app.common.dto.QrResponseDto;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class PollCameraResponse extends QumaResponse {
    @Builder.Default
    private boolean valid = false;
}
