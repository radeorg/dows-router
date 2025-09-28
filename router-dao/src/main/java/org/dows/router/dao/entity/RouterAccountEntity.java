package org.dows.router.dao.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 路由账户实体
 *
 * @author lait.zhang@gmail.com
 * @since 1.0.0
 */
@Table("router_account")
@Data
@Schema(description = "路由账户")
public class RouterAccountEntity {

    @Id(keyType = KeyType.Generator, value = KeyGenerators.snowFlakeId)
    @Schema(description = "路由账户ID")
    private Long routerAccountId;

    @Column(value = "account_instance_id")
    @Schema(description = "账号实例ID")
    private Long accountInstanceId;

    @Column(value = "amount")
    @Schema(description = "总额")
    private BigDecimal amount;

    @Column(value = "balance")
    @Schema(description = "余额")
    private BigDecimal balance;

    @Column(value = "app_id")
    @Schema(description = "应用id")
    private String appId;

    @Column(value = "deleted")
    @Schema(description = "是否删除:0未删除,1已删除")
    private Byte deleted;

    @Column(value = "ts")
    @Schema(description = "时间戳")
    private LocalDateTime ts;

    @Column(value = "ut")
    @Schema(description = "更新时间")
    private LocalDateTime ut;
}