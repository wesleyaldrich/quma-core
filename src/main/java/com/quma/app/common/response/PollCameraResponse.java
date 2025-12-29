package com.quma.app.common.response;

import com.quma.app.entity.Session;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class PollCameraResponse extends QumaResponse {
    private boolean responded;
    private boolean valid;
}
