package com.enba.cloud.orders.ordercomment.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enba.boot.core.base.Result;
import com.enba.boot.core.enums.StatusEnum;
import com.enba.cloud.common.annotation.CurrentUser;
import com.enba.cloud.orders.api.ordercomment.entity.OrderComment;
import com.enba.cloud.orders.ordercomment.service.IOrderCommentService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 订单评价表
 *
 * @author 恩爸编程
 * @since 2025-05-26
 */
@RestController
@RequestMapping("/api/orderComment")
public class OrderCommentController {

  @Resource private IOrderCommentService orderCommentService;

  /**
   * 根据订单ID查询数据接口
   *
   * @param orderId orderId
   * @return result
   */
  @GetMapping("/{orderId}")
  public Result<List<OrderComment>> findByOrderId(@PathVariable Integer orderId) {
    QueryWrapper<OrderComment> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("order_id", orderId);
    return Result.success(orderCommentService.list(queryWrapper));
  }

  /**
   * 根据id查询数据接口
   *
   * @param id id
   * @return result
   */
  @GetMapping("/{id}")
  public Result<OrderComment> findOne(@PathVariable Integer id) {
    return Result.success(orderCommentService.getById(id));
  }

  /**
   * 分页查询接口
   *
   * @param pageNum nume
   * @param pageSize size
   * @return result
   */
  @GetMapping("/page")
  public Result<Page<OrderComment>> findPage(
      @RequestParam Integer pageNum, @RequestParam Integer pageSize) {
    QueryWrapper<OrderComment> queryWrapper = new QueryWrapper<>();
    return Result.success(orderCommentService.page(new Page<>(pageNum, pageSize), queryWrapper));
  }

  /**
   * 新增和更新接口
   *
   * @param orderComment orderComment
   * @return result
   */
  @PostMapping
  public Result<Boolean> save(@RequestBody OrderComment orderComment, @CurrentUser Long userId) {
    // 是否已评价
    if (orderCommentService.exists(
        new QueryWrapper<OrderComment>()
            .eq("order_id", orderComment.getOrderId())
            .eq("user_id", userId))) {
      return Result.err(StatusEnum.ERR.getCode(), "您已评价过此订单");
    }

    return Result.success(orderCommentService.saveOrUpdate(orderComment));
  }

  /**
   * 删除接口
   *
   * @param id id
   * @return result
   */
  @DeleteMapping("/{id}")
  public Result<Boolean> delete(@PathVariable Integer id) {
    return Result.success(orderCommentService.removeById(id));
  }

  /**
   * 批量删除接口
   *
   * @param ids ids
   * @return result
   */
  @PostMapping("/del/batch")
  public Result<Boolean> deleteBatch(@RequestBody List<Integer> ids) {
    return Result.success(orderCommentService.removeByIds(ids));
  }
}
