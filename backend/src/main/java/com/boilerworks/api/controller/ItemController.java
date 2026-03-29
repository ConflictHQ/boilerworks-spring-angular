package com.boilerworks.api.controller;

import com.boilerworks.api.dto.*;
import com.boilerworks.api.model.Item;
import com.boilerworks.api.service.ItemService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/items")
@RequiredArgsConstructor
public class ItemController {

  private final ItemService itemService;

  @GetMapping
  @PreAuthorize("hasAuthority('item.view')")
  public List<ItemDto> list(@RequestParam(required = false) String search) {
    return itemService.findAll(search);
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAuthority('item.view')")
  public ItemDto get(@PathVariable UUID id) {
    return itemService.findById(id);
  }

  @PostMapping
  @PreAuthorize("hasAuthority('item.add')")
  public ApiResponse<ItemDto> create(@Valid @RequestBody CreateItemRequest request) {
    Item item = itemService.create(request);
    return ApiResponse.ok(ItemDto.from(item));
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasAuthority('item.change')")
  public ApiResponse<ItemDto> update(
      @PathVariable UUID id, @Valid @RequestBody UpdateItemRequest request) {
    Item item = itemService.update(id, request);
    return ApiResponse.ok(ItemDto.from(item));
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('item.delete')")
  public ApiResponse<Void> delete(@PathVariable UUID id) {
    itemService.softDelete(id);
    return ApiResponse.ok(null);
  }
}
