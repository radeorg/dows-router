package org.dows.router.open;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dows.router.handler.RouterHandler;
import org.dows.router.handler.model.RouterRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "路由器", description = "路由器")
public class RouterRest {

    private final RouterHandler rourouterHandler;

    @PostMapping("/v1/open/router/submit")
    @Operation(summary = "新增对象")
    public void submit(@RequestBody RouterRequest routerRequest) {
        log.info("提交任务");
        rourouterHandler.submitRequest("test", "appId", 1L, 1L, 1);


    }
}
